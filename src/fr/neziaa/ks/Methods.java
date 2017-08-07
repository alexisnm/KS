package fr.neziaa.ks;

import java.text.DecimalFormat;
import org.bukkit.entity.Player;

public class Methods
{
    static Core plugin;
    
    public Methods(final Core instance) {
        Methods.plugin = instance;
    }
    
    public static double getKills(final Player p) {
        final double kills = Methods.plugin.getConfig().getInt(String.valueOf(p.getName()) + ".Kills");
        return kills;
    }
    
    public static double getDeaths(final Player p) {
        final double deaths = Methods.plugin.getConfig().getInt(String.valueOf(p.getName()) + ".Deaths");
        return deaths;
    }
    
    public static double getResetPoints(final Player p) {
        final double resetpoints = Methods.plugin.getConfig().getInt(String.valueOf(p.getName()) + ".ResetPoints");
        return resetpoints;
    }
    
    public static String getKDR(final Player p) {
        final double kills = Methods.plugin.getConfig().getInt(String.valueOf(p.getName()) + ".Kills");
        final double deaths = Methods.plugin.getConfig().getInt(String.valueOf(p.getName()) + ".Deaths");
        double kdr = kills / deaths;
        final DecimalFormat df = new DecimalFormat("#.##");
        df.format(kdr);
        if (kdr == 0.0) {
            kdr = kills;
        }
        return df.format(kdr);
    }
}
