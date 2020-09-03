package work.torp.tikirewards.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import work.torp.tikirewards.Main;
import work.torp.tikirewards.alerts.Alert;
import work.torp.tikirewards.classes.Reward;
import work.torp.tikirewards.classes.PlacedSpawner;
import work.torp.tikirewards.classes.Rank;
import work.torp.tikirewards.classes.UnclaimedCash;
import work.torp.tikirewards.classes.UnclaimedGroup;
import work.torp.tikirewards.classes.UnclaimedItem;
import work.torp.tikirewards.classes.UnclaimedMessage;
import work.torp.tikirewards.classes.UnclaimedSpawner;
import work.torp.tikirewards.helper.Convert;
import work.torp.tikirewards.helper.Provide;

public abstract class Database {
    Main plugin;
    Connection connection;

    public String SQLConnectionExecute = "Couldn't execute SQL statement: ";
    public String SQLConnectionClose = "Failed to close SQL connection: "; 
    public String NoSQLConnection = "Unable to retreive SQL connection: ";
    public String NoTableFound = "Database Error: No Table Found";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    
    public Database(Main instance){
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM reward");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);
   
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, NoSQLConnection, ex);
        }
    }

    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
        	DatabaseError.close(plugin, ex);
        }
    }   
    
    public void getRewards() {
    	
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * from reward;");  
            rs = ps.executeQuery();
            
            
            while(rs.next()){
            	try
            	{
            		Reward d = new Reward();
            		d.setRewardID(Convert.IntegerFromString(rs.getString("reward_id")));
            		d.setUUID(rs.getString("uuid"));
            		d.setRank(rs.getString("rank"));
        			try {
                	    
                	    Date ftRewardDateTime = dateFormat.parse(rs.getString("reward_dtime"));
                	    Timestamp rewardDateTime = new java.sql.Timestamp(ftRewardDateTime.getTime());
                	    d.setRewardDateTime(rewardDateTime);
                	} catch(Exception e) {
                	    
                	}

                	Main.getInstance().AddReward(d);
            	}
            	catch (Exception ex)
            	{
            		Alert.VerboseLog("Database.getRewards()", "Unexpected error getting rewards from database: " + ex.getMessage());
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, SQLConnectionExecute, ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    }
    public void getUnclaimedCash() {
    	
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * from cash WHERE claimed = 0;");  
            rs = ps.executeQuery();
            
            
            while(rs.next()){
            	try
            	{
            		UnclaimedCash uc = new UnclaimedCash();
            		uc.setCashID(Convert.IntegerFromString(rs.getString("cash_id")));
            		uc.setRewardID(Convert.IntegerFromString(rs.getString("reward_id")));
            		uc.setUUID(rs.getString("uuid"));
            		uc.setCashAmount(Convert.IntegerFromString(rs.getString("amt")));

                	Main.getInstance().AddUnclaimedCash(uc);
            	}
            	catch (Exception ex)
            	{
            		Alert.VerboseLog("Database.getUnclaimedCash()", "Unexpected error getting unclaimed cash from database: " + ex.getMessage());
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, SQLConnectionExecute, ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    }
    public void getUnclaimedGroups() {
    	
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * from group_set WHERE claimed = 0;");  
            rs = ps.executeQuery();

            while(rs.next()){
            	try
            	{
            		UnclaimedGroup ug = new UnclaimedGroup();
            		ug.setGroupID(Convert.IntegerFromString(rs.getString("group_set_id")));
            		ug.setRewardID(Convert.IntegerFromString(rs.getString("reward_id")));
            		ug.setUUID(rs.getString("uuid"));

            		Rank r = Provide.rankByRewardID(ug.getRewardID());
            		if (r != null)
            		{
	            		ug.setGroupName(r.getAddGroups());
	            		ug.setKeepGroups(r.getKeepGroups());
	            		ug.setRemoveOtherGroups(r.getRemoveOtherGroups());
	                	Main.getInstance().AddUnclaimedGroup(ug);
            		}
            	}
            	catch (Exception ex)
            	{
            		Alert.VerboseLog("Database.getUnclaimedGroups()", "Unexpected error getting unclaimed groups from database: " + ex.getMessage());
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, SQLConnectionExecute, ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    }
    public void getUnclaimedItem() {
    	
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * from item WHERE claimed = 0;");  
            rs = ps.executeQuery();
            
            
            while(rs.next()){
            	try
            	{
            		UnclaimedItem ui = new UnclaimedItem();
            		Material m = Convert.StringToMaterial(rs.getString("item_name"));
            		boolean btag = false;
            		if (rs.getString("tag").equalsIgnoreCase("true"))
            		{
            			btag = true;
            		}
            		if (m != null)
            		{
            			String itemname = rs.getString("name");
            			if (itemname.equals("null"))
            			{
            				itemname = "";
            			}
            			ui.set(
            					Convert.IntegerFromString(rs.getString("item_id")), 
            					Convert.IntegerFromString(rs.getString("reward_id")), 
            					rs.getString("uuid"), 
            					itemname, 
            					m, 
            					Convert.IntegerFromString(rs.getString("item_cnt")), 
            					btag
            			);
                		Main.getInstance().AddUnclaimedItem(ui);
            		}
            	}
            	catch (Exception ex)
            	{
            		Alert.VerboseLog("Database.getUnclaimedItem()", "Unexpected error getting unclaimed cash from database: " + ex.getMessage());
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, SQLConnectionExecute, ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    }
    public void getUnclaimedMessage() {
    	
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * from player_message WHERE claimed = 0;");  
            rs = ps.executeQuery();
            
            
            while(rs.next()){
            	try
            	{
            		UnclaimedMessage um = new UnclaimedMessage();
            		um.setMessageID(Convert.IntegerFromString(rs.getString("player_message_id")));
            		um.setRewardID(Convert.IntegerFromString(rs.getString("reward_id")));
            		um.setUUID(rs.getString("uuid"));
            		um.setPlayerMessage(rs.getString("player_message"));

                	Main.getInstance().AddUnclaimedMessage(um);
            	}
            	catch (Exception ex)
            	{
            		Alert.VerboseLog("Database.getUnclaimedMessage()", "Unexpected error getting unclaimed message from database: " + ex.getMessage());
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, SQLConnectionExecute, ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    }
    public void getUnclaimedSpawner() {
    	
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * from spawner WHERE claimed = 0;");  
            rs = ps.executeQuery();

            while(rs.next()){
            	try
            	{
            		UnclaimedSpawner us = new UnclaimedSpawner();
            		EntityType et = Convert.StringToEntityType(rs.getString("entity_type"));
            		if (et != null)
            		{
                		us.set(Convert.IntegerFromString(rs.getString("reward_id")), Convert.IntegerFromString(rs.getString("reward_id")), rs.getString("uuid"), et, Convert.IntegerFromString(rs.getString("item_cnt")));
                    	Main.getInstance().AddUnclaimedSpawner(us);
            		}
            	}
            	catch (Exception ex)
            	{
            		Alert.VerboseLog("Database.getUnclaimedSpawner()", "Unexpected error getting unclaimed spawners from database: " + ex.getMessage());
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, SQLConnectionExecute, ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    }
    public void getPlacedSpawner() {
    	
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * from placed_spawner WHERE is_removed = 0;");  
            rs = ps.executeQuery();

            while(rs.next()){
            	try
            	{
            		Location loc = Convert.LocationFromXYZ(rs.getString("world"), Integer.parseInt(rs.getString("x")), Integer.parseInt(rs.getString("y")), Integer.parseInt(rs.getString("z")));
            		if (loc != null)
            		{
            			PlacedSpawner psp = new PlacedSpawner();
            			psp.setPlacedSpawnerID(Convert.IntegerFromString(rs.getString("placed_spawner_id")));
            			psp.setSpawnerID(Convert.IntegerFromString(rs.getString("spawner_id")));
            			psp.setEntityType(EntityType.valueOf(rs.getString("entity_type")));
            			psp.setPlacedByUUID(rs.getString("placed_by_uuid"));
            			psp.setPlacedLocation(loc);
            			try {
                    	    
                    	    Date ftPlacedDateTime = dateFormat.parse(rs.getString("placed_dtime"));
                    	    Timestamp donateDateTime = new java.sql.Timestamp(ftPlacedDateTime.getTime());
                    	    psp.setPlacedDateTime(donateDateTime);
                    	} catch(Exception e) {
                    	    
                    	}
            			
            			Main.getInstance().AddPlacedSpawner(psp);
            		}
            	}
            	catch (Exception ex)
            	{
            		Alert.VerboseLog("Database.getPlacedSpawner", "Unexpected error getting placed spawners from database: " + ex.getMessage());
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, SQLConnectionExecute, ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    }
    
    public int saveReward(String uuid, String rank) {
        int reward_id = -1;
    	Connection conn = null;
        PreparedStatement psDb = null;
        PreparedStatement psGetID = null;
        ResultSet rsGetID = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "INSERT INTO reward (reward_id, uuid, rank, reward_dtime) VALUES (" +
        			"NULL, " + // Inserting null as it is an auto-incrementing value
        			"'" + uuid + "', " + 
        			"'" + rank + "', " +
        			"'" + new Timestamp(System.currentTimeMillis()) + "'" +
        			");";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

        	psGetID = conn.prepareStatement("select MAX(reward_id) AS seq from reward;");  
            rsGetID = psGetID.executeQuery();
            while(rsGetID.next()){
            	reward_id =  Integer.parseInt(rsGetID.getString("seq"));
            }        
    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.saveReward", "Unexpected error saving reward");
    		Alert.DebugLog("Database", "saveReward", "Unexpected error saving reward: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (psGetID != null)
                    psGetID.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return reward_id;
    } 
    public int saveCash(int reward_id, String uuid, int amt) {
    	int cash_id = -1;
    	Connection conn = null;
        PreparedStatement psDb = null;
        PreparedStatement psGetID = null;
        ResultSet rsGetID = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "INSERT INTO cash (cash_id, reward_id, uuid, amt, claimed, claimed_by_uuid, claimed_dtime) VALUES (" +
        			"NULL, " + // Inserting null as it is an auto-incrementing value
        			Integer.toString(reward_id) + ", " +
        			"'" + uuid + "', " + 
        			Integer.toString(amt) + ", " +
        			"0, NULL, NULL" +
        			");";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

        	psGetID = conn.prepareStatement("select MAX(cash_id) AS seq from cash;");  
            rsGetID = psGetID.executeQuery();

            while(rsGetID.next()){
            	cash_id =  Integer.parseInt(rsGetID.getString("seq"));
            }  

    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.saveCash", "Unexpected error saving cash");
    		Alert.DebugLog("Database", "saveCash", "Unexpected error saving cash: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (psGetID != null)
                    psGetID.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return cash_id;
    } 
    public int saveGroup(int reward_id, String uuid) {
    	int group_id = -1;
    	boolean saveGroup = false;
    	
    	Rank r = Provide.rankByRewardID(reward_id);
    	if (r != null) {
    		if (r.getAddGroups() != null) {
    			if (r.getAddGroups().size() > 0) {
    				saveGroup = true;
    			}
    		}
    		if (r.getRemoveOtherGroups()) {
    			saveGroup = true;
    		}
    	}
    			
    	if (saveGroup) {
    		
            
        	Connection conn = null;
            PreparedStatement psDb = null;
            PreparedStatement psGetID = null;
            ResultSet rsGetID = null;
            String sql = "";
            try {
            	conn = getSQLConnection();

            	sql = "INSERT INTO group_set (group_set_id, reward_id, uuid, claimed, claimed_by_uuid, claimed_dtime) VALUES (" +
            			"NULL, " + // Inserting null as it is an auto-incrementing value
            			Integer.toString(reward_id) + ", " +
            			"'" + uuid + "', " + 
            			"0, NULL, NULL" +
            			");";

                psDb = conn.prepareStatement(sql);
                psDb.executeUpdate();

            	psGetID = conn.prepareStatement("select MAX(group_set_id) AS seq from group_set;");  
                rsGetID = psGetID.executeQuery();

                while(rsGetID.next()){
                	group_id =  Integer.parseInt(rsGetID.getString("seq"));
                }  

        	} catch (Exception ex) {
        		Alert.VerboseLog("Database.saveCash", "Unexpected error saving group");
        		Alert.DebugLog("Database", "saveCash", "Unexpected error saving group: " + ex.getMessage() + " - SQL: " + sql);
            } finally {
                try {
                    if (psDb != null)
                        psDb.close();
                    if (psGetID != null)
                        psGetID.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException ex) {
                    plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
                }
            }
    	}
    	
        return group_id;
    } 
    public int saveItem(int reward_id, String uuid, String name, String item_name, int item_cnt, boolean tag) {
    	int item_id = -1;
        
    	Connection conn = null;
        PreparedStatement psDb = null;
        PreparedStatement psGetID = null;
        ResultSet rsGetID = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "INSERT INTO item (item_id, reward_id, uuid, name, item_name, item_cnt, tag, claimed, claimed_by_uuid, claimed_dtime) VALUES (" +
        			"NULL, " + // Inserting null as it is an auto-incrementing value
        			Integer.toString(reward_id) + ", " +
        			"'" + uuid + "', " +
        			"'" + name + "', " + 
        			"'" + item_name + "', " + 
        			Integer.toString(item_cnt) + ", " +
        			"'" + Boolean.toString(tag) + "', " + 
        			"0, NULL, NULL" +
        			");";

        	Alert.DebugLog("Database", "saveItem", "SQL: " + sql);
            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

        	psGetID = conn.prepareStatement("select MAX(item_id) AS seq from item;");  
            rsGetID = psGetID.executeQuery();

            while(rsGetID.next()){
            	item_id =  Integer.parseInt(rsGetID.getString("seq"));
            }  

    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.saveItem", "Unexpected error saving item");
    		Alert.DebugLog("Database", "saveItem", "Unexpected error saving item: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (psGetID != null)
                    psGetID.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return item_id;
    } 
    public int saveMessage(int reward_id, String uuid, String player_message) {
    	int spawner_id = -1;
        
    	Connection conn = null;
        PreparedStatement psDb = null;
        PreparedStatement psGetID = null;
        ResultSet rsGetID = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "INSERT INTO player_message (player_message_id, reward_id, uuid, player_message, claimed, claimed_by_uuid, claimed_dtime) VALUES (" +
        			"NULL, " + // Inserting null as it is an auto-incrementing value
        			Integer.toString(reward_id) + ", " +
        			"'" + uuid + "', " +
        			"'" + player_message.replace("'",  "''") + "', " + 
        			"0, NULL, NULL" +
        			");";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

        	psGetID = conn.prepareStatement("select MAX(player_message_id) AS seq from player_message;");  
            rsGetID = psGetID.executeQuery();

            while(rsGetID.next()){
            	spawner_id =  Integer.parseInt(rsGetID.getString("seq"));
            }  

    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.saveMessage", "Unexpected error saving message");
    		Alert.DebugLog("Database", "saveMessage", "Unexpected error saving message: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (psGetID != null)
                    psGetID.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return spawner_id;
    } 
    public int saveSpawner(int reward_id, String uuid, String entity_type, int item_cnt) {
    	int spawner_id = -1;
        
    	Connection conn = null;
        PreparedStatement psDb = null;
        PreparedStatement psGetID = null;
        ResultSet rsGetID = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "INSERT INTO spawner (spawner_id, reward_id, uuid, entity_type, item_cnt, claimed, claimed_by_uuid, claimed_dtime) VALUES (" +
        			"NULL, " + // Inserting null as it is an auto-incrementing value
        			Integer.toString(reward_id) + ", " +
        			"'" + uuid + "', " +
        			"'" + entity_type + "', " + 
        			Integer.toString(item_cnt) + ", " +
        			"0, NULL, NULL" +
        			");";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

        	psGetID = conn.prepareStatement("select MAX(spawner_id) AS seq from spawner;");  
            rsGetID = psGetID.executeQuery();

            while(rsGetID.next()){
            	spawner_id =  Integer.parseInt(rsGetID.getString("seq"));
            }  

    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.saveSpawner", "Unexpected error saving spawner");
    		Alert.DebugLog("Database", "saveSpanwer", "Unexpected error saving spawner: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (psGetID != null)
                    psGetID.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return spawner_id;
    } 
    public int savePlacedSpawner(EntityType etype, int spawner_id, String uuid, Location spawner_loc) {
    	int placed_spawner_id = -1;
        
    	Connection conn = null;
        PreparedStatement psDb = null;
        PreparedStatement psGetID = null;
        ResultSet rsGetID = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "INSERT INTO placed_spawner (placed_spawner_id, spawner_id, entity_type, placed_by_uuid, world, x, y, z, placed_dtime, is_removed) VALUES (" +
        			"NULL, " + // Inserting null as it is an auto-incrementing value
        			Integer.toString(spawner_id) + ", " + 
        			"'" + etype.name() + "', " +
        			"'" + uuid + "', " +
        			"'" + spawner_loc.getWorld().getName() + "', " + 
        			Integer.toString(spawner_loc.getBlockX()) + ", " +
        			Integer.toString(spawner_loc.getBlockY()) + ", " +
        			Integer.toString(spawner_loc.getBlockZ()) + ", " +
        			"'" + new Timestamp(System.currentTimeMillis()) + "', " +
        			"0" +
        			");";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

        	psGetID = conn.prepareStatement("select MAX(placed_spawner_id) AS seq from placed_spawner;");  
            rsGetID = psGetID.executeQuery();

            while(rsGetID.next()){
            	placed_spawner_id =  Integer.parseInt(rsGetID.getString("seq"));
            }  

    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.savePlacedSpawner", "Unexpected error saving place_spawner");
    		Alert.DebugLog("Database", "savePlacedSpawner", "Unexpected error saving place_spawner: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (psGetID != null)
                    psGetID.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return placed_spawner_id;
    } 
    public int saveBrokenSpawner(EntityType etype, int spawner_id, String uuid, Location spawner_loc) {
    	int broken_spawner_id = -1;
        
    	Connection conn = null;
        PreparedStatement psDb = null;
        PreparedStatement psRem = null;
        PreparedStatement psGetID = null;
        ResultSet rsGetID = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "UPDATE placed_spawner SET is_removed = 1 WHERE entity_type = '" + etype.name() + "' AND x = " + Integer.toString(spawner_loc.getBlockX()) + " AND y = " + Integer.toString(spawner_loc.getBlockY()) + " AND z = " + Integer.toString(spawner_loc.getBlockZ()) + "; ";
            psRem = conn.prepareStatement(sql);
            psRem.executeUpdate();
            
        	sql = "INSERT INTO broken_spawner (broken_spawner_id, spawner_id, entity_type, broken_by_uuid, world, x, y, z, broken_dtime) VALUES (" +
        			"NULL, " + // Inserting null as it is an auto-incrementing value
        			Integer.toString(spawner_id) + ", " +
        			"'" + etype.name() + "', " +
        			"'" + uuid + "', " +
        			"'" + spawner_loc.getWorld().getName() + "', " + 
        			Integer.toString(spawner_loc.getBlockX()) + ", " +
        			Integer.toString(spawner_loc.getBlockY()) + ", " +
        			Integer.toString(spawner_loc.getBlockZ()) + ", " +
        			"'" + new Timestamp(System.currentTimeMillis()) + "'" +
        			");";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

        	psGetID = conn.prepareStatement("select MAX(placed_spawner_id) AS seq from placed_spawner;");  
            rsGetID = psGetID.executeQuery();

            while(rsGetID.next()){
            	broken_spawner_id =  Integer.parseInt(rsGetID.getString("seq"));
            }  

    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.saveBrokenSpawner", "Unexpected error saving broken_spawner");
    		Alert.DebugLog("Database", "saveBrokenSpawner", "Unexpected error saving broken_spawner: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psRem != null)
                    psRem.close();
                if (psDb != null)
                    psDb.close();
                if (psGetID != null)
                    psGetID.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return broken_spawner_id;
    }         
    public boolean claimCash(int cash_id, String uuid)
    {
    	boolean claimed = false;
    	Connection conn = null;
        PreparedStatement psDb = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "UPDATE cash SET claimed = 1, claimed_by_uuid = '" + uuid + "', claimed_dtime = '" + new Timestamp(System.currentTimeMillis()) + "' WHERE cash_id = " + Integer.toString(cash_id) + "; ";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

            claimed = true;
    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.claimCash", "Unexpected error claiming cash");
    		Alert.DebugLog("Database", "claimCash", "Unexpected error claiming cash: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return claimed;
    }
    public boolean claimGroups(int group_id, String uuid)
    {
    	boolean claimed = false;
    	Connection conn = null;
        PreparedStatement psDb = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "UPDATE group_set SET claimed = 1, claimed_by_uuid = '" + uuid + "', claimed_dtime = '" + new Timestamp(System.currentTimeMillis()) + "' WHERE group_set_id = " + Integer.toString(group_id) + "; ";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

            claimed = true;
    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.claimGroups", "Unexpected error claiming group");
    		Alert.DebugLog("Database", "claimGroups", "Unexpected error claiming group: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return claimed;
    }        
    public boolean claimItem(int item_id, String uuid)
    {
    	boolean claimed = false;
    	Connection conn = null;
        PreparedStatement psDb = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "UPDATE item SET claimed = 1, claimed_by_uuid = '" + uuid + "', claimed_dtime = '" + new Timestamp(System.currentTimeMillis()) + "' WHERE item_id = " + Integer.toString(item_id) + "; ";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

            claimed = true;
    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.claimItem", "Unexpected error claiming item");
    		Alert.DebugLog("Database", "claimItem", "Unexpected error claiming item: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return claimed;
    }
    public boolean claimMessage(int player_message_id, String uuid)
    {
    	boolean claimed = false;
    	Connection conn = null;
        PreparedStatement psDb = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "UPDATE player_message SET claimed = 1, claimed_by_uuid = '" + uuid + "', claimed_dtime = '" + new Timestamp(System.currentTimeMillis()) + "' WHERE player_message_id = " + Integer.toString(player_message_id) + "; ";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

            claimed = true;
    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.claimItem", "Unexpected error claiming message");
    		Alert.DebugLog("Database", "claimItem", "Unexpected error claiming message: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return claimed;
    }
    public boolean claimSpawner(int spawner_id, String uuid)
    {
    	boolean claimed = false;
    	Connection conn = null;
        PreparedStatement psDb = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	sql = "UPDATE spawner SET claimed = 1, claimed_by_uuid = '" + uuid + "', claimed_dtime = '" + new Timestamp(System.currentTimeMillis()) + "' WHERE spawner_id = " + Integer.toString(spawner_id) + "; ";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

            claimed = true;
    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.claimSpawner", "Unexpected error claiming spawner");
    		Alert.DebugLog("Database", "claimSpawner", "Unexpected error claiming spawner: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDb != null)
                    psDb.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
        return claimed;
    }    




}