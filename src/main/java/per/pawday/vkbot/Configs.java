package per.pawday.vkbot;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import per.pawday.jsonFormatter.JsonFormatter;
import per.pawday.jsonFormatter.constants.IndentChars;
import per.pawday.jsonFormatter.constants.IndentsStyles;
import per.pawday.jsonFormatter.exceptions.JsonFormatterException;

import java.io.*;
import java.nio.charset.StandardCharsets;

class Configs
{
    private static JsonFormatter formatter;

    static
    {
        try
        {
            formatter = new JsonFormatter(IndentsStyles.ERIC_ALLMANS_STYLE, IndentChars.TAB,1);
        } catch (JsonFormatterException e)
        {
            // i used embedded constants
        }
        tokenFile = new File("configs" + File.separator + "tokens.json");
        tokens = new Tokens();
    }


    public static Tokens tokens;


    private static File tokenFile;
    static boolean inited = false;


    static void init() throws IOException
    {

        tokenFile = new File("configs" + File.separator + "tokens.json");

        if (!tokenFile.exists())
        {
            if(!tokenFile.createNewFile())  throw new IOException("File creating exception");
        }
        else
        {
            if (!tokenFile.delete()) throw new IOException("File removing exception");
            if (!tokenFile.createNewFile())  throw new IOException("File creating exception");
        }


        JSONObject tokensObject = new JSONObject();

        tokensObject.put("group_token","");
        tokensObject.put("admin_token","");
        tokensObject.put("group_id","");

        FileOutputStream tokenOutputStream = new FileOutputStream(tokenFile);

        tokenOutputStream.write(formatter.formatToString(tokensObject.toString()).getBytes(StandardCharsets.UTF_8));
        tokenOutputStream.close();

        tokens = new Tokens();
        inited = true;

    }

    static class Tokens
    {
        private String groupToken;
        private String adminToken;
        private String groupId;

        Tokens()
        {
            FileInputStream inputStream = null;
            try
            {
                if (!tokenFile.exists())
                {
                    init();
                }
                inputStream = new FileInputStream(tokenFile);

                JSONObject object = new JSONObject(new JSONTokener(inputStream));
                this.groupToken= object.getString("group_token");
                this.adminToken = object.getString("admin_token");
                this.groupId = object.getString("group_id");

            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    // already closed
                }
            }
        }

        public String getAdminToken()
        {
            return adminToken;
        }

        public String getGroupToken()
        {
            return groupToken;
        }

        public String getGroupId()
        {
            return groupId;
        }
    }
}