package per.pawday.vkbot.handlers;

import org.json.JSONObject;
import org.json.JSONTokener;
import per.pawday.vkbot.console.ConsoleColors;
import per.pawday.vkbot.vk.VkRequester;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class UserLongPollHandler implements Runnable
{
	public UserLongPollHandler(String token, String apiVersion)
	{
		this.requester = new VkRequester(apiVersion,token);
	}

	SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
	private Map<String,String> params = new HashMap<>();

	private String server;
	private String key;
	private String ts;
	private String pts;
	private VkRequester requester;

	private JSONObject response;

	private SSLSocket socket;

	private void getParams()
	{
		params.clear();
		params.put("need_pts","1");
		params.put("lp_version","3");
		JSONObject res = null;
		try
		{
			res = requester.post("messages.getLongPollServer",params);
			this.key = res.getJSONObject("response").getString("key");
			this.server = res.getJSONObject("response").getString("server");
			this.pts = String.valueOf(res.getJSONObject("response").getInt("pts"));
			this.ts = String.valueOf(res.getJSONObject("response").getInt("ts"));
		}
		catch (IOException e)
		{
			System.out.println(ConsoleColors.RED + "Проблемма с доступом к серверу VK" + ConsoleColors.RESET);
			System.exit(-1);
		}
	}

	@Override
	public void run()
	{

		this.getParams();

		while (true)
		{
			this.response = lpReq();
			if (this.response.has("failed"))
			{
				//TODO: remove yellow logs
				System.out.println(ConsoleColors.YELLOW + "Failed: user ..........." + ConsoleColors.RESET);
				System.out.println(this.response);
				switch (this.response.getInt("failed"))
				{
					case 1:
						this.ts = String.valueOf(this.response.getString("ts"));
						continue;
					case 2:
					case 3:
						this.getParams();
						continue;
				}
				System.out.println(ConsoleColors.YELLOW + "//......................." + ConsoleColors.RESET);
			}
			//TODO: At this point, the event must be sent to the distributors.
			System.out.println(response);
			this.ts = String.valueOf(response.getInt("ts"));
		}
	}

	private JSONObject lpReq()
	{
		String reqBody = "act=a_check" + "&" +
				"key=" + this.key + "&" +
				"ts="+ this.ts + "&" +
				"wait=25" +"&" +
				"mode=255" + "&" +
				"version=3";

		byte[] s =
				"POST /".concat(server.split("/")[1]).concat(" HTTP/1.1\n" +
				"Host: " + server.split("/")[0] + "\n" +
				"Connection: close\n" +
				"Content-Length: ".concat(String.valueOf(reqBody.length())).concat("\n\n").concat(reqBody)).getBytes(StandardCharsets.UTF_8);
		JSONObject object = null;

		try
		{
			socket = (SSLSocket) ssf.createSocket(this.server.split("/")[0], 443);

			{
				OutputStream out = socket.getOutputStream();
				out.write(s);
			}
			InputStream in = socket.getInputStream();
			{
				char[] chars = new char[] {'a','b','c','d'};
				int b;

				while (true)
				{
					if ((b = in.read()) != -1)
					{

						chars[0] = chars[1];
						chars[1] = chars[2];
						chars[2] = chars[3];
						chars[3] = (char) b;

						if
						(
							chars[0] == '\r' &&
							chars[1] == '\n' &&
							chars[2] == '\r' &&
							chars[3] == '\n'
						) break;
					}
					else
						throw new IOException("");
				}

				object = new JSONObject(new JSONTokener(in));

				socket.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println(ConsoleColors.RED + "Проблемма с доступом к серверу VK" + ConsoleColors.RESET);
			System.exit(-1);
		}
		return object;
	}
}
