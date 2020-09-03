package work.torp.tikirewards.helper;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import work.torp.tikirewards.Main;
import work.torp.tikirewards.alerts.Alert;
import work.torp.tikirewards.classes.Reward;
import work.torp.tikirewards.classes.Item;
import work.torp.tikirewards.classes.Rank;
import work.torp.tikirewards.classes.Spawner;
import work.torp.tikirewards.classes.UnclaimedCash;
import work.torp.tikirewards.classes.UnclaimedGroup;
import work.torp.tikirewards.classes.UnclaimedItem;
import work.torp.tikirewards.classes.UnclaimedMessage;
import work.torp.tikirewards.classes.UnclaimedSpawner;

public class CommandFunctions {
	public static void giveOfflineReward(OfflinePlayer oplayer, String rank, CommandSender sender, String fullargs)
	{
		rank = rank.toLowerCase();
		if (oplayer != null) 
		{
			if (!rank.equalsIgnoreCase(""))
			{
				boolean rankExists = false;
				Rank r = new Rank();
				if (Main.hmRanks.containsKey(rank))
				{
					rankExists = true;
					r = Main.hmRanks.get(rank);
				}
				if (rankExists)
				{
					if (r != null)
					{
						Reward d = CommandFunctions.saveReward(oplayer.getUniqueId().toString(), rank);
						if (d != null)
						{
							boolean blnMessage = false;
							boolean blnGroups = false;
							boolean blnCash = false;
							boolean blnItems = false;
							boolean blnSpawners = false;
							blnMessage = CommandFunctions.saveMessage(d);
							if (Main.getInstance().HasVault())
							{
								blnGroups = CommandFunctions.saveGroups(d);
								blnCash = CommandFunctions.saveCash(d);
							} else {
								blnGroups = true;
								blnCash = true;
							}
							blnItems = CommandFunctions.saveItems(d);
							blnSpawners = CommandFunctions.saveSpawners(d);
							if (!blnMessage)
							{
								Alert.DebugLog("CommandFunctions", "giveOfflineReward", "Unable to save message for " + oplayer.getName());
							}
							if (!blnGroups)
							{
								Alert.DebugLog("CommandFunctions", "giveOfflineReward", "Unable to save groups for " + oplayer.getName());
							}
							if (!blnCash)
							{
								Alert.DebugLog("CommandFunctions", "giveOfflineReward", "Unable to save cash reward for " + oplayer.getName());
							}
							if (!blnItems)
							{
								Alert.DebugLog("CommandFunctions", "giveOfflineReward", "Unable to save reward items for " + oplayer.getName());
							}
							if (!blnSpawners)
							{
								Alert.DebugLog("CommandFunctions", "giveOfflineReward", "Unable to save reward spawners for " + oplayer.getName());
							}
							Player p = Bukkit.getPlayer(oplayer.getUniqueId());
							if (p != null)
							{
								Alert.Player(r.getPlayerMessage().replace("<%player%>", p.getName()), p, true);
								Bukkit.broadcastMessage(r.getBroadcastMessage().replaceAll("<%player%>", p.getName()));
							}
						} else {
							Alert.Log("RewardCommand.Give", "Unable to save donation to " + rank + " for " + oplayer.getName());
						}
					} else {
						Alert.Log("RewardCommand.Give", "Unable to save donation to " + rank + " for " + oplayer.getName() + " - Rank not found");
						Alert.Sender("Unable to save donation to " + rank + " for " + oplayer.getName() + " - Rank not found", sender, true);
					}
				} else {
					Alert.Sender("Invalid rank - Unable to process donor reward", sender, true);
					Alert.DebugLog("CommandFunctions", "giveOfflineReward", "Invalid rank - Unable to process.  Command run by " + sender.getName() + ": /donor " + fullargs);
				}
			} else {
				Alert.Sender("Invalid rank - Unable to process donor reward", sender, true);
				Alert.DebugLog("CommandFunctions", "giveOfflineReward", "Invalid rank - Unable to process.  Command run by " + sender.getName() + ": /donor " + fullargs);
			}
		} else {
			Alert.Sender("Invalid player - Unable to process donor reward", sender, true);
			Alert.DebugLog("CommandFunctions", "giveOfflineReward", "Invalid player - Unable to process.  Command run by " + sender.getName() + ": /donor " + fullargs);
		}	
	}
	public static Reward saveReward(String uuid, String rank) {
		int donation_id = -1;
		donation_id = Main.getInstance().getDatabase().saveReward(uuid, rank);
		
		Reward d = new Reward();
		d.setRewardID(donation_id);
		d.setUUID(uuid);
		d.setRank(rank);
		d.setRewardDateTime(new Timestamp(System.currentTimeMillis()));
		
		Main.getInstance().AddReward(d);
		return d;
	}
	public static boolean saveGroups(Reward d) {
		boolean returnVal = false;
		
		if (d != null)
		{
			int group_id = Main.getInstance().getDatabase().saveGroup(d.getRewardID(), d.getUUID());
			UnclaimedGroup ug = new UnclaimedGroup();
			ug.setGroupID(group_id);
			ug.setRewardID(d.getRewardID());
			ug.setUUID(d.getUUID());
			Rank r = Provide.rankByRewardID(ug.getRewardID());
    		if (r != null)
    		{
    			
    			if (r.getAddGroups() != null) {
    				if (r.getAddGroups().size() > 0) {
						ug.setGroupName(r.getAddGroups());
						Main.getInstance().AddUnclaimedGroup(ug);
    				}
    			}
    			if (r.getKeepGroups() != null) {
					if (r.getKeepGroups().size() > 0) {
						ug.setKeepGroups(r.getKeepGroups());
					}
				}
				if (r.getRemoveOtherGroups()) {
					ug.setRemoveOtherGroups(r.getRemoveOtherGroups());
				}
    		}
    		
    		returnVal = true;
		}
		
		return returnVal;
	}
	public static boolean saveCash(Reward d) {
		
		boolean returnVal = false;
		
		if (d != null)
		{
			int amt = 0;
			Rank r = Provide.rankByRewardID(d.getRewardID());
    		if (r != null)
    		{
    			amt = r.getCashAmount();
    			if (amt > 0) {
	    			int cash_id = Main.getInstance().getDatabase().saveCash(d.getRewardID(), d.getUUID(), amt);
	    			UnclaimedCash uc = new UnclaimedCash();
	    			uc.setCashID(cash_id);
	    			uc.setRewardID(d.getRewardID());
	    			uc.setUUID(d.getUUID());
	    			uc.setCashAmount(amt);
	    			Main.getInstance().AddUnclaimedCash(uc);
    			}
    			returnVal = true;
    		}
		}
		
		return returnVal;
	}
	public static boolean saveItems(Reward d) {
		Alert.DebugLog("CommandFunctions", "saveItems", "saveItems run with: ID = " + Integer.toString(d.getRewardID()) + " Rank = " + d.getRank() + " UUID = " + d.getUUID());
		boolean returnVal = false;
		
		if (d != null)
		{
			Rank r = Provide.rankByRewardID(d.getRewardID());
    		if (r != null)
    		{
    			Alert.DebugLog("CommandFunctions", "saveItems", "Rank found: " + r.getDisplayName());
    			List<Item> lstI = r.getItems();
    			if (lstI != null)
    			{
    				if (lstI.size() > 0)
    				{
    					for (Item i : lstI)
    					{
    						UnclaimedItem ui = new UnclaimedItem();
    						int item_id = Main.getInstance().getDatabase().saveItem(d.getRewardID(), d.getUUID(), i.getName(), i.getMaterial().name(), i.getItemCount(), i.getTagItem());
    						ui.set(
    								item_id, 
    								d.getRewardID(), 
    								d.getUUID(), 
                					i.getName(), 
                					i.getMaterial(), 
                					i.getItemCount(), 
                					i.getTagItem()
                			);

    						Main.getInstance().AddUnclaimedItem(ui);
    						returnVal = true;
    					}
    				} else {
    					returnVal = true;
    				}
    			} else { 
    				returnVal = true;
    			}
    		}
		}
		
		return returnVal;
	}
	public static boolean saveMessage(Reward d) {
		
		boolean returnVal = false;
		
		if (d != null)
		{
			Rank r = Provide.rankByRewardID(d.getRewardID());
    		if (r != null)
    		{
    			UnclaimedMessage um = new UnclaimedMessage();
				int player_message_id = Main.getInstance().getDatabase().saveMessage(d.getRewardID(), d.getUUID(), r.getPlayerMessage());
				um.setMessageID(player_message_id);
				um.setRewardID(d.getRewardID());
				um.setPlayerMessage(r.getPlayerMessage());
				
				Main.getInstance().AddUnclaimedMessage(um);
				returnVal = true;
    		}
		}
		
		return returnVal;
	}
	public static boolean saveSpawners(Reward d) {
		
		boolean returnVal = false;
		
		if (d != null)
		{
			Rank r = Provide.rankByRewardID(d.getRewardID());
    		if (r != null)
    		{
    			List<Spawner> lstS = r.getSpawners();
    			if (lstS != null)
    			{
    				if (lstS.size() > 0)
    				{
    					for (Spawner s : lstS)
    					{
    						UnclaimedSpawner us = new UnclaimedSpawner();
    						int spawner_id = Main.getInstance().getDatabase().saveSpawner(d.getRewardID(), d.getUUID(), s.getEntityType().name(), s.getSpawnerCount());
    						
    						us.set(spawner_id, d.getRewardID(), d.getUUID(), s.getEntityType(), s.getSpawnerCount());

    						Main.getInstance().AddUnclaimedSpawner(us);
    						returnVal = true;
    					}
    				} else {
    					returnVal = true;
    				}
    			} else {
    				returnVal = true;
    			}
    		}
		}
		
		return returnVal;
	}
	public static void setGroups(String uuid)
	{
		Player p = Bukkit.getPlayer(UUID.fromString(uuid));
		if (p != null)
		{
			List<UnclaimedGroup> lstUG = Provide.getUnclaimedGroups(uuid);
			if (lstUG != null)
			{
				for (UnclaimedGroup ug : lstUG)
				{
					
					if (ug.getRemoveOtherGroups())
					{
						Functions.removeGroups(p, ug.getKeepGroups());
					}
					if (ug.getGroupNames() != null)
					{
						for (String group : ug.getGroupNames())
						{
							Functions.addToGroup(p, group);
						}
					}
					Main.getInstance().getDatabase().claimGroups(ug.getGroupID(), uuid);
					Main.getInstance().RemoveUnclaimedGroup(ug);
				}
				Alert.Player("Your groups have been updated", p, true);
			}
		} else {
			Alert.Log("CommandFunctions.setGroups", "Unable to set groups for " + uuid + " - Player is offline or does not exist");
		}
	}
	public static void payPlayer(String uuid)
	{
		int totalPay = 0;
		Player p = Bukkit.getPlayer(UUID.fromString(uuid));
		if (p != null)
		{
			List<UnclaimedCash> lstUC = Provide.getUnclaimedCash(uuid);
			if (lstUC != null)
			{
				for (UnclaimedCash uc : lstUC)
				{
					if (uc.getCashAmount() > 0)
					{
						totalPay = totalPay + uc.getCashAmount();
						Functions.payPlayer(p, uc.getCashAmount());
						Main.getInstance().getDatabase().claimCash(uc.getCashID(), uuid);
						Main.getInstance().RemoveUnclaimedCash(uc);
					}
				}
				double newbal = Provide.getBalance(uuid);
				DecimalFormat formatter = new DecimalFormat("#0.00");        
				Alert.Player("You have been paid " + ChatColor.GREEN + "$" + formatter.format(totalPay), p, true);
				Alert.Player("New balance " + ChatColor.GREEN + "$" + formatter.format(newbal), p, true);
			}
		} else {
			Alert.Log("CommandFunctions.payPlayer", "Unable to deposit funds for " + uuid + " - Player is offline or does not exist");
		}
	}
}
