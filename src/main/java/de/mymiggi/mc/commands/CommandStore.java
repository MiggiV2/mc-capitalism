package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Shop;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandStore implements CommandExecutor
{
	private final Shop shop = new Shop();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
	{
		if (sender instanceof Player)
		{
			StringBuilder message = new StringBuilder("Inventory\n");
			List<Material> sortedMaterials = shop.listMaterials().stream()
				.sorted((o1, o2) -> Integer.compare(shop.prizeForBuying(o1), shop.prizeForBuying(o2)))
				.toList();
			for (Material material : sortedMaterials)
			{
				String line = String.format("- %s %d€\n", shop.getAliasOrDefaultName(material), shop.prizeForBuying(material));
				message.append(line);
			}
			shop.sendMessage((Player)sender, message.toString());
		}
		return true;
	}
}
