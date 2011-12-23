package org.pokepal101.log.pokepal101.log;

import java.util.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.*;
import org.jnbt.*;

public class CPlayerListener extends PlayerListener
{
    private final LogPlugin plugin;

    public CPlayerListener (LogPlugin instance)
    {
        plugin = instance;
    }

    public void onPlayerInteract (PlayerInteractEvent event)
    {
        Block b = null;
        if (event.getAction ().equals (Action.RIGHT_CLICK_BLOCK))
        {
            if (event.getMaterial ().equals (plugin.stickMat) && event.getPlayer ().hasPermission ("log.stick"))
            {
                b = event.getClickedBlock ();
            }
            else if (event.getMaterial ().equals (plugin.boneMat) && event.getPlayer ().hasPermission ("log.bone"))
            {
                Block bb = event.getClickedBlock ();
                BlockFace bf = event.getBlockFace ();
                b = bb.getRelative (bf);
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
            event.getPlayer ().sendMessage ("Block changes:");
            ArrayList<Modification> mod = new ArrayList<Modification> ();
            try
            {
                plugin.bw.flush ();
                plugin.resetReader ();
                /*
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
                 */

                CompoundTag main = (CompoundTag) plugin.rdr.readTag ();
                Object[] contents = main.getValue ().values ().toArray ();
                for (int i = 0; i < contents.length; i++)
                {
                    Modification m = Modification.fromNBT ((Tag) contents[i]);
                    if (m.x == b.getX () && m.y == b.getY () && m.z == b.getZ ())
                    {
                        mod.add (m);
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println ("Log: Error getting modification data: " + e);
                e.printStackTrace ();
            }
            if (mod.isEmpty ())
            {
                event.getPlayer ().sendMessage ("- No hits.");
            }
            else
            {
                for (int i = 0; i < mod.size (); i++)
                {
                    Modification m = mod.get (i);
                    String s = "Block [";
                    s += m.blockid;
                    s += "] ";
                    if (m.operation == 0)
                    {
                        s += "placed";
                    }
                    else if (m.operation == 1)
                    {
                        s += "destroyed";
                    }
                    else if (m.operation == 2)
                    {
                        s += "placed (bucket)";
                    }
                    else if (m.operation == 3)
                    {
                        s += "destroyed (bucket)";
                    }
                    s += " by " + m.who;
                    s += " at ";
                    s += m.hour + ":" + m.min + "." + m.sec + " ";
                    s += m.day + "/" + m.month + "/" + m.year;
                    s += ".";
                    event.getPlayer ().sendMessage ("- " + s);
                }
            }
            event.setCancelled (true);
        }
    }

    public void onPlayerBucketEmpty (PlayerBucketEmptyEvent event)
    {
        Material b = event.getBucket ();
        Block bl = event.getBlockClicked ().getRelative (event.getBlockFace ());
        String block = "?";
        if (b.getId () == Material.WATER_BUCKET.getId ())
        {
            block = "8";
        }
        else if (b.getId () == Material.LAVA_BUCKET.getId ())
        {
            block = "10";
        }
        Calendar c = Calendar.getInstance ();
        String idData = block + "";
        Modification m = new Modification (event.getPlayer ().getName (), 2, idData,
                c.get (Calendar.DAY_OF_MONTH), c.get (Calendar.MONTH), c.get (Calendar.YEAR),
                c.get (Calendar.HOUR_OF_DAY), c.get (Calendar.MINUTE), c.get (Calendar.SECOND),
                bl.getX (), bl.getY (), bl.getZ ());
        plugin.bw.write (m);
        
        event.setCancelled (false);
    }

    public void onPlayerBucketFill (PlayerBucketFillEvent event)
    {
        ItemStack b = event.getItemStack ();
        Block bl = event.getBlockClicked ().getRelative (event.getBlockFace ());
        String block = "?";
        if (b.getTypeId () == Material.WATER_BUCKET.getId ())
        {
            block = "8";
        }
        else if (b.getTypeId () == Material.LAVA_BUCKET.getId ())
        {
            block = "10";
        }
        Calendar c = Calendar.getInstance ();
        String idData = block + "";
        Modification m = new Modification (event.getPlayer ().getName (), 3, idData,
                c.get (Calendar.DAY_OF_MONTH), c.get (Calendar.MONTH), c.get (Calendar.YEAR),
                c.get (Calendar.HOUR_OF_DAY), c.get (Calendar.MINUTE), c.get (Calendar.SECOND),
                bl.getX (), bl.getY (), bl.getZ ());
        plugin.bw.write (m);
        
        event.setCancelled (false);
    }
}
