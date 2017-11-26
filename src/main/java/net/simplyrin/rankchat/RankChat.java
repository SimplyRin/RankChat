package net.simplyrin.rankchat;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RankChat extends JavaPlugin implements Listener {

	private static RankChat plugin;

	/**
	 * @category Permission to Tag
	 */
	private static HashMap<String, String> list = new HashMap<String, String>();

	@Override
	public void onEnable() {
		plugin = this;
		plugin.getServer().getPluginManager().registerEvents(this, this);
	}

	private void loadConfigFile() {
		for(String key : plugin.getConfig().getConfigurationSection("Ranks").getKeys(false)) {
			list.put(plugin.getConfig().getString("Ranks." + key + ".Permission"), plugin.getConfig().getString("Ranks." + key + ".Format"));
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();

		for(String msg : list.values()) {
			if(player.hasPermission(msg)) {
				event.setCancelled(true);

				String format = list.get(msg);

				format = format.replace("%prefix", PermissionsEx.getUser(player).getPrefix());
				format = format.replace("%suffix", PermissionsEx.getUser(player).getSuffix());

				format = format.replace("%player", player.getName());

				plugin.getServer().broadcastMessage(format);
			}
		}
	}

}
