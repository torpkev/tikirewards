package work.torp.tikirewards.classes;

import java.sql.Timestamp;

public class Reward {
	int rewardID;
	String uuid;
	String rank;
	Timestamp rewardDateTime;
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
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank.toLowerCase();
	}
    public Timestamp getRewardDateTime() {
    	return rewardDateTime;
    }
    public void setRewardDateTime(Timestamp rewardDateTime) {
    	this.rewardDateTime = rewardDateTime;
    }
}
