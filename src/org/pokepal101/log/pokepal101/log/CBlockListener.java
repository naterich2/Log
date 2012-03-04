package org.pokepal101.log.pokepal101.log;

import java.util.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

/**
 * Sample block listener
 * @author Dinnerbone
 */
public class CBlockListener implements Listener
{
    private final LogPlugin plugin;

    public CBlockListener(final LogPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockPlace (BlockPlaceEvent event)
    {
        Block b = event.getBlockPlaced();
        Position p = new Position(b.getX(), b.getY(), b.getZ());
//        ArrayList<Modification> mod = plugin.get(p);
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.DAY_OF_MONTH) + "/";
        date += (c.get(Calendar.MONTH) + 1) + "/";
        date += c.get(Calendar.YEAR) + " ";
        date += c.get(Calendar.HOUR_OF_DAY) + ":";
        date += c.get(Calendar.MINUTE);
        String idData = b.getTypeId() + "";
        if (b.getData() != 0) idData += ":" + b.getData();
        Modification m = new Modification(event.getPlayer().getName(), true, idData, date);
//        mod.add(m);
//        plugin.set(p, mod);
		plugin.bw.write(p.getData() + ";" + m.getData());
    }
    
    @EventHandler
    public void onBlockBreak (BlockBreakEvent event)
    {
        Block b = event.getBlock();
        Position p = new Position(b.getX(), b.getY(), b.getZ());
//        ArrayList<Modification> mod = plugin.get(p);
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.DAY_OF_MONTH) + "/";
        date += (c.get(Calendar.MONTH) + 1) + "/";
        date += c.get(Calendar.YEAR) + " ";
        date += c.get(Calendar.HOUR_OF_DAY) + ":";
        date += c.get(Calendar.MINUTE);
        String idData = b.getTypeId() + "";
        if (b.getData() != 0) idData += ":" + b.getData();
        Modification m = new Modification(event.getPlayer().getName(), false, idData, date);
//        mod.add(m);
//        plugin.set(p, mod);
		plugin.bw.write(p.getData() + ";" + m.getData());
    }
}
