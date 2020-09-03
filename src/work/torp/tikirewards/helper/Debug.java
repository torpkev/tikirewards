package work.torp.tikirewards.helper;

import java.util.Map;

import work.torp.tikirewards.Main;
import work.torp.tikirewards.alerts.Alert;
import work.torp.tikirewards.classes.Item;
import work.torp.tikirewards.classes.Rank;
import work.torp.tikirewards.classes.Spawner;

public class Debug {
	public static void AlertConfig()
	{
		for (Map.Entry<String, Rank> entry : Main.hmRanks.entrySet()) {
		    String key = entry.getKey();
		    Rank value = entry.getValue();
		    Alert.Log("Main", "Looping through " + key);
		    Alert.Log(key, "Internal Name: " + value.getInternalName());
		    Alert.Log(key, "Display Name: " + value.getDisplayName());
		    Alert.Log(key, "Player Message: " + value.getPlayerMessage());
		    Alert.Log(key, "Broadcast Message: " + value.getBroadcastMessage());
		    Alert.Log(key, "Add Groups:");
		    if (value.getAddGroups() != null)
		    {
			    for (String add : value.getAddGroups())
			    {
			    	Alert.Log(key + ".AddGroup", add);
			    }
		    } 
		    Alert.Log(key, "Remove Other Groups: " + Boolean.toString(value.getRemoveOtherGroups()));
		    Alert.Log(key, "Keep Groups:");
		    if (value.getKeepGroups() != null)
		    {
			    for (String keep : value.getKeepGroups())
			    {
			    	Alert.Log(key + ".KeepGroup", keep);
			    }
		    }
		    Alert.Log(key, "Cash: " + Integer.toString(value.getCashAmount()));
		    Alert.Log(key, "Item:");
		    if (value.getItems() != null)
		    {
			    for (Item item : value.getItems())
			    {
			    	Alert.Log(key + ".Items", item.getMaterial().name() + " x" + Integer.toString(item.getItemCount()) + " - Tag: " + Boolean.toString(item.getTagItem()));
			    }
		    }
		    Alert.Log(key, "Spawner:");
		    if (value.getSpawners() != null)
		    {
			    for (Spawner spawner : value.getSpawners())
			    {
			    	Alert.Log(key + ".Spawners", spawner.getEntityType().name() + " x" + Integer.toString(spawner.getSpawnerCount()));
			    }
		    }			    
		}
	}
}
