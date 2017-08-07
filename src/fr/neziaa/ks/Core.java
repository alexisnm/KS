package fr.neziaa.ks;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.neziaa.ks.commands.KSCommand;
import fr.neziaa.ks.data.Database;
import fr.neziaa.ks.listeners.PlayerDeath;
import fr.neziaa.ks.listeners.PlayerJoin;

public class Core extends JavaPlugin
{
    public void onEnable() {
        this.loadConfiguration();
        final String host = this.getConfig().getString("Database.Host");
        final String port = this.getConfig().getString("Database.Port");
        final String db = this.getConfig().getString("Database.Database");
        final String user = this.getConfig().getString("Database.Username");
        final String password = this.getConfig().getString("Database.Password");
        if (!Database.connect(host, port, db, user, password)) {
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
        }
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerDeath(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerJoin(this), (Plugin)this);
        this.getCommand("ks").setExecutor((CommandExecutor)new KSCommand(this));
    }
    
    public void loadConfiguration() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
}
