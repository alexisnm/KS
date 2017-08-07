package fr.neziaa.ks.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.neziaa.ks.Core;
import fr.neziaa.ks.data.Database;

import org.bukkit.event.Listener;

public class PlayerJoin implements Listener
{
    Core plugin;
    
    public PlayerJoin(final Core instance) {
        this.plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void join(final PlayerJoinEvent e) {
        this.plugin.getConfig().set("Logging." + e.getPlayer().getName().toLowerCase(), (Object)true);
        this.plugin.saveConfig();
        Database.addPlayer(e.getPlayer());
    }
}
