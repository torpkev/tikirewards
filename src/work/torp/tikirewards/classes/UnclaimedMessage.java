package work.torp.tikirewards.classes;

public class UnclaimedMessage {
	int messageID;
	int rewardID;
	String uuid;
	String playerMsg;

    public int getMessageID() {
        return messageID;
    }
    public void setMessageID(int messageID) {
    	this.messageID = messageID;
    }
    public int getRewardID() {
        return rewardID;
    }
    public void setRewardID(int rewardID) {
    	this.rewardID = rewardID;
    }
    public String getUUID() {
    	return uuid;
    }
    public void setUUID(String uuid) {
    	this.uuid = uuid;
    }
    public String getPlayerMessage() {
    	return playerMsg;
    }
    public void setPlayerMessage(String playerMsg) {
    	this.playerMsg = playerMsg;
    }
}
