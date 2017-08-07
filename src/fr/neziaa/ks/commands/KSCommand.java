package fr.neziaa.ks.commands;

import org.bukkit.entity.Player;

import fr.neziaa.ks.Core;
import fr.neziaa.ks.data.Database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class KSCommand implements CommandExecutor
{
    Core plugin;
    
    public KSCommand(final Core instance) {
        this.plugin = instance;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("ks")) {
            final String noperm = ChatColor.RED + "Aucune permissions.";
            if (sender.hasPermission("ks.view")) {
                if (args.length >= 1) {
                     if (args[0].equals("classement")) {
                        if (sender.hasPermission("ks.classement")) {
                            if (sender instanceof Player) {
                                final Player p = (Player)sender;
                                Database.getTop(p);
                            }
                        }
                        else {
                            sender.sendMessage(noperm);
                        }
                    }
                     else if (args[0].equals("reset")) {
                         if (sender.hasPermission("ks.reset")) {
                     		if (sender instanceof Player) {
                     			final Player p = (Player)sender;
                     				if(Database.getResetPoints(p) < 2){
                                         Database.resetPlayer(p);
                                         sender.sendMessage("§5Votre Ks à bien été reset !");
                                         Database.addResetPoints(p);
                                         sender.sendMessage("§7Vous avez reset votre KS : §5"+ Database.getResetPoints(p) +" §7fois");
                                     }
                     				else {
                     					sender.sendMessage("§cVous avez reset votre KS trop de fois. ");
                     				}
                     			}
                            } 
                        else {
                            sender.sendMessage(noperm);
                        }
                    }
                    else if (sender.hasPermission("ks.status.others")) {
                        if (this.plugin.getConfig().getBoolean("Logging." + args[0].toLowerCase())) {
                            final Player p = Bukkit.getPlayer(args[0]);
                            sender.sendMessage("§7§m------------§r §5Statistiques §7-§5 " + args[0] + " §7§m------------");
                            sender.sendMessage("§8» §7Nombre de Kills : §5"+ Database.getKills(p));
                            sender.sendMessage("§8» §7Nombre de Morts : §5"+ Database.getDeaths(p));
                            sender.sendMessage("§8» §7Ratio : §5"+ Database.getKDR(p));
                            sender.sendMessage("§7§m--------------------------------------------");
                            }
                        else {
                            sender.sendMessage(ChatColor.RED + "§5Joueurs inconnus.");
                        }
                    }
                    else {
                        sender.sendMessage(noperm);
                    }
                     
                }
                else if (sender instanceof Player) {
                    if (sender.hasPermission("ks.status")) {
                        final Player p = (Player)sender;
                        sender.sendMessage("§7§m----------------§r §5Vos Statistiques §7§m----------------");
                        sender.sendMessage("§8» §7Votre nombre de Kills : §5"+ Database.getKills(p));
                        sender.sendMessage("§8» §7Votre nombre de Morts : §5"+ Database.getDeaths(p));
                        sender.sendMessage("§8» §7Votre Ratio : §5"+ Database.getKDR(p));
                        sender.sendMessage("§7§m-----------------------------------------------");
                    }
                    else {
                        sender.sendMessage(noperm);
                    }
                }
            }
            else {
                sender.sendMessage("§cPermissions insuffisantes");
            }
        }
        return true;
    }
}
