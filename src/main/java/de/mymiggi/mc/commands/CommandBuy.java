package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Bank;
import de.mymiggi.mc.util.AbstractMaterialCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBuy extends AbstractMaterialCommand implements CommandExecutor
{
	public CommandBuy(Bank bank)
	{
		super(bank);
	}

	@Override
	protected boolean runCommand(Player player, Material material, int howMany)
	{
		int totalPrize = shop.prizeForBuying(material) * howMany;
		if (totalPrize < 0)
		{
			player.sendMessage("Store: This item is not for sale!");
			return true;
		}
		boolean transactionSuccessful = bank.removeFromBalance(player, totalPrize);
		if (transactionSuccessful)
		{
			ItemStack newItems = new ItemStack(material);
			newItems.setAmount(howMany);
			player.getInventory().addItem(newItems);
			player.sendMessage("Bank: You new balance is " + bank.getBalance(player));
		}
		else
		{
			player.sendMessage("Store: You have not enough money to buy this...");
		}
		return true;
	}
}
