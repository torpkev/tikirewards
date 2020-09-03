package work.torp.tikirewards.classes;

import java.sql.Timestamp;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class PlacedSpawner {
	int placedSpawnerID;
	int spawnerID;
	EntityType entityType;
	String placedByUUID;
	Location placedLocation;
	Timestamp placedDateTime;
	
	public int getPlacedSpawnerID() {
		return placedSpawnerID;
	}
	public void setPlacedSpawnerID(int placedSpawnerID) {
		this.placedSpawnerID = placedSpawnerID;
	}
	public int getSpawnerID() {
		return spawnerID;
	}
	public void setSpawnerID(int spawnerID) {
		this.spawnerID = spawnerID;
	}
	public EntityType getEntityType() {
		return entityType;
	}
	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}
    public String getPlacedByUUID() {
    	return placedByUUID;
    }
    public void setPlacedByUUID(String placedByUUID) {
    	this.placedByUUID = placedByUUID;
    }
    public Location getPlacedLocation() {
    	return placedLocation;
    }
    public void setPlacedLocation(Location placedLocation) {
    	this.placedLocation = placedLocation;
    }
    public Timestamp getPlacedDateTime() {
    	return placedDateTime;
    }
    public void setPlacedDateTime(Timestamp placedDateTime) {
    	this.placedDateTime = placedDateTime;
    }
}
