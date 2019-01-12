package per.pawday.vkbot.configs;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Main
{

    public Exterior exterior = new Exterior();



    private File dir = new File("configs");
    private File file = new File("configs/main.json");

    private void writeToFile()
    {
        String template =
                "{\n" +
                "\t\"exterior\":\n" +
                "\t{\n" +
                "\t\t\"titleName\":\"\",\n" +
                "\t\t\"invocations\":[\"\"]\n" +
                "\t}\n" +
                "}";

        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(template);
            writer.close();
        }
        catch (FileNotFoundException e)
        {}
        catch (IOException e)
        {}

        System.out.println("Введите данные в файл configs/main.json");
        System.exit(-1);
    }

    public Main()
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
            JSONObject main = (JSONObject) parser.parse(new FileReader(file) );

            JSONObject exterior = (JSONObject) main.get("exterior");

            this.exterior.titleName = (String) exterior.get("titleName");
            JSONArray invocationsArr = (JSONArray) exterior.get("invocations");
            this.exterior.invocations = (Object[]) invocationsArr.toArray();











        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            System.out.println("Файл configs/main.json был поврежден");
            System.exit(-1);
        }


    }





    public class Exterior
    {
        public String titleName;
        public Object[] invocations;

        public boolean hasInvocations(String benchmark)
        {
            boolean ret = false;


            for (int i = 0; i < this.invocations.length; i++)
            {
                if (this.invocations[i].equals(benchmark))
                {
                    ret = true;
                    break;
                }
            }

            return ret;
        }
    }
}
