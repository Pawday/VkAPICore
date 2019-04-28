package per.pawday.vkbot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import per.pawday.vkbot.console.ConsoleColors;
import per.pawday.vkbot.json.JsonFormatter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class Configs
{

    //tools

    private static JSONParser parser = new JSONParser();


    //directory
    private final static File configsDir = new File("configs");


    public static TheConfigs configs = new TheConfigs();


    //files
    private final static File configsTokens = new File(configsDir.getName().concat("/tokens.json"));
    private final static File configsDatabase = new File(configsDir.getName().concat("/database.json"));


    public static void init()
    {
        if (! configsDir.exists())
        {
            configsDir.mkdir();
        }

        createFiles();

        prepareObjects();

        writeToFiles();

        setParams();


    }


    private static void createFiles()
    {

        //tokens.json
        if (! configsTokens.exists())
        {

            try
            {
                configsTokens.createNewFile();
                FileOutputStream out = new FileOutputStream(configsTokens);

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

                InputStream stream = new FileInputStream(configsTokens);
                JSONObject fileObject = (JSONObject) parser.parse(new InputStreamReader(stream));
                stream.close();


                if
                (
                    fileObject.get("groups_tokens") != null &&
                    fileObject.get("admins_tokens") != null
                )
                {
                    configs.tokens.tokenObject = fileObject;
                }
                else
                {
                    System.out.println(ConsoleColors.RED + "File " + configsDir.getName() + "/" + configsTokens.getName() + " был повреждён!" + ConsoleColors.RESET);
                    System.out.println(ConsoleColors.YELLOW + "Не был найден один из параметров, отредактируйте этот файл или выполните инициализацию." + ConsoleColors.RESET);
                    System.exit(1);
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
                System.out.println(ConsoleColors.RED + "File " + configsDir.getName() + "/" + configsTokens.getName() + " был повреждён!" + ConsoleColors.RESET);
                System.out.println(ConsoleColors.YELLOW + "Была повреждена структура json: это может быть: стёртая запятая , фигурная скобка , квадратная скобка" + ConsoleColors.RESET);
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
                    else
                    {
                        ArrayList<String> params = new ArrayList<>();

                        if (mysql.get("uses") == null)
                        {
                            params.add("MySQL -> uses");
                        }
                        if (mysql.get("host") == null)
                        {
                            params.add("MySQL -> host");
                        }
                        if (mysql.get("user") == null)
                        {
                            params.add("MySQL -> user");
                        }
                        if (mysql.get("password") == null)
                        {
                            params.add("MySQL -> password");
                        }
                        if (mysql.get("port") == null)
                        {
                            params.add("MySQL -> port");
                        }

                        if (postgresql.get("uses") == null)
                        {
                            params.add("PostgreSQL -> uses");
                        }
                        if (postgresql.get("host") == null)
                        {
                            params.add("PostgreSQL -> host");
                        }
                        if (postgresql.get("user") == null)
                        {
                            params.add("PostgreSQL -> user");
                        }
                        if (postgresql.get("password") == null)
                        {
                            params.add("PostgreSQL -> password");
                        }
                        if (postgresql.get("port") == null)
                        {
                            params.add("PostgreSQL -> port");
                        }

                        if (sqlite.get("uses") == null)
                        {
                            params.add("SQLite -> uses");
                        }
                        if (sqlite.get("path") == null)
                        {
                            params.add("SQLite -> path");
                        }


                        System.out.println(ConsoleColors.RED + "Файл " + configsDir.getName() + "/" + configsDatabase.getName() + " был повреждён!" + ConsoleColors.RESET);
                        if (params.size() == 1)
                        {
                            System.out.println(ConsoleColors.YELLOW + "Не был найден параметр \"" + params.get(0) + "\", добавьте этот параметр в этот файл." + ConsoleColors.RESET);
                        }
                        else
                        {
                            System.out.println(ConsoleColors.YELLOW + "Не были найдены следующие параметры:" + ConsoleColors.RESET);
                            for (int i = 0; i < params.size(); i++)
                            {
                                System.out.println((i + 1) + ". " + params.get(i));
                            }
                            System.out.println(ConsoleColors.YELLOW + "Добавьте эти параметры в этот файл или выполните инициалилацию.");
                        }

                        System.exit(1);
                    }
                }
                else
                {
                    System.out.println(ConsoleColors.RED + "File " + configsDir.getName() + "/" + configsDatabase.getName() + " был повреждён!" + ConsoleColors.RESET);
                    System.out.println(ConsoleColors.YELLOW + "Отсутствует один из параметров , отредактируйте этот файл или выполните инициализацию." + ConsoleColors.RESET);
                    System.exit(1);
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
                System.out.println(ConsoleColors.RED + "File " + configsDir.getName() + "/" + configsDatabase.getName() + " был повреждён!" + ConsoleColors.RESET);
                System.out.println(ConsoleColors.YELLOW + "Была повреждена структура json: это может быть: стёртая запятая , фигурная скобка , квадратная скобка" + ConsoleColors.RESET);
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
                FileOutputStream out = new FileOutputStream(configsTokens);
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

            JSONArray groupsTokensJsonArray = (JSONArray) configs.tokens.tokenObject.get("groups_tokens");
            JSONArray adminsTokensJsonArray = (JSONArray) configs.tokens.tokenObject.get("admins_tokens");

            String[] groupTokens = new String[groupsTokensJsonArray.size()];
            String[] adminsTokens = new String[adminsTokensJsonArray.size()];

            for (int i = 0; i < groupTokens.length; i++)
            {
                groupTokens[i] = (String) groupsTokensJsonArray.get(i);
            }

            for (int i = 0; i < adminsTokens.length; i++)
            {
                adminsTokens[i] = (String) adminsTokensJsonArray.get(i);
            }

            configs.tokens.groupsTokens = groupTokens;
            configs.tokens.adminsTokens = adminsTokens;

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
        configsTokens.delete();
        configsDatabase.delete();
    }


    public static class TheConfigs
    {
        public Tokens tokens = new Tokens();
        public Database database = new Database();




        public static class Tokens
        {
            private String[] groupsTokens;
            private String[] adminsTokens;

            private JSONObject tokenObject = new JSONObject();

            Tokens()
            {
                JSONArray sampleText = new JSONArray();
                sampleText.add("");
                tokenObject.put("groups_tokens", sampleText);
                tokenObject.put("admins_tokens", sampleText);
            }

            public String[] getGroupsTokens()
            {
                return groupsTokens;
            }
            public String[] getAdminsTokens()
            {
                return adminsTokens;
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