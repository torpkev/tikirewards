package work.torp.tikirewards.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import work.torp.tikirewards.Main;
import work.torp.tikirewards.Main.IGUI;
import work.torp.tikirewards.alerts.Alert;
import work.torp.tikirewards.classes.UnclaimedItem;
import work.torp.tikirewards.classes.UnclaimedSpawner;
import work.torp.tikirewards.helper.Check;
import work.torp.tikirewards.helper.CommandFunctions;
import work.torp.tikirewards.helper.Convert;
import work.torp.tikirewards.helper.Provide;

public class InventoryEvents implements Listener {
	
//	
//	@EventHandler
//	public void onInventory(InventoryEvent evt) {
//		String invTitle = ChatColor.stripColor(evt.getView().getTitle());
//		Alert.DebugLog("InventoryEvents", "onInventory()", evt.getEventName());
//		if (invTitle.equalsIgnoreCase("Tiki Rewards")) {
//			if (evt instanceof InventoryClickEvent) {
//				Alert.DebugLog("InventoryEvents", "onInventory()", "Event is a click event");
//				onInventoryClick((InventoryClickEvent) evt);
//			}
//			if (evt instanceof InventoryOpenEvent) {
//				Alert.DebugLog("InventoryEvents", "onInventory()", "Event is an open event");
//				onInventoryOpen((InventoryOpenEvent) evt);
//			}
////			if (evt.getEventName().equalsIgnoreCase("InventoryClickEvent")) {
////				
////				
////			}
//		}
//	}
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {  
        if(evt.getInventory().getHolder() instanceof IGUI) // check if the inventory opened is our storage GUI
        { 

	    	if (evt.getWhoClicked() instanceof Player) // check to make sure the HumanEntity that clicked it is a player (should always be so)
	    	{
	    		Player player = (Player) evt.getWhoClicked();
	    		evt.setCancelled(true); // cancel the event no matter what
        		if (evt.getClick() == ClickType.LEFT) {
        			if (evt.getRawSlot() == 0)
        			{
        				if (Check.hasUnclaimedCash(player.getUniqueId().toString()))
        				{
	        				CommandFunctions.payPlayer(player.getUniqueId().toString());
	        	    		evt.getInventory().setItem(0, Provide.getCashItemStack(player));
        				}
        				return;
        			}
        			if (evt.getRawSlot() == 1)
        			{
        				if (Check.hasUnclaimedGroup(player.getUniqueId().toString()))
        				{
	        				CommandFunctions.setGroups(player.getUniqueId().toString());
	        	    		evt.getInventory().setItem(1, Provide.getRankItemStack(player));
        				}
        				return;
        			}
        			if (evt.getRawSlot() >= 2 && evt.getRawSlot() <= 36)
        			{
        				int emptySlot = Provide.firstEmptySlot(player);
        				if (emptySlot >= 0)
        				{
        					ItemStack istack = new ItemStack(Material.AIR, 1); // Create a new 'AIR' ItemStack to use to remove the redeemable item
        					ItemStack redeemItem = evt.getInventory().getItem(evt.getRawSlot()); // Create the ItemStack to redeem
        					if (redeemItem != null)
        					{
	        					ItemMeta redeemMeta = redeemItem.getItemMeta();
	        					evt.getInventory().setItem(evt.getRawSlot(), istack); // Set the redeemable item to 'AIR' first (no duplication of items ever)
	        					

	        					String redeemName = redeemItem.getType().name();
	        					if (redeemMeta != null)
	        					{
	        						if (!redeemMeta.getDisplayName().equals(""))
	        						{
	        							redeemName = redeemMeta.getDisplayName();
	        						}
	        						String itemDonationID = "";
	        						String spawnerDonationID = "";
	        						List<String> lstLore = new ArrayList<String>();
	        						if (redeemMeta.getLore() != null)
	        						{
	        							redeemMeta.getLore();
	        							for (String l : redeemMeta.getLore())
	        							{
	        								if (redeemItem.getType() == Material.SPAWNER)
	    	        						{
	        									if (!ChatColor.stripColor(l).contains("Spawner: "))
		        								{
	        										lstLore.add(l);
		        								} else {
		        									lstLore.add(l);
	        										spawnerDonationID = ChatColor.stripColor(l).replace("Spawner: ", "");
		        								}
	    	        						} else {
	        									if (!ChatColor.stripColor(l).contains("Item: "))
		        								{
		        									lstLore.add(l);
		        								} else {
		        									itemDonationID = ChatColor.stripColor(l).replace("Item: ", "");
		        								}
	    	        						}
	        							}
	        							redeemMeta.setLore(lstLore);
	        							redeemItem.setItemMeta(redeemMeta);
	        						}
	        						if (redeemItem.getType() == Material.SPAWNER)
	        						{
		        						int spawnerID = Convert.IntegerFromString(spawnerDonationID);
		        						if (spawnerID > -1)
		        						{
		        							Main.getInstance().RemoveUnclaimedSpawner(Provide.unclaimedSpawnerBySpawnerID(spawnerID));
		        							Main.getInstance().getDatabase().claimSpawner(spawnerID, player.getUniqueId().toString());
		    	        					player.getInventory().setItem(emptySlot, redeemItem); // Add the redeemable item to the players inventory
		    	        					Alert.Player("You have redeemed " + Integer.toString(redeemItem.getAmount()) + "x " + redeemName, player, true);
		        						} else {
		        							Alert.Player("Error redeeming spawner - Please contact a member of staff", player, true);
		        						}	        							
	        						} else {
		        						int itemID = Convert.IntegerFromString(itemDonationID);
		        						if (itemID > -1)
		        						{
		        							Main.getInstance().RemoveUnclaimedItem(Provide.unclaimedItemByItemID(itemID));
		        							Main.getInstance().getDatabase().claimItem(itemID, player.getUniqueId().toString());
		    	        					player.getInventory().setItem(emptySlot, redeemItem); // Add the redeemable item to the players inventory
		    	        					Alert.Player("You have redeemed " + Integer.toString(redeemItem.getAmount()) + "x " + redeemName, player, true);
		        						} else {
		        							Alert.Player("Error redeeming item - Please contact a member of staff", player, true);
		        						}
	        						}
	        					}
        					}
        				} else {
        					Alert.Player("Error redeeming item - You must have a free slot available", player, true);
        				}
        			}
        		}
	    	}
        }
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent evt)
	{
        if(evt.getInventory().getHolder() instanceof IGUI) // check if the inventory opened is our storage GUI
        { 

        		Player player = null; // create the Player object
    	    	if (evt.getPlayer() instanceof Player) // check to make sure the HumanEntity that clicked it is a player (should always be so)
    	    	{
    	    		player = (Player) evt.getPlayer();

    	    		// set cash
    	    		evt.getInventory().setItem(0, Provide.getCashItemStack(player));
					// set ranks
    	    		evt.getInventory().setItem(1, Provide.getRankItemStack(player));
    	    	

					

    	    		int i = 2;
    	    		if (Check.hasUnclaimedItem(player.getUniqueId().toString()))
    	    		{
    	    			List<UnclaimedItem> lstU = Provide.getUnclaimedItems(player.getUniqueId().toString());
    	    			if (lstU != null)
    	    			{
    	    				
    	    				for (UnclaimedItem ui : lstU)
    	    				{
    	    					if (i <= 36)
    	    					{
	    	    					ItemStack istack = ui.getItem(player);
	    	    					evt.getInventory().setItem(i, istack);
	    	    					i++;
    	    					}
    	    				}
    	    			}
    	    		}
    	    		if (Check.hasUnclaimedSpawner(player.getUniqueId().toString()))
    	    		{
    	    			List<UnclaimedSpawner> lstS = Provide.getUnclaimedSpawner(player.getUniqueId().toString());
    	    			if (lstS != null)
    	    			{
    	    				
    	    				for (UnclaimedSpawner us : lstS)
    	    				{
    	    					if (i <= 36)
    	    					{
	    	    					ItemStack istack = us.getSpawner(player);
	    	    					evt.getInventory().setItem(i, istack);
	    	    					i++;
    	    					}
    	    				}
    	    			}
    	    		}    	    		
    	    	}

        }
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent evt)
	{
        if(evt.getInventory().getHolder() instanceof IGUI) // check if the inventory opened is our storage GUI
        { 
        	//if (ChatColor.stripColor(evt.getInventory().getName()).equals(ChatColor.stripColor("Tiki Rewards"))) // check if the inventory being closed is our RewardCommand Rewards
    		//{
//	    		if (Main.PlayerInvslot.containsKey(player.getUniqueId()))
//				{
//    				Main.PlayerInvslot.remove(player.getUniqueId());
//				}
    		//}
        }
	}
}
