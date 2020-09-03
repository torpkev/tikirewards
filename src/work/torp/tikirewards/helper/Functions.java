package work.torp.tikirewards.helper;

import java.util.List;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import work.torp.tikirewards.Main;
import work.torp.tikirewards.alerts.Alert;

public class Functions {
	public static void removeGroups(Player player, List<String> exemptList)
	{
		Permission perm = Main.getInstance().getPermission(); // Create a new Permission object
		Alert.DebugLog("Functions", "removeGroups", "Checking " + player.getDisplayName());
		for (String group : perm.getPlayerGroups(null, player)) { // Loop through all groups the player is in
			Alert.DebugLog("Functions", "removeGroups", "Group: " + group);
			boolean blnRemove = true;
			for (String keep : exemptList) {
				Alert.DebugLog("Functions", "removeGroups", "Keep List: " + keep);
				if (group.equalsIgnoreCase(keep))
				{
					blnRemove = false;
				}
			}
			if (blnRemove)
			{
				Alert.DebugLog("Functions", "removeGroups", "Remove Group: " + group);
				perm.playerRemoveGroup(null, player, group); // Remove the player from the group
			}
	    }
	}
	public static void addToGroup(Player player, String group)
	{
		Alert.DebugLog("Functions", "addToGroup", "Started: " + player.getDisplayName() + " " + group);
		Permission perm = Main.getInstance().getPermission(); // Create a new Permission object
		Alert.DebugLog("Functions", "addToGroup", "Vault Permission object created");
		if (!Check.inGroup(player, group)) {
			Alert.DebugLog("Functions", "addToGroup", "Player is not already in group");
			perm.playerAddGroup(null, player, group);
		} else {
			Alert.DebugLog("Functions", "addToGroup", "Player is already in group");
		}
	}
	public static void payPlayer(Player player, int amt)
	{
		try
		{
			Economy econ = Main.getInstance().getEconomy();
			EconomyResponse r = econ.depositPlayer(player, amt);
			if (!r.transactionSuccess())
			{
				Alert.DebugLog("Functions", "payPlayer", "Unexpected error trying to deposit funds to player.  Error: " + r.errorMessage);
			}
		}
		catch (Exception ex) {
			Alert.DebugLog("Functions", "payPlayer", "Unexpected error trying to deposit funds to player.  Error: " + ex.getMessage());
		}
	}
}
