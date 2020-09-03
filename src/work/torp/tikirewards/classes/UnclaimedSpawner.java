package work.torp.tikirewards.classes;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import work.torp.tikirewards.helper.Provide;

public class UnclaimedSpawner {
	int spawnerID;
	int rewardID;
	String uuid;
	EntityType entityType;
	int spawnerCnt;
    public int getSpawnerID() {
        return spawnerID;
    }
    public int getRewardID() {
        return rewardID;
    }
    public String getUUID() {
    	return uuid;
    }
    public EntityType getEntityType() {
        return entityType;
    }
    public int getSpawnerCount() {
        return spawnerCnt;
    }
    public void set(int spawnerID, int rewardID, String uuid, EntityType entityType, int spawnerCnt)
    {
    	this.spawnerID = spawnerID;
    	this.rewardID = rewardID;
    	this.uuid = uuid;
    	this.entityType = entityType;
    	this.spawnerCnt = spawnerCnt;
    }
    public ItemStack getSpawner(Player player) {
    	Rank r = Provide.rankByRewardID(this.rewardID);
    	ItemStack istack = Provide.getSpawner(player, this.entityType, this.spawnerID, this.spawnerCnt, true, true, r);
    	return istack;
    }
}
