package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Bank;
import de.mymiggi.mc.money.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CommandBuy extends AbstractMaterialCommand
{
	public CommandBuy(Shop shop, Bank bank)
	{
		super(shop, bank);
	}

	private static final int LIMIT = 5;
	private final Map<String, Integer> usedMap = new HashMap<>();

	@Override
	protected boolean runCommand(Player player, Material material, int howMany)
	{
		long totalPrize = shop.prizeForBuying(material) * (long)howMany;
		if (totalPrize < 0)
		{
			shop.sendMessage(player, "This item is not for sale!");
			return true;
		}
		int usedCount = usedMap.getOrDefault(player.getName(), 0);
		if (usedCount >= LIMIT)
		{
			shop.sendMessage(player, "You have reached your limit of " + LIMIT + " times.");
			shop.sendMessage(player, "Come back in 5 minutes :)");
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
			shop.sendMessage(player, "Thanks for buying at our store :)");
			shop.sendMessage(player, "You have " + (LIMIT - usedCount - 1) + " visits for today.");
			bank.sendMessage(player, "Your new balance is " + bank.getBalance(player));
			usedMap.put(player.getName(), usedCount + 1);
		}
		else
		{
			bank.sendMessage(player, String.format("You can't effort %d€ to by %s...", totalPrize, material.name().toLowerCase()));
		}
		return true;
	}

	public void resetUsedCount()
	{
		usedMap.clear();
	}
}