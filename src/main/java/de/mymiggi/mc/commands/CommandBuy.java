package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Bank;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;

public class CommandBuy extends AbstractMaterialCommand
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
		boolean transactionSuccessful = bank.removeFromBalance(player, BigInteger.valueOf(totalPrize));
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