package de.mymiggi.mc.events;

import de.mymiggi.mc.commands.CommandBuy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.Plugin;

public class SleepListener implements Listener
{
	private static int playersInBed = 0;
	private final Server server;
	private final Plugin plugin;
	private final CommandBuy commandBuy;
	private static final String[] GOOD_MORNING_MESSAGES = {
		"Good morning, Crafters! Hope you’re refreshed and ready to mine some diamonds! Let’s make those pickaxes swing today at the LAN-party!",
		"Rise and shine, fellow builders! Another adventure-filled day awaits us in the world of blocks. Let's light up this LAN-party with our crafting skills!",
		"Morning everyone! Who’s ready to explore more caves and conquer some mobs today? Let’s make the LAN-party legendary with epic builds and survival strategies!",
		"Hey team, grab your potions and notch your armor, it's another bright day in Minecraft! Let's dive into this LAN-party and show what true crafters are made of!",
		"Top of the morning, Minecraft warriors! Gear up, because today we raid more than just villages – we take on the whole LAN! Let’s gooooo!",
		"Good morning, pixel pals! Let’s turn this day into a masterpiece of craft and creativity at the LAN-party. Keep those pixels popping!",
		"Welcome to another sunny day in our favorite blocky universe! Let’s use our imaginations and make some magic happen at the LAN-party today!",
		"Hello, Minecraft masters! As the sun rises over our blocky horizons, let’s gather our resources and build some unforgettable memories at the LAN-party.",
		"Good morning, adventurers! Ready your tools and refill your energy; today we shape worlds and craft dreams together at the LAN-party. Let’s break new ground!",
		"Wake up, builders and fighters! The world of Minecraft awaits us with new quests and challenges. Let’s grab our gear and make this LAN-party one for the history books!",
		"Morning, miners! Grab your pickaxes, and let's start the day with some sparkling ores!",
		"Rise and shine, builders! A new day means endless possibilities for constructions. Let’s build something great today!",
		"Good morning, adventurers! Ready to tackle the wild biomes and uncover hidden treasures today?",
		"Hey, redstone engineers! Power up your day with some clever circuits at our LAN-party. Let the tinkering begin!",
		"Wakey wakey, dragon slayers! Let’s band together and head to The End for another epic showdown.",
		"Top of the morning, farmers! Let’s sow some seeds of fun and harvest joy at today’s LAN-party.",
		"Goooood morning, pixel pioneers! Ready to lay down some blocks and defy the laws of physics together?",
		"Ahoy, sailors! Set your compass to adventure and let’s navigate the vast oceans in search of new lands!",
		"Beam up, Nether travelers! Prepare your fire potions; we’re diving into heat to forge alliances with Pigmen.",
		"Good day, dungeon raiders! Arm yourselves, for today we plunge into the darkness to claim our glory.",
		"Sunrise in Minecraft means more sunshine for our crops and more energy for us! Let’s get crafting.",
		"Mornin' crafters! Stack up your resources, because today, we're building dreams block by block.",
		"Hey, troop! Let’s hatch some chickens, shear some sheep, and dig some dirt. It’s all in a day's play at our LAN-party!",
		"Good morning, Minecraft maniacs! Let the pixels refresh your spirit as we embark on today’s blocky adventures.",
		"Let’s brighten up this world with our creativity. Good morning, everyone, let's make every block count today!",
		"Wake up with enthusiasm, fellow gamers! Our cuboid world awaits our grand designs and daring exploits.",
		"Morning, enchanters! Time to imbue our tools with magic and make this LAN party spellbinding!",
		"Hello to the architects of imagination! Let’s craft a day so fantastic, it feels like it’s spawned from a dream!",
		"Salutations, survivors! Equip your armor, ready your weapons, and let’s outlast the creepers!",
		"Good morning, cobblestone kids! Let’s break ground on new projects and carve out our legend today in Minecraft!"
	}; // generated by gpt-4-turbo

	public SleepListener(Server server, Plugin plugin, CommandBuy commandBuy)
	{
		this.server = server;
		this.plugin = plugin;
		this.commandBuy = commandBuy;
	}

	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent event)
	{
		playersInBed += 1;
		int totalPlayers = server.getOnlinePlayers().size();
		if ((double)playersInBed >= (double)totalPlayers / 2)
		{
			server.getScheduler().scheduleSyncDelayedTask(plugin, this::setTimeAndMessage, 35);
		}
	}

	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent event)
	{
		playersInBed -= 1;
	}

	private void setTimeAndMessage()
	{
		World world = server.getWorlds().getFirst();
		if (world.getTime() >= 13000)
		{
			world.setTime(0);
			int randomIndex = (int)(Math.random() * GOOD_MORNING_MESSAGES.length);
			String randomMessage = GOOD_MORNING_MESSAGES[randomIndex];
			Component component = Component.text(randomMessage).color(NamedTextColor.GREEN);
			server.broadcast(component);
		}
	}
}