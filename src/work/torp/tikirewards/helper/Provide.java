package work.torp.tikirewards.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import work.torp.tikirewards.Main;
import work.torp.tikirewards.alerts.Alert;
import work.torp.tikirewards.classes.Reward;
import work.torp.tikirewards.classes.Rank;
import work.torp.tikirewards.classes.UnclaimedCash;
import work.torp.tikirewards.classes.UnclaimedGroup;
import work.torp.tikirewards.classes.UnclaimedItem;
import work.torp.tikirewards.classes.UnclaimedMessage;
import work.torp.tikirewards.classes.UnclaimedSpawner;

public class Provide {
	public static Reward donationByID(int donation_id) {
		Reward d = new Reward();
		List<Reward> lstD = Main.getInstance().GetRewards();
		if (lstD != null)
		{
			if (lstD.size() > 0)
			{
				for (Reward don : lstD)
				{
					if (don.getRewardID() == donation_id)
					{
						d = don;
					}
				}
			}
		}
		return d;
	}
	public static Rank rankByRewardID(int donation_id) {
		Rank r = new Rank();
		List<Reward> lstD = Main.getInstance().GetRewards();
		if (lstD != null)
		{
			if (lstD.size() > 0)
			{
				for (Reward don : lstD)
				{
					if (don.getRewardID() == donation_id)
					{
						if (Main.hmRanks.containsKey(don.getRank()))
						{
							Alert.DebugLog("Provide", "rankByRewardID", "Rank found");
							r = Main.hmRanks.get(don.getRank());
						} else {
							Alert.DebugLog("Provide", "rankByRewardID", "Rank not found");
						}
					}
				}
			} else {
				Alert.DebugLog("Provide", "rankByRewardID", "Reward list is empty");
			}
		} else {
			Alert.DebugLog("Provide", "rankByRewardID", "Reward list is null");
		}
		return r;
	}
	public static List<UnclaimedMessage> getUnclaimedMessages(String uuid)
	{
		List<UnclaimedMessage> lstRet = new ArrayList<UnclaimedMessage>();
		List<UnclaimedMessage> lstM = Main.getInstance().GetUnclaimedMessage();
		if (lstM != null)
		{
			if (lstM.size() > 0)
			{
				for (UnclaimedMessage um : lstM)
				{
					if (um != null) {
						if (um.getUUID() != null) {
							if (um.getUUID().equalsIgnoreCase(uuid)) {
								lstRet.add(um);
							}
						}
					}
				}
			}
		}
		return lstRet;
	}
	public static List<UnclaimedGroup> getUnclaimedGroups(String uuid)
	{
		List<UnclaimedGroup> lstRet = new ArrayList<UnclaimedGroup>();
		List<UnclaimedGroup> lstG = Main.getInstance().GetUnclaimedGroup();
		if (lstG != null)
		{
			if (lstG.size() > 0)
			{
				for (UnclaimedGroup ug : lstG)
				{
					if (ug.getUUID().equalsIgnoreCase(uuid)) {
						lstRet.add(ug);
					}
				}
			}
		}
		return lstRet;
	}
	public static List<UnclaimedCash> getUnclaimedCash(String uuid)
	{
		List<UnclaimedCash> lstRet = new ArrayList<UnclaimedCash>();
		List<UnclaimedCash> lstC = Main.getInstance().GetUnclaimedCash();
		if (lstC != null)
		{
			if (lstC.size() > 0)
			{
				for (UnclaimedCash uc : lstC)
				{
					if (uc.getUUID().equalsIgnoreCase(uuid)) {
						lstRet.add(uc);
					}
				}
			}
		}
		return lstRet;
	}
	public static List<UnclaimedItem> getUnclaimedItems(String uuid)
	{
		List<UnclaimedItem> lstRet = new ArrayList<UnclaimedItem>();
		List<UnclaimedItem> lstI = Main.getInstance().GetUnclaimedItem();
		if (lstI != null)
		{
			if (lstI.size() > 0)
			{
				for (UnclaimedItem ui : lstI)
				{
					if (ui.getUUID().equalsIgnoreCase(uuid)) {
						lstRet.add(ui);
					}
				}
			}
		}
		return lstRet;
	}
	public static List<UnclaimedSpawner> getUnclaimedSpawner(String uuid)
	{
		List<UnclaimedSpawner> lstRet = new ArrayList<UnclaimedSpawner>();
		List<UnclaimedSpawner> lstS = Main.getInstance().GetUnclaimedSpawner();
		if (lstS != null)
		{
			if (lstS.size() > 0)
			{
				for (UnclaimedSpawner us : lstS)
				{
					if (us.getUUID().equalsIgnoreCase(uuid)) {
						lstRet.add(us);
					}
				}
			}
		}
		return lstRet;
	}
	public static double getBalance(String uuid)
	{
		OfflinePlayer oplayer = Bukkit.getPlayer(UUID.fromString(uuid));
		Economy econ = Main.getInstance().getEconomy();
		double bal = econ.getBalance(oplayer);   
		return bal;
	}
	public static EntityType getEntityTypeFromSpawner(Player player, ItemStack istack)
	{
		EntityType entitytype = EntityType.PIG;
		
		List<String> lore = new ArrayList<String>();
		if (istack.getType() == Material.SPAWNER)
		{
			Alert.DebugLog("Provide", "getEntityTypeFromSpawner", "Itemstack is a spawner");
			if(istack.getItemMeta().getLore() != null) {
				Alert.DebugLog("Provide", "getEntityTypeFromSpawner", "Lore is not null");
				lore = istack.getItemMeta().getLore();
				for (String s : lore)
				{
					s = ChatColor.stripColor(s);
					if (s.length() > 1)
					{
						if (s.substring(0,  4).equals("ID: "))
						{
							String hash = s.replace("ID: ",  "");
							for (EntityType et : EntityType.values())
							{
								String playerhash = Integer.toString((player.getUniqueId().toString() + "-" + et.name()).hashCode());
								if (playerhash.equals(hash))
								{
									entitytype = et;
								}
							}
						} else {
							Alert.DebugLog("Provide", "getEntityTypeFromSpawner", "ID not found");
						}
					}
				}
			} else {
				Alert.DebugLog("Provide", "getEntityTypeFromSpawner", "Lore is null");
			}
		} else {
			Alert.VerboseLog("getEntityTypeFromSpawner", "Itemstack is not a spawner");
			return null;
		}

		return entitytype;
	}
	public static ItemStack getSpawner(Player player, EntityType entitytype, int spawnerID, int spawnercount, boolean displayOwner, boolean displayDate, Rank r)
	{
		ItemStack istack = new ItemStack(Material.SPAWNER, spawnercount);
		if (istack != null)
		{
			ItemMeta imeta = istack.getItemMeta();
			if (imeta != null)
			{
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd"); // Create a DateTimeFormatter
				LocalDate localDate = LocalDate.now(); // Get the current date
				List<String> lore = new ArrayList<String>(); // Create a new list for our Lore (this will tag the items as donation items)
				if (r != null)
				{
					lore.add("Rank: " + r.getDisplayName()); // Add our string to the Lore list
				}
				if (entitytype != null) // If our entitytype is not null, then this is a mob spawner
				{
					String loreString = "Type: " + entitytype.name().replace("_", " ").substring(0, 1).toUpperCase() + entitytype.name().replace("_", " ").substring(1).toLowerCase(); // Create a new string to hold our EntityType name
					lore.add(loreString); // Add our string to the Lore list
				}
				if (displayOwner)
				{
					lore.add("Owner: " + player.getDisplayName()); // Add the owner of the spawner to the Lore list
				}
				if (displayDate)
				{
					lore.add("Given: " + dtf.format(localDate)); // Add the current date (formatted yyyy/MM/dd) as the date given to the Lore list
				}
				lore.add(ChatColor.BLACK + "ID: " + Integer.toString((player.getUniqueId().toString() + "-" + entitytype.name()).hashCode()));
				lore.add(ChatColor.BLACK + "Spawner: " + Integer.toString(spawnerID));
				imeta.setLore(lore);
				imeta.setDisplayName(entitytype.name().replace("_", " ").substring(0, 1).toUpperCase() + entitytype.name().replace("_", " ").substring(1).toLowerCase() + " Spawner");
				istack.setItemMeta(imeta);	
				Alert.DebugLog("Provide", "getSpawner", "Created " + entitytype.name() + " spawner + ItemStack for " + player.getDisplayName());
			}
		}
		return istack;
	}
	public static int firstEmptySlot(Player player)
	{
		int slot = -1;
		if (player != null)
		{
			if (player.getInventory().firstEmpty() >= 0)
			{
				slot = player.getInventory().firstEmpty();
			}
		}
		return slot;
	}
	public static UnclaimedItem unclaimedItemByItemID(int itemID)
	{
		UnclaimedItem ret = null;
		if (Main.getInstance().GetUnclaimedItem() != null)
		{
			for (UnclaimedItem ui : Main.getInstance().GetUnclaimedItem())
			{
				if (ui.getItemID() == itemID)
				{
					ret = ui;
					break;
				}
			}
		}
		return ret;
	}
	public static UnclaimedSpawner unclaimedSpawnerBySpawnerID(int spawnerID)
	{
		UnclaimedSpawner ret = null;
		if (Main.getInstance().GetUnclaimedSpawner() != null)
		{
			for (UnclaimedSpawner us : Main.getInstance().GetUnclaimedSpawner())
			{
				if (us.getSpawnerID() == spawnerID)
				{
					ret = us;
					break;
				}
			}
		}
		return ret;
	}
	public static ItemStack getCashItemStack(Player player)
	{
		int total = 0;
		if (Check.hasUnclaimedCash(player.getUniqueId().toString()))
		{
			List<UnclaimedCash> lstC = Provide.getUnclaimedCash(player.getUniqueId().toString());
			if (lstC != null)
			{
				for (UnclaimedCash uc : lstC)
				{
					total = total + uc.getCashAmount();	
				}
			}
		}
		ItemStack isCash = new ItemStack(Material.GOLD_INGOT, 1);
		ItemMeta imCash = isCash.getItemMeta();
		if (imCash != null)
		{
			if (total > 0) {
				imCash.setDisplayName(ChatColor.GREEN + "Claim money");
			} else {
				imCash.setDisplayName(ChatColor.RED + "No money available to claim");
			}
			List<String> lstLore = new ArrayList<String>();
			lstLore.add("Amount: $" + Integer.toString(total));
			imCash.setLore(lstLore);
			isCash.setItemMeta(imCash);
		}
		return isCash;
	}
	public static ItemStack getRankItemStack(Player player)
	{
		ItemStack isGroup = new ItemStack(Material.LADDER, 1);
		ItemMeta imGroup = isGroup.getItemMeta();
		if (imGroup != null)
		{
			if (Check.hasUnclaimedGroup(player.getUniqueId().toString())) {
				imGroup.setDisplayName(ChatColor.GREEN + "Claim Rank");
    		} else {
    			imGroup.setDisplayName(ChatColor.RED + "No rank available to claim");
    		}
			isGroup.setItemMeta(imGroup);
		}
		return isGroup;
	}
}
