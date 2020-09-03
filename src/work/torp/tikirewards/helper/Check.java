package work.torp.tikirewards.helper;

import java.util.List;
import org.bukkit.entity.Player;

import net.milkbowl.vault.permission.Permission;
import work.torp.tikirewards.Main;
import work.torp.tikirewards.alerts.Alert;
import work.torp.tikirewards.classes.UnclaimedCash;
import work.torp.tikirewards.classes.UnclaimedGroup;
import work.torp.tikirewards.classes.UnclaimedItem;
import work.torp.tikirewards.classes.UnclaimedMessage;
import work.torp.tikirewards.classes.UnclaimedSpawner;

public class Check {
	public static boolean hasPermission(Player player, String permission)
	{
		if (player.isOp() || player.hasPermission(permission))
		{
			return true;
		} else {
			return false;
		}
	}
	public static boolean hasUnclaimedMessage(String uuid)
	{
		boolean returnVal = false;
		if (uuid != null) {
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
									Alert.DebugLog("Check", "hasUnclaimedMessage", "Unclaimed Message");
									returnVal = true;
								}
							}
						}
					}
				}
			}
		}
		return returnVal;
	}
	public static boolean hasUnclaimedGroup(String uuid)
	{
		boolean returnVal = false;
		List<UnclaimedGroup> lstG = Main.getInstance().GetUnclaimedGroup();
		if (lstG != null)
		{
			if (lstG.size() > 0)
			{
				for (UnclaimedGroup ug : lstG)
				{
					if (ug.getUUID().equalsIgnoreCase(uuid)) {
						Alert.DebugLog("Check", "hasUnclaimedMessage", "Unclaimed Group");
						returnVal = true;
					}
				}
			}
		}
		return returnVal;
	}
	public static boolean hasUnclaimedCash(String uuid)
	{
		boolean returnVal = false;
		List<UnclaimedCash> lstC = Main.getInstance().GetUnclaimedCash();
		if (lstC != null)
		{
			if (lstC.size() > 0)
			{
				for (UnclaimedCash uc : lstC)
				{
					if (uc.getUUID().equalsIgnoreCase(uuid)) {
						Alert.DebugLog("Check", "hasUnclaimedMessage", "Unclaimed Cash");
						returnVal = true;
					}
				}
			}
		}
		return returnVal;
	}
	public static boolean hasUnclaimedItem(String uuid)
	{
		boolean returnVal = false;
		List<UnclaimedItem> lstI = Main.getInstance().GetUnclaimedItem();
		if (lstI != null)
		{
			if (lstI.size() > 0)
			{
				for (UnclaimedItem ui : lstI)
				{
					if (ui.getUUID().equalsIgnoreCase(uuid)) {
						Alert.DebugLog("Check", "hasUnclaimedMessage", "Unclaimed Item");
						returnVal = true;
					}
				}
			}
		}
		return returnVal;
	}
	public static boolean hasUnclaimedSpawner(String uuid)
	{
		boolean returnVal = false;
		List<UnclaimedSpawner> lstS = Main.getInstance().GetUnclaimedSpawner();
		if (lstS != null)
		{
			if (lstS.size() > 0)
			{
				for (UnclaimedSpawner us : lstS)
				{
					if (us.getUUID().equalsIgnoreCase(uuid)) {
						Alert.DebugLog("Check", "hasUnclaimedMessage", "Unclaimed Spawner");
						returnVal = true;
					}
				}
			}
		}
		return returnVal;
	}
	public static boolean inGroup(Player player, String group)
	{
		boolean ret = false;
		Alert.DebugLog("Check", "inGroup", "Checking " + player.getDisplayName() + " - " + group);
		Permission perm = Main.getInstance().getPermission(); // Create a new Permission object
		String[] groups = perm.getPlayerGroups(null, player);
		for (String g : groups)
		{
			if (g.equalsIgnoreCase(group))
			{
				Alert.DebugLog("Check", "inGroup", "User is in group");
				ret = true;
			}
		}
		Alert.DebugLog("Check", "inGroup", "Check complete.  Result = " + Boolean.toString(ret));
		return ret;
	}
}
