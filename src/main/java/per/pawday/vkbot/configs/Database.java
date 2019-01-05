package per.pawday.vkbot.configs;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Database
{
    public MySQL MySQL = new MySQL();
    public PostgreSQL PostgreSQL = new PostgreSQL();
    public SQLite SQLite = new SQLite();

    private File dir = new File("configs");
    private File file = new File("configs/database.json");

    private void writeToFile()
    {
        String template =
                        "{\n" +
                        "\t\"MySQL\" : \n" +
                        "\t{\n" +
                        "\t\t\"usage\" : true,\n" +
                        "\t\t\"primary\":true,\n" +
                        "\t\t\"connection\":\n" +
                        "\t\t{\n" +
                        "\t\t\t\"host\":\"\",\n" +
                        "\t\t\t\"user\":\"\",\n" +
                        "\t\t\t\"password\":\"\",\n" +
                        "\t\t\t\"database\":\"\"\n" +
                        "\t\t}\n" +
                        "\t},\n" +
                        "\t\"PostgreSQL\" : \n" +
                        "\t{\n" +
                        "\t\t\"usage\" : false,\n" +
                        "\t\t\"primary\":false,\n" +
                        "\t\t\"connection\":\n" +
                        "\t\t{\n" +
                        "\t\t\t\"host\":\"\",\n" +
                        "\t\t\t\"user\":\"\",\n" +
                        "\t\t\t\"password\":\"\",\n" +
                        "\t\t\t\"database\":\"\"\n" +
                        "\t\t}\n" +
                        "\t},\n" +
                        "\t\"SQLite\" : \n" +
                        "\t{\n" +
                        "\t\t\"usage\" : false,\n" +
                        "\t\t\"primary\":true,\n" +
                        "\t}\n" +
                        "}";

        try
        {
            FileWriter writer= new FileWriter(file);
            writer.write(template);
            writer.close();
        }
        catch (FileNotFoundException e)
        {}
        catch (IOException e)
        {}

        System.out.println("Введите данные в файл configs/database.json");
        System.exit(-1);
    }

    public Database()
    {
        JSONParser parser = new JSONParser();

        if ( ! dir.exists())
        {
            dir.mkdir();
            try
            {
                file.createNewFile();
                this.writeToFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            if (!file.exists())
            {
                try
                {
                    file.createNewFile();
                    this.writeToFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        try
        {
            JSONObject main = (JSONObject) parser.parse(new FileReader(file));



            JSONObject mysql = (JSONObject) main.get("MySQL");
            JSONObject postgresql = (JSONObject) main.get("PostgreSQL");
            JSONObject sqlite = (JSONObject) main.get("SQLite");


            //..............


            this.MySQL.configs.primary = (Boolean) mysql.get("primary");
            this.MySQL.configs.usage = (Boolean) mysql.get("usage");


            JSONObject mysqlConnectionConfigs = (JSONObject) mysql.get("connection");

            this.MySQL.connection.host = (String) mysqlConnectionConfigs.get("host");
            this.MySQL.connection.user = (String) mysqlConnectionConfigs.get("user");
            this.MySQL.connection.password = (String) mysqlConnectionConfigs.get("password");
            this.MySQL.connection.database = (String) mysqlConnectionConfigs.get("database");

            //..............

            this.PostgreSQL.configs.primary = (Boolean) postgresql.get("primary");
            this.PostgreSQL.configs.usage = (Boolean) postgresql.get("usage");

            JSONObject postgresqlConnectionConfigs = (JSONObject) postgresql.get("connection");

            this.PostgreSQL.connection.host = (String) postgresqlConnectionConfigs.get("host");
            this.PostgreSQL.connection.user = (String) postgresqlConnectionConfigs.get("user");
            this.PostgreSQL.connection.password = (String) postgresqlConnectionConfigs.get("password");
            this.PostgreSQL.connection.database = (String) postgresqlConnectionConfigs.get("database");

            //..............

            this.SQLite.configs.primary = (Boolean) sqlite.get("primary");
            this.SQLite.configs.usage = (Boolean) sqlite.get("usage");


        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }



    public class Connection
    {
        public String host;
        public String user;
        public String password;
        public String database;
    }

    public class Configs
    {
        public Boolean usage;
        public Boolean primary;
    }

    public class MySQL
    {
        public Configs configs = new Configs();
        public Connection connection = new Connection();
    }

    public class PostgreSQL
    {
        public Configs configs = new Configs();
        public Connection connection = new Connection();
    }

    public class SQLite
    {
        public Configs configs = new Configs();
    }

    private class AllDb extends JSONObject
    {
        MySQL MySQL = new MySQL();
        PostgreSQL PostgreSQL = new PostgreSQL();
        SQLite SQLite = new SQLite();
    }

}


