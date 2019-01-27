package per.pawday.vkbot.module;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import api.Module;
import per.pawday.vkbot.main.Main;


import java.io.*;
import java.net.*;

public class Loader
{

    public Loader()
    {
        JSONParser parser  = new JSONParser();

        File modulesManagerFile = new File("Modules_Manager.json");
        File modulesDir = new File("modules");

        if ( ! modulesDir.exists())
        {
            modulesDir.mkdir();
        }


        if (! modulesManagerFile.exists())
        {
            try
            {
                modulesManagerFile.createNewFile();
                Writer writer = new FileWriter(modulesManagerFile);
                writer.write("[\n\n\n]");
                writer.close();
            }
            catch (IOException e) {}
        }
    }
    public void load()
    {
        File modulesManagerFile = new File("Modules_Manager.json");
        JSONParser parser  = new JSONParser();

        JSONArray modulesArr = null;
        try
        {
            modulesArr = (JSONArray) parser.parse(new FileReader(modulesManagerFile));
        }
        catch (IOException e)
        {}
        catch (ParseException e)
        {
            System.out.println("Файл Modules_Manager.json был поврежден.");
            System.exit(-1);
        }

        for (int i = 0; i < modulesArr.size(); i++)
        {
            JSONObject module = (JSONObject) modulesArr.get(i);
            File moduleFile = new File("modules/" + module.get("name") + ".jar");
            if (!moduleFile.exists())
            {
                System.out.print("Downloading " + module.get("name") + ".jar ...");


                InputStream data = null;
                try
                {
                    HttpURLConnection download = (HttpURLConnection) new URL((String) module.get("url")).openConnection();
                    download.setRequestMethod("GET");
                    download.setDoInput(true);
                    data = download.getInputStream();

                    //witting
                    {
                        OutputStream moduleFileOS = new FileOutputStream(moduleFile);
                        int r;
                        while ((r = data.read()) != -1)
                        {
                            moduleFileOS.write(r);
                        }

                        moduleFileOS.flush();
                        moduleFileOS.close();

                        System.out.print("\b\b\b  [Success]\n");
                    }

                }
                catch (ProtocolException e)
                {} catch (IOException e)
                {
                    System.out.print("\b\b\b  [Fail]\n");
                }
            }
        }
    }

    public void run()
    {
        JSONArray modulesArr = null;



        File modulesDir = new File("modules");
        String[] files = modulesDir.list();

        for (int i = 0; i < files.length; i++)
        {
            if  ( files[i].split("\\.")[1].equals("jar") )
            {


                File module = new File("modules/" + files[i].split("\\.")[0] + ".jar");
                URI url = module.toURI();

                try
                {
                    URLClassLoader loader = new URLClassLoader(new URL[]{url.toURL()});

                    Module theModule = (Module) loader.loadClass("main.Main").newInstance();


                    if (theModule.version().split("\\.").length != 3)
                    {

                        continue;
                    } else
                    {
                        try
                        {
                            int globalUpdateV = Integer.parseInt(theModule.version().split("\\.")[0]);
                            int release = Integer.parseInt(theModule.version().split("\\.")[1]);
                            int patch = Integer.parseInt(theModule.version().split("\\.")[2]);

                            if
                            (
                                    !
                                    (
                                        Integer.parseInt(Main.coreVersion.split("\\.")[0]) >= globalUpdateV &&
                                        Integer.parseInt(Main.coreVersion.split("\\.")[1]) >= release &&
                                        Integer.parseInt(Main.coreVersion.split("\\.")[2]) >= patch
                                    )
                            )
                            {
                                continue;
                            }

                        }
                        catch (NumberFormatException e)
                        {
                            continue;
                        }

                        theModule.run();
                    }




                }
                catch (IOException | IllegalAccessException | InstantiationException e)
                {
                }
                catch (ClassNotFoundException | ClassCastException ex)
                {
                }
                catch (AbstractMethodError error)
                {
                }
            }
        }
    }
}
