package work.torp.tikirewards.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class CreatureSpawnEvents  implements Listener {
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent evt) {
		if (evt.getSpawnReason() == SpawnReason.SPAWNER_EGG)
		{
			if (evt.getEntity() instanceof Entity)
			{
				Entity e = (Entity) evt.getEntity();
				e.setCustomName(null);
			}
		}
	}
}
