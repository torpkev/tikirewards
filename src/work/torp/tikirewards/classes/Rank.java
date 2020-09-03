package work.torp.tikirewards.classes;

import java.util.List;

public class Rank {
	String internalName;
	String displayName;
	String playerMsg;
	String broadcastMsg;
	List<String> addGroups;
	boolean removeOtherGroups;
	List<String> keepGroups;
	int cash;
	List<Item> items;
	List<Spawner> spawners;
	
    public String getInternalName() {
        return internalName;
    }
    public void setInternalName(String internalName) {
    	this.internalName = internalName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
    	this.displayName = displayName;
    }
    public String getPlayerMessage() {
        return playerMsg;
    }
    public void setPlayerMessage(String playerMsg) {
    	this.playerMsg = playerMsg;
    }
    public String getBroadcastMessage() {
        return broadcastMsg;
    }
    public void setBroadcastMessage(String broadcastMsg) {
    	this.broadcastMsg = broadcastMsg;
    }
    public List<String> getAddGroups() {
        return addGroups;
    }
    public void setAddGroups(List<String> addGroups) {
    	this.addGroups = addGroups;
    }
    public boolean getRemoveOtherGroups() {
    	return removeOtherGroups;
    }
    public void setRemoveOtherGroups(boolean removeOtherGroups) {
    	this.removeOtherGroups  = removeOtherGroups;
    }
    public List<String> getKeepGroups() {
        return keepGroups;
    }
    public void setKeepGroups(List<String> keepGroups) {
    	this.keepGroups = keepGroups;
    }
    public int getCashAmount() {
        return cash;
    }
    public void setCashAmount(int cash) {
    	this.cash = cash;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
    	this.items = items;
    }
    public List<Spawner> getSpawners() {
        return spawners;
    }
    public void setSpawners(List<Spawner> spawners) {
    	this.spawners = spawners;
    }
}
