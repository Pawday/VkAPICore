package per.pawday.vkbot.vk;



import org.json.JSONObject;
import org.json.JSONTokener;


import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class VkRequester
{
    private static SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();




    private String vkAPIVersion;
    private String token;

    public VkRequester(String vkAPIVersion, String token)
    {
        this.vkAPIVersion = vkAPIVersion;
        this.token = token;
    }



    public JSONObject post(String method , Map<String,String> params) throws IOException
    {


        Socket socket = ssf.createSocket("api.vk.com",443);


        StringBuilder reqBody = new StringBuilder();

        reqBody.append("access_token=").append(token);
        reqBody.append("&v=").append(vkAPIVersion);

        if (params.size() != 0)
        {
            reqBody.append("&");
        }




        Object[] keys = params.keySet().toArray();
        Object[] vals = params.values().toArray();

        for (int i = 0; i < params.size(); i++)
        {
            String key = (String) keys[i];
            String val = (String) vals[i];

            reqBody.append(key).append("=").append(val);

            if (! (i + 1 == params.size()))
            {
                reqBody.append("&");
            }
        }










        OutputStream stream = socket.getOutputStream();


        String s =
                "POST /method/".concat(method).concat(" HTTP/1.1\n" +
                "Host: api.vk.com\n" +
                "Connection: close\n" +
                "Content-Length: ".concat(String.valueOf(reqBody.length())).concat("\n\n").concat(reqBody.toString()));

        stream.write(s.getBytes());



        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);


        StringBuilder builder = new StringBuilder();



        {
            int b;

            while ((b = inputStreamReader.read()) != -1)
            {
                builder.append((char) b);
            }
        }



        socket.close();




        String[] arr = builder.toString().split("\r\n\r\n");


        JSONObject reter = null;

        reter = new JSONObject(new JSONTokener(arr[1]));
        return reter;


    }


}
