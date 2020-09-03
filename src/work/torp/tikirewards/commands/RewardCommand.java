package work.torp.tikirewards.commands;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import work.torp.tikirewards.Main;
import work.torp.tikirewards.alerts.Alert;
import work.torp.tikirewards.classes.ClaimGUI;
import work.torp.tikirewards.classes.Rank;
import work.torp.tikirewards.helper.Check;
import work.torp.tikirewards.helper.CommandFunctions;
import work.torp.tikirewards.helper.Debug;

public class RewardCommand implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Alert.DebugLog("RewardCommand", "onCommand", "/reward command run");
		boolean playerSent = false;
		if (sender instanceof Player) {
			Alert.DebugLog("RewardCommand", "onCommand", "Player sent");
			playerSent = true;
			if (Main.getInstance().getConsoleOnly())
			{
				Alert.Sender("This command can only be performed on Console", sender, true);
				return true;
			}
		} else {
			// Console sent
			Alert.DebugLog("RewardCommand", "onCommand", "Console sent");
		}
		
		boolean hasGive = false;
		boolean hasClaim = false;
		boolean hasDebug = false;
		if (playerSent)
		{
			if (Check.hasPermission((Player) sender, "tikirewards.give"))
			{
				hasGive = true;
			}
			if (Check.hasPermission((Player) sender, "tikirewards.claim"))
			{
				hasClaim = true;
			}
			if (Check.hasPermission((Player) sender, "tikirewards.debug"))
			{
				hasDebug = true;
			}
		} else {
			hasGive = true;
			hasClaim = false; // Console shouldn't be able to claim
			hasDebug = true;
		}
		

		StringBuilder fullargs = new StringBuilder();
		int iargs = 0;
		if (args.length > 0) {
			for (Object o : args)
			{
				iargs++;
				fullargs.append(o.toString());
				fullargs.append(" ");
			}
			Alert.DebugLog("RewardCommand", "onCommand", "/reward command run by " + sender.getName() + " with arguments: " + fullargs);
		}
		if (iargs >= 1)
		{
			if (args[0] != null)
			{
				switch (args[0])
				{			
					case "debug" :
						if (hasDebug)
						{
							// TODO: Toggle debug
						} else {
							Alert.Sender("You do not have permission to run this command", sender, true);
							Alert.DebugLog("RewardCommand", "onCommand", "Invalid permissions for /reward command run by " + sender.getName() + ": /reward " + fullargs);
						}
						break;	
					case "debugconfig" :
						if (hasDebug)
						{
							Debug.AlertConfig();
						} else {
							Alert.Sender("You do not have permission to run this command", sender, true);
							Alert.DebugLog("RewardCommand", "onCommand", "Invalid permissions for /reward command run by " + sender.getName() + ": /reward " + fullargs);
						}
						break;
					case "list" :
						if (hasGive)
						{
							// TODO: Print out list of ranks
							if (Main.hmRanks != null) {
								String ranklist = "Reward ranks available: ";
								for (Map.Entry<String, Rank> mapRank : Main.hmRanks.entrySet()) {
									ranklist = ranklist + mapRank.getValue().getInternalName() + ", ";
								}
								ranklist = ranklist + "#";
								ranklist = ranklist.replace(", #",  "");
								ranklist = ranklist.replace("#",  "");
								Alert.Sender(ranklist, sender, true);
							} else {
								Alert.Sender("No reward ranks found", sender, true);
							}
						} else {
							Alert.Sender("You do not have permission to run this command", sender, true);
							Alert.DebugLog("RewardCommand", "onCommand", "Invalid permissions for /reward command run by " + sender.getName() + ": /reward " + fullargs);
						}
						break;
					case "claim" :
						if (hasClaim)
						{
							if (playerSent)
							{
								Player player = (Player) sender;
								ClaimGUI gui = new ClaimGUI();
								player.openInventory(gui.getInventory()); // display the GUI
							} else {
								Alert.Sender("You must be a logged in player to send the claim command", sender, true);
								Alert.DebugLog("RewardCommand", "onCommand", "Console tried to send /reward claim command: /reward " + fullargs);
							}
						} else {
							Alert.Sender("You do not have permission to run this command", sender, true);
							Alert.DebugLog("RewardCommand", "onCommand", "Invalid permissions for /reward command run by " + sender.getName() + ": /reward " + fullargs);
						}
						break;
					case "give" :
						if (iargs >= 2)
						{
							UUID uuid = null;
							String rank = "";
							OfflinePlayer oplayer = null;
							if (args[1] != null)
							{
								boolean validPlayer = false;
								try{
								    uuid = UUID.fromString(args[1].toString());
								    Alert.DebugLog("Command", "Give", "UUID: " + uuid.toString());
							    
								    oplayer = Bukkit.getOfflinePlayer(uuid);
								    if (oplayer != null)
								    {
								    	validPlayer = true;
								    	Alert.DebugLog("RewardCommand", "Give", "Found player by uuid: " + oplayer.getName());
								    } else {
								    	validPlayer = false;
								    	Alert.DebugLog("RewardCommand", "Give", "Unable to find player by uuid: " + uuid.toString());
								    }
								} catch (IllegalArgumentException exception){
									validPlayer = false;
								}
								if (!validPlayer) {
									oplayer = Bukkit.getPlayer(args[1]);
								    if (oplayer != null)
								    {
								    	uuid = oplayer.getUniqueId();
								    	validPlayer = true;
								    	Alert.DebugLog("RewardCommand", "Give", "Found player by name:  " + oplayer.getName());
								    } else {
								    	Alert.DebugLog("RewardCommand", "Give", "Unable to find player by name: : " + args[1].toString());
								    	Alert.Sender("Unable to run command, player is not online or invalid UUID", sender, true);
								    }
								}
								
								
							} else {
								Alert.DebugLog("RewardCommand", "Give", "no arg 1");
							}
							if (args[2] != null)
							{
								rank = args[2].toString();
								Alert.DebugLog("RewardCommand", "Give", "Rank found: " + rank);
							}
							if (hasGive)
							{
								CommandFunctions.giveOfflineReward(oplayer, rank, sender, fullargs.toString());
							} else {
								Alert.Sender("You do not have permission to run this command", sender, true);
								Alert.DebugLog("RewardCommand", "onCommand", "Invalid permissions for /reward command run by " + sender.getName() + ": /reward " + fullargs);
							}
						} else {
							Alert.Sender("Usage: /reward give " + ChatColor.AQUA + "<player name [if player is online] OR uuid> <rank>", sender, true);
							Alert.DebugLog("RewardCommand", "onCommand", "Command sent by " + sender.getName() + " without all arguments: /reward " + fullargs);
						}
						break;
					default:
						break;
				}
			} else {
				if (hasGive || hasClaim || hasDebug)
				{
					Alert.Sender("RewardCommand Rewards Usage:", sender, true);
					if (hasGive)
					{
						Alert.Sender(ChatColor.BOLD + "Give reward items: " + ChatColor.RESET + "/reward give " + ChatColor.AQUA + " <player name [if player is online] OR uuid> <rank>", sender, true);
						Alert.Sender(ChatColor.BOLD + "List reward ranks: " + ChatColor.RESET + "/reward list", sender, true);
					}
					if (hasClaim)
					{
						Alert.Sender(ChatColor.BOLD + "Claim reward items: " + ChatColor.RESET + "/reward claim", sender, true);
					}
					if (hasDebug)
					{
						Alert.Sender(ChatColor.BOLD + "Toggle debug mode:   " + ChatColor.RESET + "/reward debug", sender, true);
						Alert.Sender(ChatColor.BOLD + "View config options: " + ChatColor.RESET + "/reward debugconfig", sender, true);
					}	
				}
				Alert.DebugLog("RewardCommand", "onCommand", "Command sent by " + sender.getName() + " without all arguments: /reward " + fullargs);
			}
		} else {
			Alert.Sender("Usage: /reward " + ChatColor.GOLD + "give" + ChatColor.RESET + "|" + ChatColor.BLUE + "list" + ChatColor.RESET + "|" + ChatColor.DARK_GREEN + "claim " + ChatColor.AQUA + "<player name [if player is online] OR uuid> <rank>", sender, true);
			Alert.DebugLog("RewardCommand", "onCommand", "Command sent by " + sender.getName() + " without all arguments: /reward " + fullargs);
		}
		return true;
	}

}
