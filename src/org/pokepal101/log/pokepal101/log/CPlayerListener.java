package org.pokepal101.log.pokepal101.log;

import java.util.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CPlayerListener implements Listener
{
    private final LogPlugin plugin;

    public CPlayerListener(LogPlugin instance)
    {
        plugin = instance;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Block b = null;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if (event.getMaterial().equals(plugin.stickMat) && event.getPlayer().hasPermission("log.stick"))
            {
                b = event.getClickedBlock();
            }
            else if (event.getMaterial().equals(plugin.boneMat) && event.getPlayer().hasPermission("log.bone"))
            {
                Block bb = event.getClickedBlock();
                BlockFace bf = event.getBlockFace();
                b = bb.getRelative(bf);
            }
        }
/*
        else if (event.getAction().equals(Action.RIGHT_CLICK_AIR))
        {
            if (event.getMaterial().equals(plugin.boneMat) && event.getPlayer().hasPermission("log.bone"))
            {
                b = event.getClickedBlock();
            }
        }
*/
        if (b != null)
        {
            event.getPlayer().sendMessage("Block changes:");
            Position p = new Position(b.getX(), b.getY(), b.getZ());
            ArrayList<Modification> mod = new ArrayList<Modification>();
            try
            {
                plugin.bw.flush();
                plugin.resetReader();
                String s = null;
                while ((s = plugin.rdr.readLine()) != null)
                {
                    if (s.length() > 0)
                    {
                        StringTokenizer token = new StringTokenizer(s, ";");
                        int xdata = Integer.parseInt(token.nextToken());
                        int ydata = Integer.parseInt(token.nextToken());
                        int zdata = Integer.parseInt(token.nextToken());
                        Position pdata = new Position(xdata, ydata, zdata);
                        if (pdata.toString().equalsIgnoreCase(p.toString()))
                        {
                            String whodata = token.nextToken();
                            boolean placeddata = Boolean.parseBoolean(token.nextToken());
                            String blockiddata = token.nextToken();
                            String datedata = token.nextToken();
                            Modification mdata = new Modification(whodata, placeddata, blockiddata, datedata);
                            mod.add(mdata);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException ("Log: Error getting modification data: " + e);
            }
            if (mod.size() == 0)
                event.getPlayer().sendMessage("- No hits.");
            else
            {
                for (int i = 0; i < mod.size(); i++)
                {
                    Modification m = mod.get(i);
                    String s = "Block [";
                    s += m.getBlockID();
                    s += "] ";
                    if (m.getPlaced()) s += "placed by ";
                    else s += "destroyed by ";
                    s += m.getWho();
                    s += " at ";
                    s += m.getDate();
                    s += ".";
                    event.getPlayer().sendMessage("- " + s);
                }
            }
            event.setCancelled(true);
        }
    }
}
