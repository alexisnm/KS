package fr.neziaa.ks.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.neziaa.ks.Core;
import fr.neziaa.ks.data.Database;

import org.bukkit.event.Listener;

public class PlayerDeath implements Listener
{
    Core plugin;
    
    public PlayerDeath(final Core instance) {
        this.plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void death(final PlayerDeathEvent e) {
        if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {
            final Player p = e.getEntity();
            final Player k = e.getEntity().getKiller();
            Database.addDeath(p);
            Database.setKDR(p);
            Database.addKill(k);
            Database.setKDR(k);
        }
    }
}
