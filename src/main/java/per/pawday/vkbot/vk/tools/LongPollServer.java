package per.pawday.vkbot.vk.tools;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import per.pawday.vkbot.configs.Token;
import per.pawday.vkbot.vk.functions.Request;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LongPollServer
{
    private boolean initedLpParams = false;

    private String server = null;
    private String key = null;
    private long ts = 0;

    private JSONObject getParams(Token token)
    {
        Request vkReq = new Request();

        JSONObject resp = vkReq.post(token.toString(),"messages.getLongPollServer",new JSONObject());
        resp = (JSONObject) resp.get("response");

        return resp;
    }

    private JSONObject req(String server, String key, long ts, byte waitTime)
    {
        JSONParser parser = new JSONParser();

        HttpURLConnection connection = null;
        URL url = null;
        JSONObject ret = null;


        try
        {
            url = new URL("https://" + server + "?act=a_check&key=" + key + "&ts=" + ts + "&wait=" + waitTime + "&mode=2&version=3");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            ret = (JSONObject) parser.parse(reader);






        }
        catch (MalformedURLException | ParseException e)
        {}
        catch (IOException e)
        {
            System.out.println("Не удалось одключиться к ВК.");
            e.printStackTrace();
            System.exit(-1);
        }

        return ret;
    }

    public JSONArray getEventsArr(Token token)
    {

        String server = null;
        String key = null;
        long ts = 0;


        if ( ! this.initedLpParams)
        {
            JSONObject params = this.getParams(token);
            this.server = (String) params.get("server");
            this.key = (String) params.get("key");
            this.ts = (long) params.get("ts");

            this.initedLpParams = true;
        }

        JSONObject res = this.req(this.server,this.key,this.ts,(byte) 25);

        this.ts = (long) res.get("ts");

        if (res.get("failed") != null)
        {
            JSONObject params = this.getParams(token);

            this.key = (String) params.get("key");
            this.server = (String) params.get("server");
            this.ts = (long) params.get("ts");

        }



        return (JSONArray) res.get("updates");

    }
}
