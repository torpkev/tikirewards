package work.torp.tikirewards.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import work.torp.tikirewards.Main;

public class SQLite extends Database{
    String dbname;
    public SQLite(Main instance){
        super(instance);
        dbname = plugin.getConfig().getString("SQLite.Filename", "TikiRewards"); 
    }
   
    public String cash = "CREATE TABLE IF NOT EXISTS cash (" + 
    		"`cash_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
    		"`reward_id` INTEGER NOT NULL, " +
    		"`uuid`	varchar(50) NOT NULL, " +
    		"`amt` INTEGER NOT NULL, " +
    		"`claimed` boolean NOT NULL, " +
    		"`claimed_by_uuid` varchar(50), " +
    		"`claimed_dtime` timestamp " +
    		");";

    public String reward = "CREATE TABLE IF NOT EXISTS reward (" + 
    		"`reward_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
    		"`uuid`	varchar(50) NOT NULL, " +
    		"`rank`	varchar(250) NOT NULL, " +
    		"`reward_dtime` timestamp NOT NULL " +
    		");"; 
    
    public String group = "CREATE TABLE IF NOT EXISTS group_set (" + 
    		"`group_set_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
    		"`reward_id` INTEGER NOT NULL, " +
    		"`uuid` varchar(50) NOT NULL, " +
    		"`claimed` boolean NOT NULL, " +
    		"`claimed_by_uuid` varchar(50), " +
    		"`claimed_dtime` timestamp " +
    		");"; 

    public String item = "CREATE TABLE IF NOT EXISTS item (" + 
    		"`item_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
    		"`reward_id` INTEGER NOT NULL, " +
    		"`uuid`	varchar(50) NOT NULL, " +
    		"`name` varchar(250) NOT NULL, " +
    		"`item_name` varchar(250) NOT NULL, " +
    		"`item_cnt`	INTEGER NOT NULL, " +
    		"`tag` boolean NOT NULL, " +
    		"`claimed` boolean NOT NULL, " +
    		"`claimed_by_uuid` varchar(50), " +
    		"`claimed_dtime` timestamp " +
    		");"; 
    
    public String msg = "CREATE TABLE IF NOT EXISTS player_message (" + 
    		"`player_message_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
    		"`reward_id` INTEGER NOT NULL, " +
    		"`uuid`	varchar(50) NOT NULL, " +
    		"`player_message` TEXT NOT NULL, " +
    		"`claimed` boolean NOT NULL, " +
    		"`claimed_by_uuid` varchar(50), " +
    		"`claimed_dtime` timestamp " +
    		");";
    
    public String spawner = "CREATE TABLE IF NOT EXISTS spawner (" + 
    		"`spawner_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
    		"`reward_id` INTEGER NOT NULL, " +
    		"`uuid`	varchar(50) NOT NULL, " +
    		"`entity_type` varchar(250) NOT NULL, " +
    		"`item_cnt`	INTEGER NOT NULL, " +
    		"`claimed` boolean NOT NULL, " +
    		"`claimed_by_uuid` varchar(50), " +
    		"`claimed_dtime` timestamp " +
    		");";
 
    public String broken_spawner = "CREATE TABLE IF NOT EXISTS broken_spawner (" + 
    		"`broken_spawner_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
    		"`spawner_id` INTEGER NOT NULL, " +
    		"`entity_type` VARCHAR(100) NOT NULL, " +
    		"`broken_by_uuid` varchar(50) NOT NULL, " +
			"`world` varchar (100) NOT NULL, " +
			"`x` INTEGER NOT NULL, " +
			"`y` INTEGER NOT NULL, " +
			"`z` INTEGER NOT NULL, " +
			"`broken_dtime`	timestamp NOT NULL " +
			");";
    
    public String placed_spawner = "CREATE TABLE IF NOT EXISTS placed_spawner (" + 
    		"`placed_spawner_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
    		"`spawner_id` INTEGER NOT NULL, " +
    		"`entity_type` VARCHAR(100) NOT NULL, " +
    		"`placed_by_uuid` varchar(50) NOT NULL, " +
    		"`world` varchar(100) NOT NULL, " +
    		"`x` INTEGER NOT NULL, " +
    		"`y` INTEGER NOT NULL, " +
    		"`z` INTEGER NOT NULL, " +
    		"`placed_dtime`	timestamp NOT NULL, " +
    		"`is_removed` boolean NOT NULL " +
    		");";

    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(broken_spawner);
            s.executeUpdate(cash);
            s.executeUpdate(reward);
            s.executeUpdate(group);
            s.executeUpdate(item);
            s.executeUpdate(msg);
            s.executeUpdate(placed_spawner);
            s.executeUpdate(spawner);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}
 