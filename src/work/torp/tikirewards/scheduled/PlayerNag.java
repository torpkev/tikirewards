package work.torp.tikirewards.scheduled;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import work.torp.tikirewards.Main;
import work.torp.tikirewards.alerts.Alert;
import work.torp.tikirewards.helper.Check;

public class PlayerNag {
	public static void Run() {
		Alert.DebugLog("PlayerNag", "run", "Nag Check!");
		if (Bukkit.getOnlinePlayers() != null)
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				String uuid = p.getUniqueId().toString();
				boolean hasUnclaimedGroup = false;
				boolean hasUnclaimedCash = false;
				boolean hasUnclaimedItem = false;
				boolean hasUnclaimedSpawner = false;
				if (Main.getInstance().HasVault())
				{
					if (Check.hasUnclaimedGroup(uuid))
					{
						hasUnclaimedGroup = true;
					}
					if (Check.hasUnclaimedCash(uuid))
					{
						hasUnclaimedCash = true;
					}
				}
				if (Check.hasUnclaimedItem(uuid))
				{
					hasUnclaimedItem = true;
				}
				if (Check.hasUnclaimedSpawner(uuid))
				{
					hasUnclaimedSpawner = true;
				}
				
				if (hasUnclaimedGroup || hasUnclaimedCash || hasUnclaimedItem || hasUnclaimedSpawner)
				{
					Alert.Player("You have unclaimed donor rewards.  Type " + ChatColor.AQUA + "/reward claim" + ChatColor.RESET + " to claim them!", p, true);
				}
			}
		}
	}
}
