package work.torp.tikirewards.classes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import work.torp.tikirewards.Main.IGUI;

public class ClaimGUI implements IGUI{

	
	@Override
    public Inventory getInventory() {
   
        Inventory GUI = Bukkit.createInventory(this, 36, "Tiki Rewards");

        ItemStack isCash = new ItemStack(Material.GOLD_INGOT, 1);
		ItemMeta imCash = isCash.getItemMeta();
		imCash.setDisplayName("Redeem Cash");
		imCash.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		isCash.setItemMeta(imCash);
	
        ItemStack isRank = new ItemStack(Material.LADDER, 1);
		ItemMeta imRank = isRank.getItemMeta();
		imRank.setDisplayName("Redeem Rank");
		imRank.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		isRank.setItemMeta(imRank);
	
        GUI.setItem(0, isCash);
        GUI.setItem(1, isRank);
        return GUI;
    }

	@Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {

    }

}