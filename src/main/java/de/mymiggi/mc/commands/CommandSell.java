package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Bank;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CommandSell extends AbstractMaterialCommand
{
	public CommandSell(Bank bank)
	{
		super(bank);
	}

	@Override
	protected boolean runCommand(Player player, Material material, int howMany)
	{
		boolean isSaleAble = shop.moneyForSelling(material) * howMany > 0;
		if (!isSaleAble)
		{
			shop.sendMessage(player, "You can't sell this item!");
			return true;
		}
		Inventory inventory = player.getInventory();
		int itemSum = inventory.all(material).values()
			.stream()
			.map(ItemStack::getAmount)
			.reduce(0, Integer::sum);
		if (itemSum >= howMany)
		{
			removeItems(inventory, material, howMany);
			bank.addToBalance(player, (long)shop.moneyForSelling(material) * howMany);
			bank.sendMessage(player, "You new balance is " + bank.getBalance(player));
		}
		else
		{
			shop.sendMessage(player, "You don't have enough of this item...");
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