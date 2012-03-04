package org.pokepal101.log.pokepal101.log;

import java.io.*;
import java.util.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.command.*;
import org.bukkit.material.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

//import com.nijiko.permissions.PermissionHandler;
//import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * Sample plugin for Bukkit
 *
 * @author Dinnerbone
 */
public class LogPlugin extends JavaPlugin {
    private final CPlayerListener playerListener = new CPlayerListener(this);
    private final CBlockListener blockListener = new CBlockListener(this);
//    private HashMap<Position, ArrayList<Modification>> mod = new HashMap<Position, ArrayList<Modification>>();
//    private HashMap<Integer, String> blocks = new HashMap<Integer, String>();

    protected BufferedDataFileWriter bw;
    protected BufferedReader rdr;
    protected Material stickMat = Material.STICK;
    protected Material boneMat = Material.BONE;
    private Properties props = new Properties();

//    public static PermissionHandler Permissions;

    public void onDisable()
    {
        System.out.println("Log version " + getDescription().getVersion() + ": Shutting down.");
        try
        {
/*
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("logdata.db"));
            output.writeObject(mod);
            output.close();
            System.out.println("Log version " + getDescription().getVersion() + ": Data written successfully.");
*/
			bw.close();
			rdr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Log version " + getDescription().getVersion() + ": Unloaded.");
    }

    public void onEnable()
    {
        System.out.println("Log version " + getDescription().getVersion() + ": Loaded.");
        PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(blockListener, this);
		pm.registerEvents(playerListener, this);
        System.out.println("Log version " + getDescription().getVersion() + ": Events initialised successfully.");

        try
        {
            if (new File("logdata.db").exists() && !new File("logdata.db2").exists())
            {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream("logdata.db"));
		BufferedDataFileWriter dw = new BufferedDataFileWriter(new File("logdata.db2"));
                @SuppressWarnings("unchecked")
				HashMap<Position, ArrayList<Modification>> mod = (HashMap<Position, ArrayList<Modification>>) input.readObject();
                input.close();
		Iterator<Position> keySet = mod.keySet().iterator();
		while (keySet.hasNext())
		{
			Position pos = keySet.next();
			ArrayList<Modification> mods = mod.get(pos);
			for (int j = 0; j < mods.size(); j++)
			{
				dw.write(pos.getData() + ";" + mods.get(j).getData());
			}
		}
		dw.close();
                System.out.println("Log version " + getDescription().getVersion() + ": Data converted successfully.");
            }
            if (!new File("logdata.db2").exists())
            {
		new File("logdata.db2").createNewFile();
            }
		bw = new BufferedDataFileWriter(new File("logdata.db2"));
		rdr = new BufferedReader(new FileReader(new File("logdata.db2")));
                System.out.println("Log version " + getDescription().getVersion() + ": Data loaded successfully.");

            if (!new File("plugins/Log/config.properties").exists())
            {
                if (!new File("plugins/Log/").exists()) { new File("plugins/Log/").mkdirs(); }
                new File("plugins/Log/config.properties").createNewFile();
                props.put("stickItem", "280");
                props.put("boneItem", "352");
                FileOutputStream fos = new FileOutputStream("plugins/Log/config.properties");
                props.store(fos, "");
                fos.close();
                System.out.println("Log version " + getDescription().getVersion() + ": Created new properties file.");
            }
            else
            {
                FileInputStream is = new FileInputStream("plugins/Log/config.properties");
                props.load(is);
                is.close();
                System.out.println("Log version " + getDescription().getVersion() + ": Loaded properties file.");
            }
            stickMat = new MaterialData(Integer.parseInt(props.getProperty("stickItem", "280"))).getItemType();
            boneMat = new MaterialData(Integer.parseInt(props.getProperty("boneItem", "352"))).getItemType();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        setupPermissions();
    }

	protected void resetReader ()
	{
		try
		{
			rdr = new BufferedReader(new FileReader(new File("logdata.db2")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

    private void setupPermissions() {
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) 
    {
        if (command.getName().equalsIgnoreCase("log"))
        {
            if (args.length == 0)
            {
                sender.sendMessage("Log Help:");
                sender.sendMessage("/log purge : Erases the log database");

                sender.sendMessage("/log clean <time> : Erases entries older than <time>");
                sender.sendMessage("     <time> should end in 'm', 'h', or 'd'");
                sender.sendMessage("     <time> cannot contain a combination of time units (ie: '6h 13m' is not valid)");

            }
            else if (args[0].equalsIgnoreCase("purge") && hasPerms(sender, "log.admin.purge"))
            {
                try
                {
                    rdr.close();
/*
                    FileOutputStream erasor = new FileOutputStream("logdata.db2");
                    erasor.write(new String().getBytes());
                    erasor.close();
*/
                    bw.erase();
                    resetReader();
                    sender.sendMessage("Log database purged");
                }
                catch (Exception e)
                {
                    sender.sendMessage("Error while trying to purge database. See console.");
                    e.printStackTrace();
                }
            }

            else if (args[0].equalsIgnoreCase("clean") && hasPerms(sender, "log.admin.clean"))
            {
                if (args.length >= 2)
                {
try
{
                    long seconds = 0;
//                    if (args[1].endsWith("s"))
//                        seconds = Long.parseLong(args[1].substring(0, args[1].length() - 1));
                    if (args[1].endsWith("m"))
                        seconds = Long.parseLong(args[1].substring(0, args[1].length() - 1)) * 60;
                    else if (args[1].endsWith("h"))
                        seconds = Long.parseLong(args[1].substring(0, args[1].length() - 1)) * 60 * 60;
                    else if (args[1].endsWith("d"))
                        seconds = Long.parseLong(args[1].substring(0, args[1].length() - 1)) * 60 * 60 * 24;
                    else
                    {
                        sender.sendMessage("Invalid unit of time.");
                        return true;
                    }

                    bw.flush();
                    resetReader();
                    ArrayList<String> towrite = new ArrayList<String>();
                    String s = null;
                    while ((s = rdr.readLine()) != null)
                    {
                        if (s.length() > 0)
                        {
                            StringTokenizer token = new StringTokenizer(s, ";");
                            String position = token.nextToken() + ";" + token.nextToken() + ";" + token.nextToken();
                            String whodata = token.nextToken();
                            boolean placeddata = Boolean.parseBoolean(token.nextToken());
                            String blockiddata = token.nextToken();
                            String datedata = token.nextToken();

                            StringTokenizer tok2 = new StringTokenizer(datedata, " ");
                            String date01 = tok2.nextToken();
                            String date02 = tok2.nextToken();

                            StringTokenizer tok3 = new StringTokenizer(date01, "/");
                            int day = Integer.parseInt(tok3.nextToken());
                            int month = Integer.parseInt(tok3.nextToken());
                            int year = Integer.parseInt(tok3.nextToken());

                            StringTokenizer tok4 = new StringTokenizer(date02, ":");
                            int hour = Integer.parseInt(tok4.nextToken());
                            int min = Integer.parseInt(tok4.nextToken());

                            @SuppressWarnings("deprecation")
							Date thatdate = new Date(year - 1900, month - 1, day, hour, min);
                            Date nowdate = new Date();

                            long s1 = ((nowdate.getTime() - thatdate.getTime()) / 1000);
//REMOVE
//                            System.out.println(thatdate);
//                            System.out.println(nowdate);
                            if (s1 < seconds)
                            {
                                towrite.add(position + ";" + new Modification(whodata, placeddata, blockiddata, datedata).getData());
                            }
                        }
                    }

                    rdr.close();
                    bw.erase();
                    for (int i = 0; i < towrite.size(); i++)
                    {
                        bw.write(towrite.get(i));
                    }
}
catch (Exception e)
{
    sender.sendMessage("Error while trying to clean database. See console.");
    e.printStackTrace();
}
                }
            }

            return true;
        }
        return false;
    }

    public boolean hasPerms(CommandSender sender, String perm)
    {
        return ((sender instanceof Player && sender.hasPermission(perm))
                 || sender instanceof ConsoleCommandSender);
    }
}
