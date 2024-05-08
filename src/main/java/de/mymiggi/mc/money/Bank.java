package de.mymiggi.mc.money;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bank
{
	private final Map<UUID, Integer> userMoneyMap = new HashMap<>();

	public void addToBalance(Player player, int amount)
	{
		int balance = getBalance(player);
		userMoneyMap.put(player.getUniqueId(), balance + amount);
	}

	public boolean removeFromBalance(Player player, int amount)
	{
		int balance = getBalance(player);
		if (balance < amount)
		{
			return false;
		}
		userMoneyMap.put(player.getUniqueId(), balance - amount);
		return true;
	}

	public int getBalance(Player player)
	{
		return userMoneyMap.getOrDefault(player.getUniqueId(), 0);
	}
}
