package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Bank;
import de.mymiggi.mc.money.Shop;
import de.mymiggi.mc.util.SellBuyArgParser;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

abstract public class AbstractMaterialCommand implements CommandExecutor
{
	protected final Shop shop;

	protected final Bank bank;

	public AbstractMaterialCommand(Shop shop, Bank bank)
	{
		this.shop = shop;
		this.bank = bank;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
	{
		if (sender instanceof Player player)
		{
			if (!shop.isEnabled())
			{
				shop.sendMessage(player, "The store is currently disabled. Please come back later.");
				return true;
			}
			if (args.length != 2)
			{
				return false;
			}

			// Parse material
			Optional<Material> materialInput = shop.getMaterialByName(args[1]);
			if (materialInput.isEmpty())
			{
				player.sendMessage("No block found for " + args[1]);
				return false;
			}
			Material material = materialInput.get();

			// Parse how many items
			SellBuyArgParser parser = new SellBuyArgParser();
			int howMany = parser.parseOrOne(args[0]);

			// overflow prevention
			int maxInventorySpace = 2560;
			if (howMany > maxInventorySpace || howMany < 0)
			{
				player.sendMessage("Only numbers between 0 and " + maxInventorySpace + " are allowed!");
				return true;
			}

			return runCommand(player, material, howMany);
		}
		return true;
	}

	abstract protected boolean runCommand(Player player, Material material, int howMany);
}
