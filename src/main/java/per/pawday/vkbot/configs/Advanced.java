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


    private File dir = new File("configs");
    private File file = new File("configs/advanced.json");


    private void writeToFile()
    {
        String template =
                "{\n" +
                "\t\"heroku\":\n" +
                "\t{\n" +
                "\t\t\"usage\":false\n" +
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

            this.heroku.usage = (Boolean) heroku.get("usage");


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
        public Boolean usage;
    }
}
