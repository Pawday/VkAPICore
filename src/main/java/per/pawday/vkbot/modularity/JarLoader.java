package per.pawday.vkbot.modularity;

import java.io.*;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

class JarLoader
{

    private Map<String,ArrayList<Byte>> filesMap = new HashMap<>();

    private String fileName;

    public JarLoader(String fileName)
    {
        this.fileName = fileName;
    }

    public void load() throws IOException {
        File file = new File(this.fileName);
        ZipEntry entry;

        FileInputStream fileInputStream = new FileInputStream(file);
        JarInputStream jarInputStream = new JarInputStream(fileInputStream);

        while ((entry = jarInputStream.getNextEntry()) != null)
        {
            if (!entry.isDirectory())
            {
                {
                    ArrayList<Byte> bytes = new ArrayList<>();
                    int b;
                    while ((b = jarInputStream.read()) != -1)
                    {
                        bytes.add((byte) b);
                    }

                    filesMap.put(entry.getName(), (ArrayList<Byte>) bytes.clone());
                    bytes.clear();
                }
            }
        }


        jarInputStream.close();
        fileInputStream.close();

    }
    public void unload()
    {
        this.filesMap.clear();
    }

    public ArrayList<Byte> getByteArray(String key)
    {
        return this.filesMap.get(key);
    }

}