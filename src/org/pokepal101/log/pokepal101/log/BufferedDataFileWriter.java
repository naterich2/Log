package org.pokepal101.log.pokepal101.log;

import java.io.*;
import java.util.HashMap;
import org.jnbt.*;

public class BufferedDataFileWriter
{
    private File dataFile;
    private Modification[] cache;
    private int index;
    private final int cacheSize = 15;
    private LogPlugin plugin;

    public BufferedDataFileWriter (File dF, LogPlugin p)
    {
        dataFile = dF;
        cache = new Modification[cacheSize];
        index = 0;
        plugin = p;
    }

    public void write (Modification mod)
    {
        if (index >= cache.length)
        {
            flush ();
        }
        cache[index] = mod;
        index++;
    }

    public void flush ()
    {
        if (index > 0)
        {
            try
            {
                plugin.resetReader ();
                CompoundTag mods = (CompoundTag) plugin.rdr.readTag ();
                HashMap<String, Tag> values = new HashMap<String, Tag> (mods.getValue ());
                plugin.rdr.close ();
                for (int i = 0; ((i < index) && (i < cache.length)); i++)
                {
                    Tag nbt = cache[i].toNBT ();
                    values.put (nbt.getName (), nbt);
                }
                FileOutputStream ps = new FileOutputStream (dataFile);
                NBTOutputStream out = new NBTOutputStream(ps);
                CompoundTag modsNew = new CompoundTag(mods.getName (), values);
                out.writeTag (modsNew);
                out.close();
                cache = new Modification[cacheSize];
                index = 0;
                plugin.resetReader ();
            }
            catch (IOException e)
            {
                //throw new RuntimeException ("Log: Error flushing buffer: " + e);
                e.printStackTrace();
            }
        }
    }

    public void erase ()
    {
        flush ();
        try
        {
            PrintWriter pw = new PrintWriter (new FileWriter (dataFile, false));
            pw.close ();
        }
        catch (IOException e)
        {
            throw new RuntimeException ("Log: Error erasing data: " + e);
        }
    }

    public void close ()
    {
        flush ();
    }
}