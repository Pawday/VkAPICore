package per.pawday.vkbot.modularity;

import per.pawday.vkbot.api.MainModule;

import java.util.*;

public class ModuleStorage implements Map<String, MainModule>
{


    private static HashSet<String> keys = new HashSet<>();
    private static HashSet<MainModule> values = new HashSet<>();


    @Override
    public int size()
    {
        return keys.size();
    }

    @Override
    public boolean isEmpty()
    {
        return keys.size() == 0;
    }

    @Override
    public boolean containsKey(Object key)
    {
        boolean reter = false;
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext())
        {

            if (iterator.next().equals(key))
            {
                reter = true;
                break;
            }
        }

        return reter;
    }

    @Override
    public boolean containsValue(Object value)
    {
        return false;
    }

    @Override
    public MainModule get(Object key)
    {
        MainModule returnable = null;
        Iterator<String> iterator = keys.iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {

            if (iterator.next().equals(key))
            {
                Iterator<MainModule> mainModuleIterator = values.iterator();
                for (int i2 = 0; i2 < i; i2++)
                {
                    mainModuleIterator.next();
                }
                returnable = mainModuleIterator.next();
                break;
            }
        }

        return returnable;
    }

    @Override
    public MainModule put(String key, MainModule value)
    {
        MainModule returnable = null;
        if (! this.containsKey(key))
        {
            keys.add(key);
            values.add(value);
            returnable = value;
        }

        return returnable;

    }

    @Override
    public MainModule remove(Object key)
    {
        MainModule retremover = null;
        Iterator<String> iterator = keys.iterator();
        Iterator<MainModule> moduleIterator = values.iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {
            if (iterator.next().equals(key))
            {
                keys.remove(key);
                for (int i2 = 0; i2 < i; i2++)
                {
                    moduleIterator.next();
                }
                retremover = moduleIterator.next();
                values.remove(retremover);
                break;
            }
        }
        return retremover;
    }

    //not necessary
    @Override
    public void putAll(Map<? extends String, ? extends MainModule> m) {}

    @Override
    public void clear()
    {
        keys.clear();
        values.clear();
    }

    @Override
    public Set<String> keySet()
    {
        return keys;
    }


    //not necessary
    @Override
    public Collection<MainModule> values() { return null; }

    //what is this?
    @Override
    public Set<Entry<String, MainModule>> entrySet() {return null;}
}
