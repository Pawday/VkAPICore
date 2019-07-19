package per.pawday.vkbot.modularity.routing;


import org.json.JSONArray;
import org.json.JSONObject;
import per.pawday.vkbot.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
public class ModuleRouter
{
	private static File routerFile = new File("router.json");
	private static ModuleRouter router = new ModuleRouter();
	private static boolean inited = false;

	private static JSONArray routersArray;

	public void load()
	{

	}

	public static void init() throws IOException
	{
		if (!inited)
		{

			routerFile.delete();
			routerFile.createNewFile();

			OutputStream outputStream = new FileOutputStream(routerFile);



			JSONArray intoFileArray = new JSONArray();

			JSONObject routeJson = new JSONObject();
			routeJson.put("moduleAlias", "");
			routeJson.put("moduleFile", "");
			routeJson.put("moduleType", "java");


			JSONObject javaOptions = new JSONObject();
				javaOptions.put("vmOptions",new JSONArray());

			routeJson.put("javaOptions",javaOptions);
			routeJson.put("processLiveTime",3);


			routeJson.put("botsLongPollEvent","");
			routeJson.put("userLongPollEvent",0);

				JSONObject messageOptions = new JSONObject();
				messageOptions.put("message",new JSONArray());
				messageOptions.put("separator"," ");

					JSONObject callSignOps = new JSONObject();
					callSignOps.put("firstSymbols",new JSONArray(new String[] {"!","@","#","$","%","^","&","*","/","\\"}));
					callSignOps.put("names",new JSONArray());

				messageOptions.put("callSignOptions",callSignOps);

			routeJson.put("messageOption",messageOptions);
			//System.out.println(Main.formatter.formatToString(routeJson.toString()));
			intoFileArray.put(routeJson);


			outputStream.write(Main.formatter.formatToString(intoFileArray.toString()).getBytes(StandardCharsets.UTF_8));
			outputStream.flush();
			outputStream.close();
		}
	}
}
