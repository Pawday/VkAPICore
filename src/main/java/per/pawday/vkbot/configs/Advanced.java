package per.pawday.vkbot.configs;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Advanced
{
    public Heroku heroku = new Heroku();
    public Database database = new Database();


    private File dir = new File("configs");
    private File file = new File("configs/advanced.json");


    private void writeToFile()
    {
        String template =
                "{\n" +
                "\t\"heroku\":\n" +
                "\t{\n" +
                "\t\t\"usage\":false,\n" +
                "\t\t\"database\":\n" +
                "\t\t{\n" +
                "\t\t\t\"usage\":false\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"database\":\n" +
                "\t{\n" +
                "\t\t\"mysql\":\n" +
                "\t\t{\n" +
                "\t\t\t\"port\":\"3306\" \n" +
                "\t\t},\n" +
                "\t\t\"postgresql\":\n" +
                "\t\t{\n" +
                "\t\t\t\"port\":\"5432\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(template);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Advanced()
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

            JSONObject heroku = (JSONObject) main.get("heroku");
            this.heroku.usage = (boolean) heroku.get("usage");
            JSONObject herokuDatabase = (JSONObject) heroku.get("database");

            JSONObject database = (JSONObject) main.get("database");
            JSONObject databaseMysql = (JSONObject) database.get("mysql");
            JSONObject databasePostgresql = (JSONObject) database.get("postgresql");

            this.heroku.database.usage = (Boolean) herokuDatabase.get("usage");

            this.database.mysql.port = (String) databaseMysql.get("port");
            this.database.postgresql.port = (String) databasePostgresql.get("port");




        }
        catch (ParseException e)
        {
            System.out.println("Файл configs/advanced.json был поврежден");
            System.exit(-1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public class Heroku
    {
        public boolean usage;
        public Database database = new Database();

        public class Database
        {
            public Boolean usage;
        }
    }

    public class Database
    {
        public MySQL mysql = new MySQL();
        public PostgreSQL postgresql = new PostgreSQL();


        public class MySQL
        {
            public String port;
        }

        public class PostgreSQL
        {
            public String port;
        }
    }
}
