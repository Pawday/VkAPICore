package per.pawday.vkbot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import per.pawday.vkbot.console.ConsoleColors;
import per.pawday.vkbot.json.JsonFormatter;

import java.io.*;
import java.nio.charset.StandardCharsets;

class Configs
{

    //tools

    private static JSONParser parser = new JSONParser();


    //directory
    private final static File configsDir = new File("configs");


    public static TheConfigs configs = new TheConfigs();


    //files
    private final static File configsToken = new File(configsDir.getName().concat("/token.json"));



    static
    {
        if (! configsDir.exists())
        {
            configsDir.mkdir();
        }

        createFiles();

        prepareObjects();

        writeToFiles();

        setParams();


        removeFiles();

    }


    private static void createFiles()
    {

        //token.json
        if (! configsToken.exists())
        {

            try
            {
                configsToken.createNewFile();
                FileOutputStream out = new FileOutputStream(configsToken);

                out.write(JsonFormatter.formatToString(configs.token.tokenObject.toJSONString(),"\t").getBytes());
                out.close();

            } catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + configsDir.getName() + "/token.json");
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    private static void prepareObjects()
    {
        //token.json
        {
            try
            {

                InputStream stream = new FileInputStream(configsToken);
                JSONObject fileObject = (JSONObject) parser.parse(new InputStreamReader(stream));
                stream.close();


                if (fileObject.get("token") != null)
                {
                    configs.token.tokenObject.put("token",fileObject.get("token"));
                }
            }
            catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + configsDir.getName() + "/token.json");
                e.printStackTrace();
                System.exit(1);
            }
            catch (ParseException e)
            {
                System.out.println(ConsoleColors.RED + "File " + configsDir.getName() + "/" + configsToken.getName() + " is broken" + ConsoleColors.RESET);
                e.printStackTrace();
                System.exit(1);
            }

        }
    }

    private static void writeToFiles()
    {
        //token.json
        {
            try
            {
                FileOutputStream out = new FileOutputStream(configsToken);
                out.write(JsonFormatter.formatToString(configs.token.tokenObject.toJSONString(),"\t").getBytes(StandardCharsets.UTF_8));
                out.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
            catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + configsDir.getName() + "/token.json");
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    private static void setParams()
    {
        //token.json
        {
            configs.token.token = (String) configs.token.tokenObject.get("token");
        }
    }

    public static void removeFiles()
    {
        File[] files = configsDir.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            System.out.println(files[i].delete());
        }
    }

    public static void returnFiles()
    {
        createFiles();

        prepareObjects();

        writeToFiles();

        setParams();
    }

    public static class TheConfigs
    {
        public Token token = new Token();




        public static class Token
        {
            private String token;
            private JSONObject tokenObject = new JSONObject();


            public Token()
            {
                tokenObject.put("token","");
            }

            public String getToken()
            {
                return token;
            }


        }

    }

}