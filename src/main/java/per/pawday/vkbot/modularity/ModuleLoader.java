package per.pawday.vkbot.modularity;

import java.io.IOException;
import java.util.ArrayList;

public class ModuleLoader extends ClassLoader
{
    String moduleName;
    public ModuleLoader(String moduleName)
    {
        this.moduleName = moduleName;
    }
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
        JarLoader loader = new JarLoader(moduleName);
        try
        {
            loader.load();
        }
        catch (IOException e)
        {
            throw new ClassNotFoundException();
        }

        ArrayList<Byte> barr = loader.getByteArray(name);
        byte[] reter = new byte[barr.size()];

        for (int i = 0; i < reter.length; i++)
        {
            reter[i] = barr.get(i);
        }

        return defineClass(name,reter,0,barr.size());
    }
}
