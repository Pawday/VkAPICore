package per.pawday.vkbot.main;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import per.pawday.vkbot.configs.Advanced;
import per.pawday.vkbot.configs.Database;
import per.pawday.vkbot.configs.Token;
import per.pawday.vkbot.databases.mysql.MySQL;
import per.pawday.vkbot.databases.postgresql.PostgreSQL;
import per.pawday.vkbot.databases.sqlite.SQLite;
import per.pawday.vkbot.module.Loader;
import per.pawday.vkbot.vk.functions.Request;


public class Main
{
    public static String coreVersion = "1.0.0";
    public static void main(String[] args)
    {
        Token token = new Token();

        //configs
        Database dbConfigs = new Database();
        per.pawday.vkbot.configs.Main configs = new per.pawday.vkbot.configs.Main();
        Advanced advancedConf = new Advanced();

        //databases
        MySQL myDB = null;
        PostgreSQL poDB = null;
        SQLite sqDB = null;

        //functions
        Request vkReq = new Request();


        //variables

        String tokenType;   // "User" , "Group"





        //Start


        //check configs validity

        {

            //databases configs
            {
                if (! (dbConfigs.PostgreSQL.configs.usage || dbConfigs.MySQL.configs.usage || dbConfigs.SQLite.configs.usage))
                {
                    System.out.println("Должна использоваться хотя бы одна база данных!");
                    System.out.println("(Эту ошибку вызывают поля usage в файле configs/database.json)");
                    System.exit(-1);
                }
                else
                {
                    if (dbConfigs.PostgreSQL.configs.usage)
                    {
                        poDB = new PostgreSQL();
                        poDB.сonnect();
                        poDB.close();
                    }
                    if (dbConfigs.MySQL.configs.usage)
                    {
                        myDB = new MySQL();
                        myDB.сonnect();
                        myDB.close();
                    }

                    if (dbConfigs.SQLite.configs.usage)
                    {
                        sqDB = new SQLite();
                        sqDB.connect();
                        sqDB.close();
                    }
                }

                // check primary

                if ( (! dbConfigs.SQLite.configs.usage) && (dbConfigs.PostgreSQL.configs.usage && dbConfigs.MySQL.configs.usage))
                {
                    if ((dbConfigs.MySQL.configs.primary && dbConfigs.PostgreSQL.configs.primary))
                    {
                        System.out.println("Первичной может быть только одна база данных!");
                        System.exit(-1);
                    }
                    else if (! (dbConfigs.MySQL.configs.primary || dbConfigs.PostgreSQL.configs.primary))
                    {
                        System.out.println("Не указана первичная база данных!");
                        System.exit(-1);
                    }
                    dbConfigs.SQLite.configs.primary = false;
                }

                if ( (! dbConfigs.MySQL.configs.usage) && (dbConfigs.PostgreSQL.configs.usage && dbConfigs.SQLite.configs.usage))
                {
                    if ((dbConfigs.SQLite.configs.primary && dbConfigs.PostgreSQL.configs.primary))
                    {
                        System.out.println("Первичной может быть только одна база данных!");
                        System.exit(-1);
                    }
                    else if (! (dbConfigs.PostgreSQL.configs.primary || dbConfigs.SQLite.configs.primary))
                    {
                        System.out.println("Не указана первичная база данных!");
                        System.exit(-1);
                    }
                    dbConfigs.MySQL.configs.primary = false;
                }

                if ( (! dbConfigs.PostgreSQL.configs.usage) && (dbConfigs.MySQL.configs.usage && dbConfigs.SQLite.configs.usage))
                {
                    if ((dbConfigs.SQLite.configs.primary && dbConfigs.MySQL.configs.primary))
                    {
                        System.out.println("Первичной может быть только одна база данных!");
                        System.exit(-1);
                    }
                    else if  (! (dbConfigs.MySQL.configs.primary || dbConfigs.SQLite.configs.primary))
                    {
                        System.out.println("Не указана первичная база данных!");
                        System.exit(-1);
                    }
                    dbConfigs.PostgreSQL.configs.primary = false;
                }

                if ((dbConfigs.MySQL.configs.usage) && (! dbConfigs.PostgreSQL.configs.usage && ! dbConfigs.SQLite.configs.usage))
                {
                    dbConfigs.MySQL.configs.primary = true;
                    dbConfigs.PostgreSQL.configs.primary = false;
                    dbConfigs.SQLite.configs.primary = false;
                }

                if ((dbConfigs.PostgreSQL.configs.usage) && (! dbConfigs.MySQL.configs.usage && ! dbConfigs.SQLite.configs.usage))
                {
                    dbConfigs.PostgreSQL.configs.primary = true;
                    dbConfigs.MySQL.configs.primary = false;
                    dbConfigs.SQLite.configs.primary = false;
                }

                if ((dbConfigs.SQLite.configs.usage) && (! dbConfigs.MySQL.configs.usage && ! dbConfigs.PostgreSQL.configs.usage))
                {
                    dbConfigs.SQLite.configs.primary = true;
                    dbConfigs.MySQL.configs.primary = false;
                    dbConfigs.PostgreSQL.configs.primary = false;
                }

                if (dbConfigs.SQLite.configs.usage &&  dbConfigs.MySQL.configs.usage &&  dbConfigs.PostgreSQL.configs.usage)
                {
                    if (dbConfigs.MySQL.configs.primary && dbConfigs.PostgreSQL.configs.primary && dbConfigs.SQLite.configs.primary)
                    {
                        System.out.println("Первичной может быть только одна база данных!");
                        System.exit(-1);
                    }
                    else
                    {
                        if (dbConfigs.MySQL.configs.primary && dbConfigs.PostgreSQL.configs.primary)
                        {
                            System.out.println("Первичной может быть только одна база данных!");
                            System.exit(-1);
                        }
                        if (dbConfigs.MySQL.configs.primary && dbConfigs.SQLite.configs.primary)
                        {
                            System.out.println("Первичной может быть только одна база данных!");
                            System.exit(-1);
                        }
                        if (dbConfigs.PostgreSQL.configs.primary && dbConfigs.SQLite.configs.primary)
                        {
                            System.out.println("Первичной может быть только одна база данных!");
                            System.exit(-1);
                        }
                    }
                }
            }
        }

        //confirmation token and type detection

        {
            JSONObject res = vkReq.post(token.toString(),"users.get",new JSONObject());

            per.pawday.vkbot.vk.tools.Permission Permissions = new per.pawday.vkbot.vk.tools.Permission();



            //confirmation part
            {
                if (res.get("error") != null)
                {
                    JSONObject err = (JSONObject) res.get("error");
                    if ((long) err.get("error_code") == 5 )
                    {
                        System.out.println("Токен не действителен!");
                        System.exit(-1);
                    }
                }
            }

            //detection part
            {
                JSONArray arr = (JSONArray) res.get("response");
                if (arr.size() == 0)
                {
                    tokenType = "Group";
                }
                else
                {
                    tokenType = "User";
                }
            }
            // confirmation permissions part
            {
                boolean permission = Permissions.checkPermission(token.toString(),tokenType);

                if (!permission)
                {
                    System.out.println("У текущего токена не достаточно прав!");
                    System.exit(-1);
                }
            }
        }


        Loader moduleLoader = new Loader();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                moduleLoader.load();
                moduleLoader.run();
            }
        });

        thread.start();




















    }
}
