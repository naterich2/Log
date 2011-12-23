package org.pokepal101.log.pokepal101.log;

import java.util.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.*;

/**
 * Sample block listener
 * @author Dinnerbone
 */
public class CBlockListener extends BlockListener
{
    private final LogPlugin plugin;

    public CBlockListener(final LogPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void onBlockPlace (BlockPlaceEvent event)
    {
        Block b = event.getBlockPlaced();
        Calendar c = Calendar.getInstance();
        String idData = b.getTypeId() + "";
        if (b.getData() != 0) idData += ":" + b.getData();
        Modification m = new Modification(event.getPlayer().getName(), 0, idData,
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
                b.getX(), b.getY(), b.getZ());
	plugin.bw.write(m);
    }

    public void onBlockBreak (BlockBreakEvent event)
    {
        Block b = event.getBlock();
        Calendar c = Calendar.getInstance();
        String idData = b.getTypeId() + "";
        if (b.getData() != 0) idData += ":" + b.getData();
        Modification m = new Modification(event.getPlayer().getName(), 1, idData,
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
                b.getX(), b.getY(), b.getZ());
	plugin.bw.write(m);
    }
}
