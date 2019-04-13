package per.pawday.vkbot;

import org.json.simple.JSONArray;
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
    private final static File configsToken = new File(configsDir.getName().concat("/tokens.json"));
    private final static File configsDatabase = new File(configsDir.getName().concat("/database.json"));


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

        //tokens.json
        if (! configsToken.exists())
        {

            try
            {
                configsToken.createNewFile();
                FileOutputStream out = new FileOutputStream(configsToken);

                out.write(JsonFormatter.formatToString(configs.tokens.tokenObject.toJSONString(),"\t").getBytes());
                out.close();

            } catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + configsDir.getName() + "/tokens.json");
                e.printStackTrace();
                System.exit(1);
            }
        }
        //database.json
        if (! configsDatabase.exists())
        {

            try
            {
                configsDatabase.createNewFile();
                FileOutputStream out = new FileOutputStream(configsDatabase);

                out.write(JsonFormatter.formatToString(configs.database.databaseObject.toJSONString(),"\t").getBytes());
                out.close();

            } catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + configsDatabase.getName() + "/tokens.json");
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    private static void prepareObjects()
    {
        //tokens.json
        {
            try
            {

                InputStream stream = new FileInputStream(configsToken);
                JSONObject fileObject = (JSONObject) parser.parse(new InputStreamReader(stream));
                stream.close();


                if (fileObject.get("tokens") != null)
                {
                    configs.tokens.tokenObject = fileObject;
                }
            }
            catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + configsDir.getName() + "/tokens.json");
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

        //database.json
        {
            try
            {
                InputStream stream = new FileInputStream(configsDatabase);
                JSONObject fileObject = (JSONObject) parser.parse(new InputStreamReader(stream));
                stream.close();

                if
                (
                        fileObject.get("MySQL") != null &&
                        fileObject.get("PostgreSQL") != null &&
                        fileObject.get("SQLite") != null
                )
                {
                    JSONObject mysql = (JSONObject) fileObject.get("MySQL");
                    JSONObject postgresql = (JSONObject) fileObject.get("PostgreSQL");
                    JSONObject sqlite = (JSONObject) fileObject.get("SQLite");

                    if    //   ( ╯°□°)╯  ┻━━━━┻
                    (

                        mysql.get("uses") != null &&
                        mysql.get("host") != null &&
                        mysql.get("user") != null &&
                        mysql.get("password") != null &&
                        mysql.get("port") != null &&

                        postgresql.get("uses") != null &&
                        postgresql.get("host") != null &&
                        postgresql.get("user") != null &&
                        postgresql.get("password") != null &&
                        postgresql.get("port") != null &&

                        sqlite.get("uses") != null &&
                        sqlite.get("path") != null
                    )
                    {
                        configs.database.databaseObject = fileObject;
                    }
                }

            }
            catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + configsDir.getName() + "/database.json");
                e.printStackTrace();
                System.exit(1);
            }
            catch (ParseException e)
            {
                System.out.println(ConsoleColors.RED + "File " + configsDir.getName() + "/" + configsDatabase.getName() + " is broken" + ConsoleColors.RESET);
                e.printStackTrace();
                System.exit(1);
            }


        }
    }

    private static void writeToFiles()
    {
        //tokens.json
        {
            try
            {
                FileOutputStream out = new FileOutputStream(configsToken);
                out.write(JsonFormatter.formatToString(configs.tokens.tokenObject.toJSONString(),"\t").getBytes(StandardCharsets.UTF_8));
                out.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
            catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + configsDir.getName() + "/tokens.json");
                e.printStackTrace();
                System.exit(1);
            }
        }
        {
            try
            {
                FileOutputStream out = new FileOutputStream(configsDatabase);
                out.write(JsonFormatter.formatToString(configs.database.databaseObject.toJSONString(),"\t").getBytes(StandardCharsets.UTF_8));
                out.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
            catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + configsDir.getName() + "/database.json");
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    private static void setParams()
    {
        //tokens.json
        {

            JSONArray tokensJsonArray = (JSONArray) configs.tokens.tokenObject.get("tokens");
            String[] tokens = new String[tokensJsonArray.size()];

            for (int i = 0; i < tokens.length; i++)
            {
                tokens[i] = (String) tokensJsonArray.get(i);
            }

            configs.tokens.tokens = tokens;

        }

        //database.json
        {

            JSONObject mysql = (JSONObject) configs.database.databaseObject.get("MySQL");
            JSONObject postgresql = (JSONObject) configs.database.databaseObject.get("PostgreSQL");
            JSONObject sqlite = (JSONObject) configs.database.databaseObject.get("SQLite");

            configs.database.MySQL.uses = (boolean) mysql.get("uses");
            configs.database.MySQL.host = (String) mysql.get("host");
            configs.database.MySQL.user = (String) mysql.get("user");
            configs.database.MySQL.password = (String) mysql.get("password");
            configs.database.MySQL.port = (String) mysql.get("port");

            configs.database.PostgreSQL.uses = (boolean) postgresql.get("uses");
            configs.database.PostgreSQL.host = (String) postgresql.get("host");
            configs.database.PostgreSQL.user = (String) postgresql.get("user");
            configs.database.PostgreSQL.password = (String) postgresql.get("password");
            configs.database.PostgreSQL.port = (String) postgresql.get("port");

            configs.database.SQLite.uses = (boolean) sqlite.get("uses");
            configs.database.SQLite.path = (String) sqlite.get("path");
        }
    }

    public static void removeFiles()
    {
        configsToken.delete();
        configsDatabase.delete();
    }

    public static void returnFiles()
    {
        createFiles();
        writeToFiles();
    }

    public static class TheConfigs
    {
        public Tokens tokens = new Tokens();
        public Database database = new Database();




        public static class Tokens
        {
            private String[] tokens;
            private JSONObject tokenObject = new JSONObject();

            Tokens()
            {
                tokenObject.put("tokens", new JSONArray());
            }

            public String[] getTokens()
            {
                return tokens;
            }


        }

        public static class Database
        {
            public MySQL MySQL = new MySQL();
            public PostgreSQL PostgreSQL = new PostgreSQL();
            public SQLite SQLite = new SQLite();

            private JSONObject databaseObject = new JSONObject();

            Database()
            {
                JSONObject mysql = new JSONObject();

                mysql.put("uses",true);
                mysql.put("host","");
                mysql.put("user","");
                mysql.put("password","");
                mysql.put("port","");

                databaseObject.put("MySQL",mysql);

                JSONObject postgresql = new JSONObject();

                postgresql.put("uses" , false);
                postgresql.put("host" , "");
                postgresql.put("user" , "");
                postgresql.put("password" , "");
                postgresql.put("port" , "");

                databaseObject.put("PostgreSQL" , postgresql);


                JSONObject sqlite = new JSONObject();

                sqlite.put("uses" , false);
                sqlite.put("path" , "");

                databaseObject.put("SQLite",sqlite);

            }


            public static class MySQL
            {
                private boolean uses;
                private String host;
                private String user;
                private String password;
                private String port;

                public boolean isUses()
                {
                    return uses;
                }

                public String getHost()
                {
                    return host;
                }

                public String getUser()
                {
                    return user;
                }

                public String getPassword()
                {
                    return password;
                }

                public String getPort()
                {
                    return port;
                }
            }

            public static class PostgreSQL
            {
                private boolean uses;
                private String host;
                private String user;
                private String password;
                private String port;

                public boolean isUses()
                {
                    return uses;
                }

                public String getHost()
                {
                    return host;
                }

                public String getUser()
                {
                    return user;
                }

                public String getPassword()
                {
                    return password;
                }

                public String getPort()
                {
                    return port;
                }
            }

            public static class SQLite
            {
                private boolean uses;
                private String path;

                public boolean isUses()
                {
                    return uses;
                }

                public String getPath()
                {
                    return path;
                }
            }


        }

    }

}