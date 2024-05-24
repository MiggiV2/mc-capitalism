package de.mymiggi.mc.money;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.util.Map;

import static net.kyori.adventure.text.Component.text;

public class Bank
{
	private final Map<String, BigInteger> userMoneyMap;
	private final BankRepository repository = new BankRepository();
	private final Component prefix = text("Bank: ").color(TextColor.fromHexString("#ffa500"));

	public Bank()
	{
		this.userMoneyMap = repository.readData();
	}

	public void addToBalance(Player player, long amount)
	{
		positivCheck(amount);
		BigInteger money = BigInteger.valueOf(amount);
		synchronized (userMoneyMap)
		{
			BigInteger balance = getBalance(player);
			userMoneyMap.put(player.getName(), balance.add(money));
			persist();
		}
	}

	public boolean removeFromBalance(Player player, long amount)
	{
		positivCheck(amount);
		BigInteger money = BigInteger.valueOf(amount);
		synchronized (userMoneyMap)
		{
			BigInteger balance = getBalance(player);
			if (balance.compareTo(money) < 0)
			{
				return false;
			}
			userMoneyMap.put(player.getName(), balance.subtract(money));
			persist();
		}
		return true;
	}

	public BigInteger getBalance(Player player)
	{
		synchronized (userMoneyMap)
		{
			return userMoneyMap.getOrDefault(player.getName(), BigInteger.ZERO);
		}
	}

	private void persist()
	{
		repository.writeToFile(this.toString());
	}

	private void positivCheck(long money)
	{
		if (money < 0)
		{
			throw new IllegalArgumentException("We can only process positive amount of money!");
		}
	}

	@Override
	public String toString()
	{
		StringBuilder data = new StringBuilder();
		for (String key : userMoneyMap.keySet())
		{
			String newLine = String.format("%d;%s\n", userMoneyMap.get(key), key);
			data.append(newLine);
		}
		return data.toString();
	}

	public void sendMessage(Player player, String message)
	{
		Component msg = prefix.append(text(message).color(NamedTextColor.WHITE));
		player.sendMessage(msg);
	}
}