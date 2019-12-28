package me.derpy.skyblock.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.derpy.skyblock.Skyblock;
import me.derpy.skyblock.creators.IslandCreator;
import me.derpy.skyblock.enums.InvitationResult;
import me.derpy.skyblock.enums.InvitationType;
import me.derpy.skyblock.exceptions.IslandException;
import me.derpy.skyblock.exceptions.IslandInvitationException;
import me.derpy.skyblock.exceptions.UserAlreadyHasInvite;
import me.derpy.skyblock.exceptions.UserHasIslandException;
import me.derpy.skyblock.exceptions.UserHasNoIslandException;
import me.derpy.skyblock.exceptions.UserInvitationListException;
import me.derpy.skyblock.extra.Extras;
import me.derpy.skyblock.extra.economy.Economy;
import me.derpy.skyblock.objects.Permissions;
import me.derpy.skyblock.objects.data.InviteData;
import me.derpy.skyblock.objects.main.Island;
import me.derpy.skyblock.objects.main.Islander;
import me.derpy.skyblock.utils.Inventories.Inventories;
import net.md_5.bungee.api.ChatColor;

public class IslandCommand implements CommandExecutor, TabCompleter{
	public IslandCommand() {
		PluginCommand cmd = Skyblock.getPlugin(Skyblock.class).getCommand("island");
		cmd.setExecutor(this);
		cmd.setAliases(Arrays.asList("is"));
		cmd.setDescription("Opens the island gui");
		cmd.setName("island");
		cmd.setLabel("island");
		cmd.setUsage("/island");
		cmd.setPermissionMessage(ChatColor.RED+"You do not have the required permissions for this command!");
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		if(arg0 instanceof Player) {
			Islander islander = Islander.getUser((Player)arg0);
			if(arg3.length==0||arg3[0].toLowerCase().equals("gui")) {
				if(islander.hasIsland()) {
					islander.getOfflinePlayer().getPlayer().openInventory(Inventories.getMain(islander));
				}else {
					islander.getOfflinePlayer().getPlayer().openInventory(Inventories.getNoIsland(islander));
				}
			}else {
				if(arg3[0].toLowerCase().equals("setwelcome")) {
					if(islander.hasIsland()) {
						try {
							if(islander.isIslandOwner()) {
								if(Skyblock.getPlugin(Skyblock.class).getConfig().getBoolean("island_settings.custom_island_welcome.enabled")) {
									if(Skyblock.getPlugin(Skyblock.class).getConfig().getBoolean("island_settings.custom_island_welcome.require_perm")) {
										if(islander.getOfflinePlayer().getPlayer().hasPermission(Permissions.setWelcomeMessage)) {
											parseWelcome(islander, arg3, arg2);
										}else {
											islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"You do not have the required permissions for this command!");
										}
									}else {
										parseWelcome(islander, arg3, arg2);
									}
								}else {
									islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"Custom island messages have been disabled!");
								}
								
							}else {
								islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"You are not the owner of your island!");
							}
						} catch (UserHasNoIslandException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"You do not have an island!");
					}
				}else if(arg3[0].toLowerCase().equals("invite")) {
					if(arg3.length==2) {
						Player p = Bukkit.getServer().getPlayer(arg3[1]);
						if(p==null) {
							islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"Could not find player with the name "+arg3[1]);
						}else {
							if(islander.hasIsland()) {
								try {
									Islander.getUser(p).sendInvite(islander.getIsland());
								} catch (UserHasIslandException e) {
									// TODO Auto-generated catch block
									islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"This player already has an island!");
									return true;
								} catch (UserInvitationListException e) {
									// TODO Auto-generated catch block
									islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"This players invitation list is full!");
									return true;
								} catch (IslandException e) {
									// TODO Auto-generated catch block
									islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"Your island cannot hold anymore members!");
									return true;
								} catch (IslandInvitationException e) {
									// TODO Auto-generated catch block
									islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"Your island cannot send anymore invitations!");
									return true;
								} catch (UserHasNoIslandException e) {
									// TODO Auto-generated catch block
									islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"You do not have an island to invite this player to!");
									return true;
								} catch (UserAlreadyHasInvite e) {
									// TODO Auto-generated catch block
									islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"This player already has an invite to your island!");
									return true;
								}
							}else {
								islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"You do not have an island to invite this player to!");
								return true;
							}
						}
					}else {
						islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"/island invite {Player}");
					}
				}else if(arg3[0].toLowerCase().equals("home")) {
					if(islander.hasIsland()) {
						try {
							islander.getOfflinePlayer().getPlayer().teleport(islander.getIsland().getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
						} catch (UserHasNoIslandException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else if(arg3[0].toLowerCase().equals("balance")) {
					islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.YELLOW+"Balance: "+Extras.Economy.getSymbol()+Double.toString(Economy.getBalance(islander.getUniqueId())));
				}else if(arg3[0].toLowerCase().equals("level")) {
					if(islander.hasIsland()) {
						try {
							islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.YELLOW+"Island Level: "+Integer.toString(islander.getIsland().getLevel()));
						} catch (UserHasNoIslandException e) {
							// TODO Auto-generated catch block
							islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"You do not have an island!");
						}
					}else {
						islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED+"You do not have an island!");
					}
				}else if(arg3[0].toLowerCase().equals("showcase")) {
					if(arg3[1].toLowerCase().equals("fakeinvite")) {
						try {
							islander.sendInvite(Island.getIsland("Island_a4dcf21d-687e-4e25-9199-d6be70025c45"));
						} catch (UserHasIslandException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UserInvitationListException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IslandException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IslandInvitationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UserAlreadyHasInvite e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(arg3[1].toLowerCase().equals("removefakeisland")) {
						IslandCreator.deleteIsland(UUID.fromString("a4dcf21d-687e-4e25-9199-d6be70025c45"));
					}
				}else if(arg3[1].toLowerCase().equals("sendfake")) {
					try {
						Islander.getUser(UUID.fromString("07bf278e-21aa-4898-bd5b-83a2c219a1ab")).sendInvite(islander.getIsland());
					} catch (UserHasIslandException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UserInvitationListException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IslandException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IslandInvitationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UserHasNoIslandException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UserAlreadyHasInvite e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(arg3[1].toLowerCase().equals("acceptfake")) {
					try {
						Islander.getUser(UUID.fromString("07bf278e-21aa-4898-bd5b-83a2c219a1ab")).handleInvite(islander.getIsland(), InvitationResult.FORCE, new InviteData(UUID.fromString("a4dcf21d-687e-4e25-9199-d6be70025c45"), islander.getIsland().getId(), InvitationType.ISLAND));
					} catch (UserHasIslandException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IslandInvitationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UserHasNoIslandException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(arg3[1].toLowerCase().equals("createfake")) {
					IslandCreator.createIsland(Islander.getUser(UUID.fromString("a4dcf21d-687e-4e25-9199-d6be70025c45")), 2);
				}
			}
		}else {
			arg0.sendMessage(ChatColor.RED+"Command must be executed by a player!");
		}
		return true;
	}
	private static void parseWelcome(Islander islander,String[] arg3, String arg2) {
		String newwelcome = "";
		for(String string:arg3) {
			if(!string.toLowerCase().equals("setwelcome")) {
				if(!string.toLowerCase().equals(arg2)) {
					newwelcome=newwelcome.concat(string.concat(" "));
				}
			}
		}
		try {
			islander.getIsland().setWelcome(newwelcome);
		} catch (UserHasNoIslandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		islander.getOfflinePlayer().getPlayer().sendMessage(ChatColor.GREEN+"Set island message to: \""+newwelcome+"\"");
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		if(arg3.length==1) {
			ArrayList<String> available = new ArrayList<String>();
			available.add("gui");
			available.add("setwelcome");
			available.add("invite");
			available.add("home");
			available.add("balance");
			available.add("level");
			available.add("showcase");
			ArrayList<String> choices = new ArrayList<String>();
			if (!arg3[0].equals("")) {
				for(String type: available) {
					if (type.toLowerCase().contains(arg3[0].toLowerCase())) {
						choices.add(type.toLowerCase());
					}
				}
			}else {
				for(String type: available) {
						choices.add(type.toLowerCase());
				}
			}
			Collections.sort(choices);
			return choices;
		}else if(arg3.length==2) {
			ArrayList<String> available = new ArrayList<String>();
			available.add("fakeinvite");
			available.add("sendfake");
			available.add("acceptfake");
			available.add("removefakeisland");
			available.add("createfake");
			ArrayList<String> choices = new ArrayList<String>();
			if (!arg3[0].equals("")) {
				for(String type: available) {
					if (type.toLowerCase().contains(arg3[1].toLowerCase())) {
						choices.add(type.toLowerCase());
					}
				}
			}else {
				for(String type: available) {
						choices.add(type.toLowerCase());
				}
			}
			Collections.sort(choices);
			return choices;
		}else {
			return null;
		}
	}
}
