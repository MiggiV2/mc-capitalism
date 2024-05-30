package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Shop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandStore implements CommandExecutor
{
	private final Shop shop;

	public CommandStore(Shop shop)
	{
		this.shop = shop;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
	{
		if (sender instanceof Player player)
		{
			if (args.length == 1 && player.isOp())
			{
				boolean enableShop = args[0].equalsIgnoreCase("enable");
				shop.setEnabled(enableShop);
			}
			StringBuilder message = new StringBuilder("Inventory\n");
			List<Material> sortedMaterials = shop.listMaterials().stream()
				.sorted((o1, o2) -> Integer.compare(shop.prizeForBuying(o1), shop.prizeForBuying(o2)))
				.toList();
			// build message
			for (Material material : sortedMaterials)
			{
				String line = String.format("- %s %d€\n", shop.getAliasOrDefaultName(material), shop.prizeForBuying(material));
				message.append(line);
			}
			// explain fee
			String feeExplained = String.format("Tip: There is also a fee (%d%%) for buy things!\nExample if you sell 1x gold"
					+ " you get %d€. If you buy 1x gold you pay %d€.", shop.getFeeInPercent(), shop.moneyForSelling(Material.GOLD_INGOT),
				shop.prizeForBuying(Material.GOLD_INGOT));
			message.append(feeExplained);
			// send message
			shop.sendMessage((Player)sender, message.toString());

			if (!shop.isEnabled())
			{
				Component disabledHint = Component.text("The store is currently disabled...").color(NamedTextColor.RED);
				player.sendMessage(disabledHint);
			}
		}
		return true;
	}
}
