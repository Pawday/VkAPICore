package per.pawday.vkbot.configs;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Token
{
    public String token;



    private File dir = new File("configs");
    private File file = new File("configs/token.json");


    private void writeToFile()
    {


        String template =
                        "{" + "\n" +
                        "\t\"token\":\"\"" + "\n" +
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

        System.out.println("Введите токен в файл configs/token.json");
        System.exit(-1);
    }


    public Token()
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
            if ( ! file.exists())
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


            try
            {
                JSONObject object = (JSONObject) parser.parse(new FileReader(file));
                this.token = (String) object.get("token");
                if (this.token.equals(""))
                {
                    System.out.println("Введите токен в файл configs/token.json");
                    System.exit(-1);
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String toString()
    {
        return token;
    }
}
