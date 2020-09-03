package work.torp.tikirewards.classes;

import org.bukkit.Material;

public class Item {
	String name;
	Material material;
	int itemCnt;
	boolean tagItem;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
    	this.material = material;
    }
    public int getItemCount() {
        return itemCnt;
    }
    public void setItemCount(int itemCnt) {
    	this.itemCnt = itemCnt;
    }
    public boolean getTagItem() {
    	return tagItem;
    }
    public void setTagItem(boolean tagItem) {
    	this.tagItem = tagItem;
    }
}
