/*
 *        derpySkyblock - Derpy00001 | Derpy#5247
 *        discord.gg/bQxBB89
 *        Hi! :)
 *        I have no damn clue what im doing, I cant even read my own code
 */

package me.derpy.skyblock;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.derpy.skyblock.commands.IslandCommand;
import me.derpy.skyblock.extra.Extras;
import me.derpy.skyblock.extra.challenges.ChallengeManager;
import me.derpy.skyblock.extra.generator.Generator;
import me.derpy.skyblock.extra.scoreboard.BoardManager;
import me.derpy.skyblock.listeners.Challenges;
import me.derpy.skyblock.listeners.Inventory;
import me.derpy.skyblock.listeners.OnBreak;
import me.derpy.skyblock.listeners.Pickup;
import me.derpy.skyblock.listeners.Pvp;
import me.derpy.skyblock.listeners.Teleport;
import me.derpy.skyblock.listeners.UserRegister;
import me.derpy.skyblock.objects.Permissions;
import me.derpy.skyblock.objects.Threads;
import me.derpy.skyblock.objects.holders.GuiHolder;
import me.derpy.skyblock.objects.holders.GuiHolderUser;
import me.derpy.skyblock.objects.holders.GuiHolderUserInv;
import me.derpy.skyblock.objects.holders.GuiItems;
import me.derpy.skyblock.objects.holders.GuiSections;
import me.derpy.skyblock.objects.main.Island;
import me.derpy.skyblock.objects.main.Islander;
import me.derpy.skyblock.utils.Console;
import me.derpy.skyblock.utils.Garbage;
import me.derpy.skyblock.utils.config.Manager;

public class Skyblock extends JavaPlugin{
	public static Manager manager;
	public static Permissions permissions=new Permissions();
	@Override
	public void onEnable() {
		saveDefaultConfig();
		//Incase Server Reload
		if(Bukkit.getServer().getOnlinePlayers().size()>0) {
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				Islander.getRegistered().put(p.getUniqueId(), Islander.getUser(p));
				//Scoreboards
			}
		}
		for(World world : Bukkit.getServer().getWorlds()) {
			if(world.getName().contains("Island_")) {
				Island.getRegistered().put(UUID.fromString(world.getName().split("_")[1]), Island.getIsland(world.getName()));
			}
		}
		//Config manager
		manager=new Manager();
		//Listeners
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new UserRegister(), this);
		pm.registerEvents(new Inventory(), this);
		pm.registerEvents(new OnBreak(), this);
		pm.registerEvents(new Pvp(), this);
		pm.registerEvents(new Teleport(), this);
		pm.registerEvents(new Pickup(), this);
		pm.registerEvents(new Challenges(), this);
		if(Extras.Generator.generatorEnabled()) {
			pm.registerEvents(new Generator(), this);
		}
		//Commands
		new IslandCommand();
		//Scoreboard loop
		if(Extras.Scoreboard.boardEnabled()) {
			BoardManager.startLoop();
		}
		ChallengeManager.setup();
		new Garbage();
	}
	@Override
	public void onDisable() {
		for(World world : Bukkit.getServer().getWorlds()) {
			if(world.getName().contains("Island_")) {
				Bukkit.unloadWorld(world, true);
			}
		}
		try {
			try {
				Threads.clearThreads();
			}catch(Exception e) {
				getLogger().log(Level.SEVERE, "Failed to clear Threads");
			}
			Console.print("Clearing Registry: Islanders");
			try {
				Iterator<Entry<UUID, Islander>> iter = Islander.getRegistered().entrySet().iterator();
				while(iter.hasNext()) {
					Entry<UUID,Islander> entry = iter.next();
					Islander.getRegistered().remove(entry.getKey());
				}
			}catch(Exception e) {
				getLogger().log(Level.WARNING, "Failed to clear Islanders Registry");
			}
			Console.print("Clearing Registry: Islands");
			try {
				Iterator<Entry<UUID, Island>> iter = Island.getRegistered().entrySet().iterator();
				while(iter.hasNext()) {
					Entry<UUID,Island> entry = iter.next();
					Island.getRegistered().remove(entry.getKey());
				}
			}catch(Exception e) {
				getLogger().log(Level.WARNING, "Failed to clear Islands Registry");
			}
		}catch(Exception e) {
			getLogger().log(Level.WARNING, "Failed to properly disable plugin");
		}
		BoardManager.clearBoards();
		if(Bukkit.getServer().getOnlinePlayers().size()>0) {
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				if(p.getOpenInventory()!=null) {
					org.bukkit.inventory.Inventory inv = p.getOpenInventory().getTopInventory();
					if(inv.getHolder() instanceof GuiHolder || inv.getHolder() instanceof GuiHolderUser||inv.getHolder() instanceof GuiHolderUserInv||inv.getHolder() instanceof GuiSections || inv.getHolder() instanceof GuiItems) {
						p.getOpenInventory().close();
					}
				}
			}
		}
	}
	public static World getSpawn() {
		FileConfiguration config = Skyblock.getPlugin(Skyblock.class).getConfig();
		if(config.getString("spawn_world_name").equals("WORLDNAME")) {
			return Bukkit.getServer().getWorlds().get(0);
		}else {
			return Bukkit.getServer().getWorld(config.getString("spawn_world_name"));
		}
	}
}
