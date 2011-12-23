package org.pokepal101.log.pokepal101.log;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.jnbt.*;

public class Modification implements java.io.Serializable
{
    static final long serialVersionUID = 6797996890572426758L;
    public String who;
    public int operation;
    public String blockid;
    public int day, month, year, hour, min, sec;
    public int x, y, z;
    private static Random rnd = new Random ();

    public Modification (String who, int operation, String blockid,
            int day, int month, int year, int hour, int min, int sec,
            int x, int y, int z)
    {
        this.who = who;
        this.operation = operation;
        this.blockid = blockid;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Modification fromNBT (Tag m)
    {
        if (m instanceof CompoundTag)
        {
            Map<String, Tag> mod = ((CompoundTag) m).getValue ();
            String who = (String) mod.get ("who").getValue ();
            int operation = (Integer) mod.get ("op").getValue ();
            String blockid = (String) mod.get ("id").getValue ();
            CompoundTag date = (CompoundTag) mod.get ("time");
            int day = (Integer) date.getValue ().get ("day").getValue ();
            int month = (Integer) date.getValue ().get ("month").getValue ();
            int year = (Integer) date.getValue ().get ("year").getValue ();

            int hour = (Integer) date.getValue ().get ("hour").getValue ();
            int min = (Integer) date.getValue ().get ("min").getValue ();
            int sec = (Integer) date.getValue ().get ("sec").getValue ();

            CompoundTag pos = (CompoundTag) mod.get ("pos");
            int x = (Integer) pos.getValue ().get ("x").getValue ();
            int y = (Integer) pos.getValue ().get ("y").getValue ();
            int z = (Integer) pos.getValue ().get ("z").getValue ();

            return new Modification (who, operation, blockid,
                    day, month, year, hour, min, sec,
                    x, y, z);
        }
        return null;
    }

    public CompoundTag toNBT ()
    {
        IntTag z = new IntTag ("z", this.z);
        IntTag y = new IntTag ("y", this.y);
        IntTag x = new IntTag ("x", this.x);
        HashMap<String, Tag> posM = new HashMap<String, Tag> ();
        posM.put ("z", z);
        posM.put ("y", y);
        posM.put ("x", x);
        CompoundTag pos = new CompoundTag ("pos", posM);

        IntTag sec = new IntTag ("sec", this.sec);
        IntTag min = new IntTag ("min", this.min);
        IntTag hour = new IntTag ("hour", this.hour);

        IntTag year = new IntTag ("year", this.year);
        IntTag month = new IntTag ("month", this.month);
        IntTag day = new IntTag ("day", this.day);
        HashMap<String, Tag> dateM = new HashMap<String, Tag> ();
        dateM.put ("sec", sec);
        dateM.put ("min", min);
        dateM.put ("hour", hour);
        dateM.put ("year", year);
        dateM.put ("month", month);
        dateM.put ("day", day);
        CompoundTag date = new CompoundTag ("time", dateM);

        StringTag blockid = new StringTag ("id", this.blockid);
        IntTag operation = new IntTag ("op", this.operation);
        StringTag who = new StringTag ("who", this.who);
        HashMap<String, Tag> modM = new HashMap<String, Tag> ();
        modM.put ("pos", pos);
        modM.put ("time", date);
        modM.put ("id", blockid);
        modM.put ("op", operation);
        modM.put ("who", who);

        return new CompoundTag ("mod" + rnd.nextInt (10000), modM);
    }
}