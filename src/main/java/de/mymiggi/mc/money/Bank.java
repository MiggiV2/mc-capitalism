package de.mymiggi.mc.money;

import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.util.Map;

public class Bank
{
	private final Map<String, BigInteger> userMoneyMap;
	private final BankRepository repository = new BankRepository();

	public Bank()
	{
		this.userMoneyMap = repository.readData();
	}

	public void addToBalance(Player player, long amount)
	{
		positivCheck(amount);
		BigInteger balance = getBalance(player);
		BigInteger money = BigInteger.valueOf(amount);
		userMoneyMap.put(player.getName(), balance.add(money));
		persist();
	}

	public boolean removeFromBalance(Player player, long amount)
	{
		positivCheck(amount);
		BigInteger balance = getBalance(player);
		BigInteger money = BigInteger.valueOf(amount);
		if (balance.compareTo(money) < 0)
		{
			return false;
		}
		userMoneyMap.put(player.getName(), balance.subtract(money));
		persist();
		return true;
	}

	public BigInteger getBalance(Player player)
	{
		return userMoneyMap.getOrDefault(player.getName(), BigInteger.ZERO);
	}

	private void persist()
	{
		Thread thread = new Thread(() -> {
			repository.writeToFile(this.toString());
		});
		thread.start();
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
}