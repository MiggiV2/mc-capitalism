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
	protected final Shop shop = new Shop();

	protected final Bank bank;

	public AbstractMaterialCommand(Bank bank)
	{
		this.bank = bank;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
	{
		if (sender instanceof Player)
		{
			if (args.length != 2)
			{
				return false;
			}

			Player player = (Player)sender;
			// Parse material
			Optional<Material> materialInput = shop.getMaterialByName(args[1]);
			if (!materialInput.isPresent())
			{
				player.sendMessage("No block found for " + args[1]);
				return false;
			}
			Material material = materialInput.get();

			// Parse how many items
			SellBuyArgParser parser = new SellBuyArgParser();
			int howMany = parser.parseOrOne(args[0]);

			return runCommand(player, material, howMany);
		}
		return true;
	}

	abstract protected boolean runCommand(Player player, Material material, int howMany);
}
