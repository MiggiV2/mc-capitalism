package de.mymiggi.mc.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener
{
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		final Component component = MiniMessage.miniMessage().deserialize("Hello there! Welcome the the LAN-Party \\o/\nUse <i>/wizard</i> to explore the capitalism plugin.");
		player.sendMessage(component);
	}
}
