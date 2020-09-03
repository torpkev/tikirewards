package work.torp.tikirewards.classes;

public class UnclaimedCash {
	int cashID;
	int rewardID;
	String uuid;
	int amt;

    public int getCashID() {
        return cashID;
    }
    public void setCashID(int cashID) {
    	this.cashID = cashID;
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
    public int getCashAmount() {
        return amt;
    }
    public void setCashAmount(int amt) {
    	this.amt = amt;
    }
    
}
