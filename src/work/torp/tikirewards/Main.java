package work.torp.tikirewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import work.torp.tikirewards.Main;
import work.torp.tikirewards.alerts.Alert;
import work.torp.tikirewards.classes.Reward;
import work.torp.tikirewards.classes.Item;
import work.torp.tikirewards.classes.PlacedSpawner;
import work.torp.tikirewards.classes.Rank;
import work.torp.tikirewards.classes.Spawner;
import work.torp.tikirewards.classes.UnclaimedCash;
import work.torp.tikirewards.classes.UnclaimedGroup;
import work.torp.tikirewards.classes.UnclaimedItem;
import work.torp.tikirewards.classes.UnclaimedMessage;
import work.torp.tikirewards.classes.UnclaimedSpawner;
import work.torp.tikirewards.commands.RewardCommand;
import work.torp.tikirewards.database.Database;
import work.torp.tikirewards.database.SQLite;
import work.torp.tikirewards.events.BlockEvents;
import work.torp.tikirewards.events.CreatureSpawnEvents;
import work.torp.tikirewards.events.InventoryEvents;
import work.torp.tikirewards.events.PlayerEvents;
import work.torp.tikirewards.scheduled.PlayerNag;

public class Main extends JavaPlugin {
	
	public static HashMap<UUID, Integer> PlayerInvslot = new HashMap<UUID, Integer>();
	
	public interface IGUI extends InventoryHolder{
	    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem);
	}
	
	// Main
	private static Main instance;
    public static Main getInstance() {
		return instance;
	}
    
    // Vault
    private static Economy econ = null;
    private static Permission perm = null;
    private boolean hasVault = false;
    public boolean HasVault() {
    	return hasVault;
    }
    private boolean setupEconomy() {
    	try {
	        if (getServer().getPluginManager().getPlugin("Vault") == null) {
	            return false;
	        }
	        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	        if (rsp == null) {
	            return false;
	        }
	        econ = rsp.getProvider();
	        return econ != null;
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "setupEconomy", "Unexpected Error - " + ex.getMessage());  
			return false;
		}
    }
    public Economy getEconomy() {
        return econ;
    }    
    private boolean setupPermissions() {
    	try {
	        if (getServer().getPluginManager().getPlugin("Vault") == null) {
	            return false;
	        }
	        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
	        if (rsp == null) {
	            return false;
	        }
	        perm = rsp.getProvider();
	        return econ != null;
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "setupPermissions", "Unexpected Error - " + ex.getMessage());  
			return false;
		}
    }
    public Permission getPermission() {
        return perm;
    }  
    
    // Database
	private Database db;
    public Database getDatabase() {
        return this.db;
    }
    
    // DebugFile
	private boolean debugfile;
    public boolean getDebugFile() {
        return this.debugfile;
    }
    public void setDebugFile(boolean debugfile) {
    	this.debugfile = debugfile;
    }
    
    // Hashmaps
    public static HashMap<String, Rank> hmRanks = new HashMap<String, Rank>();
    
    // Lists
    private List<Reward> rewards;
    public List<Reward> GetRewards() {
    	return rewards;
    }
    public void AddReward(Reward d)
    {
    	try {
	    	if (rewards == null) {
	    		rewards = new ArrayList<Reward>();
	    	}
	    	if (d != null)
	    	{
	    		rewards.add(d);
	    	}
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "AddReward", "Unexpected Error - " + ex.getMessage());  
		}
    } 
    public void RemoveReward(Reward d)
    {
    	try {
	    	List<Reward> lstD = new ArrayList<Reward>();
	    	if (d != null)
	    	{
	    		if (rewards != null) {
	    			for (Reward don: rewards)
		    		{
	    				if (don.getRewardID() != don.getRewardID())
		    			{
	    					lstD.add(don);
		    			}
		    		}
	    		}
	    	}
	    	rewards = lstD;
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "RemoveReward", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    private List<UnclaimedItem> unclaimedItems;
    public List<UnclaimedItem> GetUnclaimedItem() {
    	return unclaimedItems;
    }
    public void AddUnclaimedItem(UnclaimedItem ui)
    {
    	try {
	    	if (unclaimedItems == null) {
	    		unclaimedItems = new ArrayList<UnclaimedItem>();
	    	}
	    	if (ui != null)
	    	{
	    		unclaimedItems.add(ui);
	    	}
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "AddUnclaimedItem", "Unexpected Error - " + ex.getMessage());  
		}
    } 
    public void RemoveUnclaimedItem(UnclaimedItem ui)
    {
    	try {
	    	List<UnclaimedItem> lstUI = new ArrayList<UnclaimedItem>();
	    	if (ui != null)
	    	{
	    		if (unclaimedItems != null) {
	    			for (UnclaimedItem i : unclaimedItems)
		    		{
	    				if (i.getItemID() != ui.getItemID()) {
	    					lstUI.add(i);
		    			}
		    		}
	    		}
	    	}
	    	unclaimedItems = lstUI;
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "RemoveUnclaimedItem", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    private List<UnclaimedSpawner> unclaimedSpawner;
    public List<UnclaimedSpawner> GetUnclaimedSpawner() {
    	return unclaimedSpawner;
    }
    public void AddUnclaimedSpawner(UnclaimedSpawner us)
    {
    	try {
	    	if (unclaimedSpawner == null) {
	    		unclaimedSpawner = new ArrayList<UnclaimedSpawner>();
	    	}
	    	if (us != null)
	    	{
	    		unclaimedSpawner.add(us);
	    	}
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "AddUnclaimedSpawner", "Unexpected Error - " + ex.getMessage());  
		}
    } 
    public void RemoveUnclaimedSpawner(UnclaimedSpawner us)
    {
    	try {
	    	List<UnclaimedSpawner> lstUS = new ArrayList<UnclaimedSpawner>();
	    	if (us != null)
	    	{
	    		if (unclaimedSpawner != null) {
	    			for (UnclaimedSpawner i : unclaimedSpawner)
		    		{
	    				if (i.getSpawnerID() != us.getSpawnerID())
		    			{
	    					lstUS.add(i);
		    			}
		    		}
	    		}
	    	}
	    	unclaimedSpawner = lstUS;
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "RemoveUnclaimedSpawner", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    private List<UnclaimedGroup> unclaimedGroup;
    public List<UnclaimedGroup> GetUnclaimedGroup() {
    	return unclaimedGroup;
    }
    public void AddUnclaimedGroup(UnclaimedGroup ug)
    {
    	try {
	    	if (unclaimedGroup == null) {
	    		unclaimedGroup = new ArrayList<UnclaimedGroup>();
	    	}
	    	if (ug != null)
	    	{
	    		unclaimedGroup.add(ug);
	    	}
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "AddUnclaimedGroup", "Unexpected Error - " + ex.getMessage());  
		}
    } 
    public void RemoveUnclaimedGroup(UnclaimedGroup ug)
    {
    	try {
	    	List<UnclaimedGroup> lstUG = new ArrayList<UnclaimedGroup>();
	    	if (ug != null)
	    	{
	    		if (unclaimedGroup != null) {
	    			for (UnclaimedGroup i : unclaimedGroup)
		    		{
	    				if (i.getGroupID() != ug.getGroupID())
		    			{
	    					lstUG.add(i);
		    			}
		    		}
	    		}
	    	}
	    	unclaimedGroup = lstUG;
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "RemoveUnclaimedGroup", "Unexpected Error - " + ex.getMessage());  
		}
    }
  
    private List<UnclaimedCash> unclaimedCash;
    public List<UnclaimedCash> GetUnclaimedCash() {
    	return unclaimedCash;
    }
    public void AddUnclaimedCash(UnclaimedCash uc)
    {
    	try {
	    	if (unclaimedCash == null) {
	    		unclaimedCash = new ArrayList<UnclaimedCash>();
	    	}
	    	if (uc != null)
	    	{
	    		unclaimedCash.add(uc);
	    	}
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "AddUnclaimedCash", "Unexpected Error - " + ex.getMessage());  
		}
    } 
    public void RemoveUnclaimedCash(UnclaimedCash uc)
    {
    	try {
	    	List<UnclaimedCash> lstUC = new ArrayList<UnclaimedCash>();
	    	if (uc != null)
	    	{
	    		if (unclaimedCash != null) {
	    			for (UnclaimedCash i : unclaimedCash)
		    		{
	    				if (i.getCashID() != uc.getCashID())
		    			{
	    					lstUC.add(i);
		    			}
		    		}
	    		}
	    	}
	    	unclaimedCash = lstUC;
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "RemoveUnclaimedCash", "Unexpected Error - " + ex.getMessage());  
		}
    }

    private List<UnclaimedMessage> unclaimedMessage;
    public List<UnclaimedMessage> GetUnclaimedMessage() {
    	return unclaimedMessage;
    }
    public void AddUnclaimedMessage(UnclaimedMessage um)
    {
    	try {
	    	if (unclaimedMessage == null) {
	    		unclaimedMessage = new ArrayList<UnclaimedMessage>();
	    	}
	    	if (um != null)
	    	{
	    		unclaimedMessage.add(um);
	    	}
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "AddUnclaimedMessage", "Unexpected Error - " + ex.getMessage());  
		}
    } 
    public void RemoveUnclaimedMessage(UnclaimedMessage um)
    {
    	try {
	    	List<UnclaimedMessage> lstUM = new ArrayList<UnclaimedMessage>();
	    	if (um != null)
	    	{
	    		if (unclaimedMessage != null) {
	    			for (UnclaimedMessage i : unclaimedMessage)
		    		{
	    				if (i.getMessageID() != um.getMessageID())
		    			{
	    					lstUM.add(i);
		    			}
		    		}
	    		}
	    	}
	    	unclaimedMessage = lstUM;
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "RemoveUnclaimedMessage", "Unexpected Error - " + ex.getMessage());  
		}
    }

    private List<PlacedSpawner> placedSpawner;
    public List<PlacedSpawner> GetPlacedSpawner() {
    	return placedSpawner;
    }
    public void AddPlacedSpawner(PlacedSpawner ps)
    {
    	try {
	    	if (placedSpawner == null) {
	    		placedSpawner = new ArrayList<PlacedSpawner>();
	    	}
	    	if (ps != null)
	    	{
	    		placedSpawner.add(ps);
	    	}
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "AddPlacedSpawner", "Unexpected Error - " + ex.getMessage());  
		}
    } 
    public void RemovePlacedSpawner(PlacedSpawner ps)
    {
    	try {
	    	List<PlacedSpawner> lstPS = new ArrayList<PlacedSpawner>();
	    	if (ps != null)
	    	{
	    		if (placedSpawner != null) {
	    			for (PlacedSpawner i : placedSpawner)
		    		{
	    				if (i.getPlacedSpawnerID() != ps.getPlacedSpawnerID())
		    			{
	    					lstPS.add(i);
		    			}
		    		}
	    		}
	    	}
	    	placedSpawner = lstPS;
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "RemovePlacedSpawner", "Unexpected Error - " + ex.getMessage());  
		}
    }

    // Config
    private int nag_seconds = 600;
    private boolean console_only = false;
    public boolean getConsoleOnly() {
    	return this.console_only;
    }
    
    // Configuration
    public void loadConfig() {
    	try {
    		
    		String s_console_only = Main.getInstance().getConfig().getString("console_only");
    		console_only = false; // default to false
	    	if (s_console_only != null) {
	    		if (s_console_only.equalsIgnoreCase("true")) {
	    			console_only = true;
				} else if (s_console_only.equalsIgnoreCase("true")) {
					console_only = false;
				} else {
					Alert.Log("Main.loadConfig", "console_only value is invalid, using default of false");
				}
	    	} else {
	    		Alert.Log("Main.loadConfig", "console_only value not found, using default of false");
	    	}	
	    	
	    	int inagSecs = 600; // Default to 600 seconds
			nag_seconds = inagSecs;
	    	if (Main.getInstance().getConfig().getString("nag_seconds") != null)
    		{
    			String s_nagsecs = Main.getInstance().getConfig().getString("nag_seconds");
    			try{
    				inagSecs = Integer.parseInt(s_nagsecs);
    				nag_seconds = inagSecs;
    			} 
    			catch (NumberFormatException ex) {
    				Alert.DebugLog("Main", "loadConfig", "Config - nag_seconds invalid, must be a number. Using default");	
    			}
    		} else {
    			Alert.DebugLog("Main", "loadConfig", "Config - nag_seconds is missing. Using default");	
    		}
	    	
			Set<String> lstRanks = Main.getInstance().getConfig().getConfigurationSection("rank").getKeys(false);
			if (lstRanks != null)
			{
				if (lstRanks.size() > 0)
				{
					for (String rank : lstRanks)
					{
						// Loop through ranks
						Rank r = new Rank();
						
						// internal name
						r.setInternalName(rank.toLowerCase()); 
						
						// display_name
						String displayName = Main.getInstance().getConfig().getString("rank." + rank + ".name");
						
						Alert.DebugLog("Main", "loadConfig", "Rank: " + displayName);
						
						if (displayName != null)
						{
							r.setDisplayName(displayName);
						} else {
							r.setDisplayName(rank); // Default to rank
						}
						// player_msg
						String playerMsg = Main.getInstance().getConfig().getString("rank." + rank + ".player_msg");
						if (playerMsg != null)
						{
							r.setPlayerMessage(playerMsg);
						} else {
							playerMsg = ""; // Default to blank
						}
						// broadcast
						String broadcastMsg = Main.getInstance().getConfig().getString("rank." + rank + ".broadcast");
						if (broadcastMsg != null)
						{
							r.setBroadcastMessage(broadcastMsg);
						} else {
							broadcastMsg = ""; // Default to blank
						}
						// add_groups
						List<String> lstAddGroups = Main.getInstance().getConfig().getStringList("rank." + rank + ".add_groups"); 
						if (lstAddGroups != null)
						{
							if (lstAddGroups.size() > 0)
							{
								List<String> lstNewAddGroups = new ArrayList<String>();
								for (String addgroup : lstAddGroups)
								{
									lstNewAddGroups.add(addgroup);
								}
								r.setAddGroups(lstNewAddGroups);
							} else {
								r.setAddGroups(new ArrayList<String>()); // Default to empty list
							}
						} else {
							r.setAddGroups(new ArrayList<String>()); // Default to empty list
						}
						// remove_other_groups
						String s_remove_other_groups = Main.getInstance().getConfig().getString("rank." + rank + ".remove_other_groups");
						r.setRemoveOtherGroups(false); // Default to false
				    	if (s_remove_other_groups != null) {
				    		if (s_remove_other_groups.equalsIgnoreCase("true")) {
				    			r.setRemoveOtherGroups(true);
							} else if (s_remove_other_groups.equalsIgnoreCase("true")) {
								r.setRemoveOtherGroups(false);
							} else {
								Alert.Log("Main.loadConfig", "remove_other_groups value for rank " + rank + " is invalid, using default of false");
							}
				    	} else {
				    		Alert.Log("Main.loadConfig", "remove_other_groups value for rank " + rank + " not found, using default of false");
				    	}	
				    	// keep_groups
				    	List<String> lstKeepGroups = Main.getInstance().getConfig().getStringList("rank." + rank + ".keep_groups"); 
						if (lstKeepGroups != null)
						{
							if (lstKeepGroups.size() > 0)
							{
								List<String> lstNewKeepGroups = new ArrayList<String>();
								for (String keepgroup : lstKeepGroups)
								{
									lstNewKeepGroups.add(keepgroup);
								}
								r.setKeepGroups(lstNewKeepGroups);
							} else {
								r.setKeepGroups(new ArrayList<String>()); // Default to empty list
							}
						} else {
							r.setKeepGroups(new ArrayList<String>()); // Default to empty list
						}
						// cash
						String s_cash = Main.getInstance().getConfig().getString("rank." + rank + ".cash");
						if (s_cash != null) {
							int cash = 0; // Default to 0
							try{
								cash = Integer.parseInt(s_cash);
								r.setCashAmount(cash);
							} 
							catch (NumberFormatException ex) {
								Alert.Log("Main.loadConfig", "cash value for rank " + rank + " is invalid, using default of 0");
							}
						} else {
							Alert.Log("Main.loadConfig", "cash value for rank " + rank + " not found, using default of 0");
						}
						// items
						Alert.DebugLog("Main", "loadConfig", "Rank: " + displayName + " - Checking Items");
						if (getConfig().getConfigurationSection("rank." + rank + ".items") != null)
						{
							Set<String> lstItems = Main.getInstance().getConfig().getConfigurationSection("rank." + rank + ".items").getKeys(false);
							Alert.DebugLog("Main", "loadConfig", "Rank: " + displayName + " - Checking Items 2");
							if (lstItems != null)
							{
								if (lstItems.size() > 0)
								{
									List<Item> lstNewItems = new ArrayList<Item>();
									for (String item : lstItems)
									{
										try
										{
											Material m = Material.valueOf(item);
											if (m != null)
											{
												Item i = new Item();
												String s_name = Main.getInstance().getConfig().getString("rank." + rank + ".items." + item + ".name");
												if (s_name != null)
												{
													i.setName(s_name);
												}
												i.setMaterial(m);
												// count
												String s_item_cnt = Main.getInstance().getConfig().getString("rank." + rank + ".items." + item + ".count");
												if (s_item_cnt != null) {
													int item_cnt = 0; // Default to 0
													try{
														item_cnt = Integer.parseInt(s_item_cnt);
														i.setItemCount(item_cnt);
													} 
													catch (NumberFormatException ex) {
														Alert.Log("Main.loadConfig", "count value for rank " + rank + " - Item: " + item + " is invalid, using default of 0");
													}
												} else {
													Alert.Log("Main.loadConfig", "count value for rank " + rank + " - Item: " + item + " not found, using default of 0");
												}
												// tag
												String s_item_tag = Main.getInstance().getConfig().getString("rank." + rank + ".items." + item + ".tag");
												i.setTagItem(false); // Default to false
										    	if (s_item_tag != null) {
										    		if (s_item_tag.equalsIgnoreCase("true")) {
										    			i.setTagItem(true);
													} else if (s_item_tag.equalsIgnoreCase("false")) {
														i.setTagItem(false);
													} else {
														Alert.Log("Main.loadConfig", "tag value for rank " + rank + " - Item: " + item + " is invalid, using default of false");
													}
										    	} else {
										    		Alert.Log("Main.loadConfig", "tag value for rank " + rank + " - Item: " + item + " not found, using default of false");
										    	}												
										    	lstNewItems.add(i);
											}
											r.setItems(lstNewItems);
										} 
										catch (Exception ex)
										{
											Alert.Log("Main.loadConfig", "Material value for rank " + rank + " - Item: " + item + " is invalid, disregarding.");
										}
									}
								} else {
									Alert.DebugLog("Main", "loadConfig", "Rank: " + displayName + " - No Items");
									r.setItems(new ArrayList<Item>());
								}
							} else {
								Alert.DebugLog("Main", "loadConfig", "Rank: " + displayName + " - No Items");
								r.setItems(new ArrayList<Item>());
							}
						}
						
						// spawners
						Alert.DebugLog("Main", "loadConfig", "Rank: " + displayName + " - Checking Spawners");
						if (getConfig().getConfigurationSection("rank." + rank + ".spawners") != null)
						{
							Set<String> lstSpawners = Main.getInstance().getConfig().getConfigurationSection("rank." + rank + ".spawners").getKeys(false);
							if (lstSpawners != null)
							{
								if (lstSpawners.size() > 0)
								{
									List<Spawner> lstNewSpawners = new ArrayList<Spawner>();
									for (String spawner : lstSpawners)
									{
										try
										{
											EntityType et = EntityType.valueOf(spawner);
											if (et != null)
											{
												Spawner s = new Spawner();
												s.setEntityType(et);
												// count
												String s_spawner_cnt = Main.getInstance().getConfig().getString("rank." + rank + ".spawners." + spawner + ".count");
												if (s_spawner_cnt != null) {
													int spawner_cnt = 0; // Default to 0
													try{
														spawner_cnt = Integer.parseInt(s_spawner_cnt);
														s.setSpawnerCount(spawner_cnt);
													} 
													catch (NumberFormatException ex) {
														Alert.Log("Main.loadConfig", "count value for rank " + rank + " - Spawner: " + spawner + " is invalid, using default of 0");
													}
												} else {
													Alert.Log("Main.loadConfig", "count value for rank " + rank + " - Spawner: " + spawner + " not found, using default of 0");
												}									
										    	lstNewSpawners.add(s);
											}
											r.setSpawners(lstNewSpawners);
										} 
										catch (Exception ex)
										{
											Alert.Log("Main.loadConfig", "Entity value for rank " + rank + " - Spawner: " + spawner + " is invalid, disregarding.");
										}
									}
								} else {
									Alert.DebugLog("Main", "loadConfig", "Rank: " + displayName + " - No Spawners");
									r.setItems(new ArrayList<Item>());
								}
							} else {
								Alert.DebugLog("Main", "loadConfig", "Rank: " + displayName + " - No Spawners");
								r.setItems(new ArrayList<Item>());
							}
						}
						
						Main.hmRanks.put(rank, r);
					}
				}
			}
    	}
    	catch (Exception ex) {
    		Alert.Log("Load Configuration", "Unexpected Error - " + ex.getMessage());  	
    	}
    }
    
    public void loadEventListeners() {
		Alert.VerboseLog("Main", "Starting Event Listeners");	
		try {
			Bukkit.getPluginManager().registerEvents(new BlockEvents(), this);
			Bukkit.getPluginManager().registerEvents(new CreatureSpawnEvents(), this);
			Bukkit.getPluginManager().registerEvents(new InventoryEvents(), this);
			Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
		} catch (Exception ex) {
			Alert.Log("Load Event Listeners", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    // Commands
    public void loadCommands() {
    	Alert.DebugLog("Main", "loadCommands", "Activating /reward");
		try {
		    	getCommand("reward").setExecutor(new RewardCommand());
		} catch (Exception ex) {
			Alert.Log("Load Commands", "Unexpected Error - " + ex.getMessage());  
		}
    }

    // Load from Database
    public void loadFromDatabase() {
    	Alert.Log("Main.loadFromDatabase", "Getting Rewards");
    	this.db.getRewards();
    	if (rewards != null)
    	{
    		Alert.DebugLog("Main", "loadFromDatabase", "# Rewards: " + Integer.toString(rewards.size()));
    	} else {
    		Alert.DebugLog("Main", "loadFromDatabase", "Rewards list is null");
    	}
    	Alert.VerboseLog("Main.loadFromDatabase", "Getting Unclaimed Cash");
    	this.db.getUnclaimedCash();
    	if (unclaimedCash != null)
    	{
    		Alert.DebugLog("Main", "loadFromDatabase", "# Unclaimed Cash: " + Integer.toString(unclaimedCash.size()));
    	} else {
    		Alert.DebugLog("Main", "loadFromDatabase", "Unclaimed Cash list is null");
    	}
    	Alert.VerboseLog("Main.loadFromDatabase", "Getting Unclaimed Groups");
    	this.db.getUnclaimedGroups();
    	if (unclaimedGroup != null)
    	{
    		Alert.DebugLog("Main", "loadFromDatabase", "# Unclaimed Groups: " + Integer.toString(unclaimedGroup.size()));
    	} else {
    		Alert.DebugLog("Main", "loadFromDatabase", "Unclaimed Groups list is null");
    	}
    	Alert.VerboseLog("Main.loadFromDatabase", "Getting Unclaimed Items");
    	this.db.getUnclaimedItem();
    	if (unclaimedItems != null)
    	{
    		Alert.DebugLog("Main", "loadFromDatabase", "# Unclaimed Items: " + Integer.toString(unclaimedItems.size()));
    	} else {
    		Alert.DebugLog("Main", "loadFromDatabase", "Unclaimed Items list is null");
    	}
    	Alert.VerboseLog("Main.loadFromDatabase", "Getting Unclaimed Spawners");
    	this.db.getUnclaimedSpawner();
    	if (unclaimedSpawner != null)
    	{
    		Alert.DebugLog("Main", "loadFromDatabase", "# Unclaimed Spawners: " + Integer.toString(unclaimedSpawner.size()));
    	} else {
    		Alert.DebugLog("Main", "loadFromDatabase", "Unclaimed Spawner list is null");
    	}
    	Alert.VerboseLog("Main.loadFromDatabase", "Getting Placed Spawners");
    	this.db.getPlacedSpawner();
    	if (placedSpawner != null)
    	{
    		Alert.DebugLog("Main", "loadFromDatabase", "# Placed Spawners: " + Integer.toString(placedSpawner.size()));
    	} else {
    		Alert.DebugLog("Main", "loadFromDatabase", "Placed Spawner list is null");
    	}
    	Alert.VerboseLog("Main.loadFromDatabase", "Getting Unclaimed Messages");
    	this.db.getUnclaimedMessage();
    	if (unclaimedMessage != null)
    	{
    		Alert.DebugLog("Main", "loadFromDatabase", "# Unclaimed Messages: " + Integer.toString(unclaimedMessage.size()));
    	} else {
    		Alert.DebugLog("Main", "loadFromDatabase", "Unclaimed Message list is null");
    	}
    }
    
    // Scheduled
    public void startPlayerNag() {
    	try {
	        BukkitTask task = new BukkitRunnable() {       	
	            public void run() {
	            	PlayerNag.Run();
	            }
	        }.runTaskTimer(getInstance(), 0, nag_seconds * 20);
	        Alert.DebugLog("Main", "startPlayerNag", "startPlayerNag running with id " + task.getTaskId());
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "startPlayerNag", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    // Enable
    @Override
	public void onEnable() {
    	
    	try {
			instance = this;

			saveDefaultConfig();
			Alert.Log("Main", "Starting RewardCommand Rewards");
			this.db = new SQLite(this); // New SQLite
	        this.db.load(); // Run load
	        this.db.initialize(); // Run initialize
	        loadConfig();
	        loadEventListeners();
	        loadCommands();
	        Alert.DebugLog("Main", "onEnable", "Loading Vault");
			if (setupEconomy() && setupPermissions())
			{
				Alert.VerboseLog("Main", "Vault dependency loaded");
	        	hasVault = true;
			} else {
	            Alert.Log("Main", "No Vault dependency found - Unable to process any cash rewards or process groups");
	            hasVault = false;
			}
			loadFromDatabase();
			startPlayerNag();
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "onEnable", "Unexpected Error - " + ex.getMessage());  
		}
	}

    // Disable
    @Override
    public void onDisable() {
    	
	}
}
