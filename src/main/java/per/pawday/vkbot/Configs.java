package per.pawday.vkbot;


import org.json.JSONObject;
import org.json.JSONTokener;
import per.pawday.vkbot.console.ConsoleColors;

import java.io.*;
import java.nio.charset.StandardCharsets;

class Configs
{


    public static Tokens tokens;
    private static String apiVersion = "5.101";


    private static File tokenFile;
    static boolean inited = false;

    static void init()
    {
        tokenFile = new File("configs" + File.separator + "tokens.json");
        tokens = new Tokens();
    }


    static void initFiles() throws IOException
    {

        tokenFile = new File("configs" + File.separator + "tokens.json");

        if (!tokenFile.exists())
        {
            new File("configs").delete();
            new File("configs").mkdir();
            if(!tokenFile.createNewFile()) throw new IOException("File creating exception");
        }
        else
        {
            if (!tokenFile.delete()) throw new IOException("File removing exception");
            if (!tokenFile.createNewFile())  throw new IOException("File creating exception");
        }


        JSONObject tokensObject = new JSONObject();

        tokensObject.put("group_token","");
        tokensObject.put("admin_token","");

        FileOutputStream tokenOutputStream = new FileOutputStream(tokenFile);

        tokenOutputStream.write(Main.formatter.formatToString(tokensObject.toString()).getBytes(StandardCharsets.UTF_8));
        tokenOutputStream.close();

        tokens = new Tokens();
        inited = true;

    }

    static class Tokens
    {
        private String groupToken;
        private String adminToken;

        Tokens()
        {
            FileInputStream inputStream = null;
            try
            {
                if (!tokenFile.exists())
                {
                    System.out.println(ConsoleColors.YELLOW + "Выполните инициализацию" + ConsoleColors.RESET);
                    System.exit(-1);
                }
                inputStream = new FileInputStream(tokenFile);

                JSONObject object = new JSONObject(new JSONTokener(inputStream));
                this.groupToken= object.getString("group_token");
                this.adminToken = object.getString("admin_token");

            }
            catch (FileNotFoundException ignored)
            {
                // file is EXISTS
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
    }

    public static String getApiVersion()
    {
        return apiVersion;
    }
}