package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Bank;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.Map;

public class CommandSell extends AbstractMaterialCommand implements CommandExecutor
{
	public CommandSell(Bank bank)
	{
		super(bank);
	}

	@Override
	protected boolean runCommand(Player player, Material material, int howMany)
	{
		Inventory inventory = player.getInventory();
		boolean isSaleAble = shop.moneyForSelling(material) * howMany > 0;
		int itemSum = inventory.all(material).values()
			.stream()
			.map(ItemStack::getAmount)
			.reduce(0, Integer::sum);
		if (!isSaleAble)
		{
			player.sendMessage("Store: You can't sell this item!");
			return true;
		}
		if (itemSum >= howMany)
		{
			removeItems(inventory, material, howMany);
			BigInteger materialPrize = BigInteger.valueOf(shop.moneyForSelling(material));
			BigInteger quanitity = BigInteger.valueOf(howMany);
			bank.addToBalance(player, materialPrize.multiply(quanitity));
			player.sendMessage("Bank: You new balance is " + bank.getBalance(player));
		}
		else
		{
			player.sendMessage("Store: You don't have enough of this item...");
		}
		return true;
	}

	private void removeItems(Inventory inventory, Material material, int quantity)
	{
		int removeItems = 0;
		Map<Integer, ? extends ItemStack> dirtMap = inventory.all(material);
		for (int slot : dirtMap.keySet())
		{
			ItemStack itemStack = dirtMap.get(slot);
			boolean moreThanNeeded = itemStack.getAmount() > (quantity - removeItems);
			int newAmount = moreThanNeeded
				? itemStack.getAmount() - (quantity - removeItems)
				: 0;

			removeItems += itemStack.getAmount();
			itemStack.setAmount(newAmount);

			if (moreThanNeeded || removeItems == quantity)
			{
				break;
			}
		}
	}
}