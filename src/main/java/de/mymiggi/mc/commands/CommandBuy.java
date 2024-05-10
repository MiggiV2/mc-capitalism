package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Bank;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBuy extends AbstractMaterialCommand
{
	public CommandBuy(Bank bank)
	{
		super(bank);
	}

	@Override
	protected boolean runCommand(Player player, Material material, int howMany)
	{
		long totalPrize = shop.prizeForBuying(material) * (long)howMany;
		if (totalPrize < 0)
		{
			player.sendMessage("Store: This item is not for sale!");
			return true;
		}
		boolean transactionSuccessful = bank.removeFromBalance(player, totalPrize);
		if (transactionSuccessful)
		{
			int addedItems = 0;
			while (addedItems < howMany)
			{
				int stackSize = Math.min(howMany, 64);
				ItemStack newItems = new ItemStack(material);
				newItems.setAmount(stackSize);
				player.getInventory().addItem(newItems);
				addedItems += stackSize;
			}
			player.sendMessage("Bank: You new balance is " + bank.getBalance(player));
		}
		else
		{
			player.sendMessage("Store: You have not enough money to buy this...");
		}
		return true;
	}
}