package fr.neziaa.ks.data;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.neziaa.ks.Core;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

public class Database
{
    static Connection con;
    static Statement st;
    static ResultSet rs;
    static Core plugin;
    
    static {
        Database.con = null;
        Database.st = null;
        Database.rs = null;
    }
    
    public Database(final Core instance) {
        Database.plugin = instance;
    }
    
    public static boolean connect(final String host, final String port, final String db, final String user, final String password) {
        final String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        try {
            Database.con = DriverManager.getConnection(url, user, password);
            Database.st = Database.con.createStatement();
            try {
                final Statement st = Database.con.createStatement();
                final String table = "CREATE TABLE ks (id INTEGER NOT NULL AUTO_INCREMENT,  user VARCHAR(255),  kills INTEGER(255),  deaths INTEGER(255),  ks INTEGER(255),  reset INTEGER(255),   PRIMARY KEY ( id ))";
                st.executeUpdate(table);
                return true;
            }
            catch (SQLException s) {
                return true;
            }
        }
        catch (SQLException e) {
            System.out.println("Impossible de se connecter a la database");
            return false;
        }
    }
    
    public static void getTop(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT * FROM ks ORDER BY ks DESC LIMIT 10");
            int num = 1;
            p.sendMessage("§7§m--------------§r §5Classement KS §7§m--------------");
            while (Database.rs.next()) {
                p.sendMessage(new StringBuilder()
                		.append(ChatColor.DARK_AQUA)
                		.append(num)
                		.append(" §8-§3 ")
                		.append(Database.rs.getString(2))
                		.append(" §8» §3")
                		.append(Database.rs.getString(5))
                		.append(" §8[")
                		.append(Database.rs.getString(3))
                		.append("§8/§7")
                		.append(Database.rs.getString(4))
                		.append("§8]")
                		.toString());
                ++num;
            }
            p.sendMessage("§7§m-----------------------------------------");
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void addPlayer(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT * FROM ks WHERE user = '" + p.getName() + "'");
            if (!Database.rs.next()) {
                Database.st.executeUpdate("INSERT INTO ks (id, user, kills, deaths, ks, reset) VALUES (NULL, '" + p.getName() + "', 0, 0, 0,0)");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void resetPlayer(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT * FROM ks WHERE user = '" + p.getName() + "'");
            if (Database.rs.next()) {
                Database.st.executeUpdate("UPDATE ks SET kills='0' WHERE user = '" + p.getName() + "'");
                Database.st.executeUpdate("UPDATE ks SET deaths='0' WHERE user = '" + p.getName() + "'");
                Database.st.executeUpdate("UPDATE ks SET kdr='0.0' WHERE user = '" + p.getName() + "'");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void addKill(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT kills FROM ks WHERE user = '" + p.getName() + "'");
            if (Database.rs.next()) {
                final int x = Integer.parseInt(Database.rs.getString(1));
                final int y = x + 1;
                Database.st.executeUpdate("UPDATE ks SET kills='" + y + "' WHERE user = '" + p.getName() + "'");
            }
            else {
                Database.st.executeUpdate("INSERT INTO ks (id, user, kills, deaths, ks, reset) VALUES (NULL, '" + p.getName() + "', 1, 0, 0, 0)");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void addDeath(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT deaths FROM ks WHERE user = '" + p.getName() + "'");
            if (Database.rs.next()) {
                final int x = Integer.parseInt(Database.rs.getString(1));
                final int y = x + 1;
                Database.st.executeUpdate("UPDATE ks SET deaths='" + y + "' WHERE user = '" + p.getName() + "'");
            }
            else {
                Database.st.executeUpdate("INSERT INTO ks (id, user, kills, deaths, ks) VALUES (NULL, '" + p.getName() + "', 0, 1, 0, 0)");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void addResetPoints(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT reset FROM ks WHERE user = '" + p.getName() + "'");
            if (Database.rs.next()) {
                final int x = Integer.parseInt(Database.rs.getString(1));
                final int y = x + 1;
                Database.st.executeUpdate("UPDATE ks SET reset='" + y + "' WHERE user = '" + p.getName() + "'");
            }
            else {
                Database.st.executeUpdate("INSERT INTO ks (id, user, kills, deaths, ks, reset) VALUES (NULL, '" + p.getName() + "', 0, 0, 0, 1)");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void setKDR(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT ks FROM ks WHERE user = '" + p.getName() + "'");
            if (Database.rs.next()) {
                final double x = Integer.parseInt(getKills(p));
                final double y = Integer.parseInt(getDeaths(p));
                double z = x / y;
                if (y == 0.0) {
                    z = x;
                }
                else if (x == 0.0) {
                    z = 0.0;
                }
                final int r = (int)Math.round(z * 100.0);
                final double f = r / 100.0;
                p.sendMessage(new StringBuilder(String.valueOf(f)).toString());
                Database.st.executeUpdate("UPDATE ks SET ks='" + f + "' WHERE user = '" + p.getName() + "'");
            }
            else {
                p.sendMessage(ChatColor.RED + "ERROR");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static String getKills(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT kills FROM ks WHERE user = '" + p.getName() + "'");
            if (Database.rs.next()) {
                return Database.rs.getString(1);
            }
            return "0";
        }
        catch (SQLException ex) {
            return "0";
        }
    }
    
    public static String getDeaths(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT deaths FROM ks WHERE user = '" + p.getName() + "'");
            if (Database.rs.next()) {
                return Database.rs.getString(1);
            }
            return "0";
        }
        catch (SQLException ex) {
            return "0";
        }
    }

    public static int getResetPoints(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT reset FROM ks WHERE user = '" + p.getName() + "'");
            if (Database.rs.next()) {
                return Database.rs.getInt(1);
            }
            return 0;
        }
        catch (SQLException ex) {
            return 0;
        }
    }
    
    public static String getKDR(final Player p) {
        try {
            Database.rs = Database.st.executeQuery("SELECT ks FROM ks WHERE user = '" + p.getName() + "'");
            if (Database.rs.next()) {
                return Database.rs.getString(1);
            }
            return "0";
        }
        catch (SQLException ex) {
            return "0";
        }
    }
}
