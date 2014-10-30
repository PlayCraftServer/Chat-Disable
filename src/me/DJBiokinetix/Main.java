package me.DJBiokinetix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		if (getConfig().get("chat") == null) {
			getConfig().addDefault("chat.status", Boolean.valueOf(false));
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}
  
	@Override
	public void onDisable() {
		saveDefaultConfig();
	}
  
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		Player p = (Player)sender;
		Location loc = p.getLocation();
		
		String msg1 = getConfig().getString("Enable").replaceAll("&", "§");
		String msg2 = getConfig().getString("Disable").replaceAll("&", "§");
		String fail1 = getConfig().getString("Fail 1").replaceAll("&", "§");
		String fail2 = getConfig().getString("Fail 2").replaceAll("&", "§");
		String prefix = getConfig().getString("Prefix").replaceAll("&", "§");
		String broadcast1 = getConfig().getString("Broadcast 1").replaceAll("&", "§").replaceAll("%user%", p.getName());
		String broadcast2 = getConfig().getString("Broadcast 2").replaceAll("&", "§").replaceAll("%user%", p.getName());
		
		if (label.equalsIgnoreCase("chat")){
			if (args.length == 0) {
				p.sendMessage(ChatColor.DARK_GRAY + "========================");
				p.sendMessage(ChatColor.GRAY + "Plugin by:" + " " + ChatColor.RED + "DJBiokinetix");
				p.sendMessage(ChatColor.GRAY + "Version:" + " " + ChatColor.AQUA + "1.0");
				p.sendMessage(ChatColor.GRAY + "Help:" + " " + ChatColor.RED + "/chat [enable/disable]");
				p.sendMessage(ChatColor.DARK_GRAY + "========================");
			} else if (args.length == 1){
				if (args[0].equalsIgnoreCase("enable")) {
					if (p.hasPermission("chat.enable")) {
						try {
							getConfig().set("chat.status", Boolean.valueOf(false));
							saveConfig();
							for (Player players : Bukkit.getOnlinePlayers()){
								players.playSound(loc, Sound.FIREWORK_LAUNCH, 10F, 0.1F);
							}
							p.setDisplayName(p.getName());
							p.getServer().broadcastMessage(ChatColor.RED + broadcast1);
							p.sendMessage(msg1);
						} catch (Exception e) {
							p.sendMessage(fail1);
						}
					} else {
						p.sendMessage(ChatColor.RED + "You don't have permissions for this!");
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("disable")) {
						if (p.hasPermission("chat.disable")) {
							try {
								getConfig().set("chat.status", Boolean.valueOf(true));
								saveConfig();
								for (Player players : Bukkit.getOnlinePlayers()){
									players.playSound(loc, Sound.ORB_PICKUP, 1F, 1F);
								}
								p.setDisplayName(prefix + " " + p.getName());
								p.getServer().broadcastMessage(ChatColor.RED + broadcast2);
								p.sendMessage(msg2);
							} catch (Exception e) {
								p.sendMessage(fail2);
							}
						} else {
							p.sendMessage(ChatColor.RED + "Yout don't have permissions for this!");
						}
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void NormalChat(AsyncPlayerChatEvent event) {
		
		String error = getConfig().getString("Error status").replaceAll("&", "§");
		Player p = event.getPlayer();
		
		if ((getConfig().getBoolean("chat.status")) && (!p.isOp())) {
			p.sendMessage(error);
			event.setCancelled(true);
		}
		
	}
}

