package org.pokepal101.log.pokepal101.log;

import java.util.*;
import java.text.*;
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
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy HH:mm")
        String date =  ft.format(d);
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
        Date d = new Date();
        SimpleDateFormat ft = new SimplDateFormat("dd MMMMM yyyy HH:mm")
        String date = ft.format(d);
        String idData = b.getTypeId() + "";
        if (b.getData() != 0) idData += ":" + b.getData();
        Modification m = new Modification(event.getPlayer().getName(), false, idData, date);
//        mod.add(m);
//        plugin.set(p, mod);
		plugin.bw.write(p.getData() + ";" + m.getData());
    }
}
