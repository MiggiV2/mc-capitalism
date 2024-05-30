package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Bank;
import de.mymiggi.mc.money.Shop;
import de.mymiggi.mc.util.DiscountWheel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CommandBuy extends AbstractMaterialCommand
{
	private final DiscountWheel wheel;
	private final Server server;

	public CommandBuy(Shop shop, Bank bank, Server server)
	{
		super(shop, bank);
		this.wheel = new DiscountWheel(shop);
		this.server = server;
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

	public void resetUsedCountAndRunDiscountWheel()
	{
		usedMap.clear();
		wheel.generateDiscount().ifPresent(d -> {
			String text = String.format("%d%% off for %s. You only play %s€!", (int)(shop.getFeeInPercent() - d.getNewFee() * 100),
				d.getMaterial().toString().toLowerCase(), shop.prizeForBuying(d.getMaterial()));
			Component component = Component.text("Store: ").color(NamedTextColor.GREEN)
				.append(Component.text(text).color(NamedTextColor.WHITE));
			server.broadcast(component);
		});
	}
}