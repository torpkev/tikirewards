package work.torp.tikirewards.classes;

import org.bukkit.entity.EntityType;

public class Spawner {
	EntityType entityType;
	int spawnerCnt;
    public EntityType getEntityType() {
        return entityType;
    }
    public void setEntityType(EntityType entityType) {
    	this.entityType = entityType;
    }
    public int getSpawnerCount() {
        return spawnerCnt;
    }
    public void setSpawnerCount(int spawnerCnt) {
    	this.spawnerCnt = spawnerCnt;
    }
}
