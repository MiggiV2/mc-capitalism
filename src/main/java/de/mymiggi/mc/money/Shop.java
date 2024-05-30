package de.mymiggi.mc.money;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static net.kyori.adventure.text.Component.text;

public class Shop
{
	private final Map<Material, Integer> materialMoneyMap = new HashMap<>();
	private final Map<String, Material> aliasMap = new HashMap<>();
	private final Component prefix = text("Shop: ").color(NamedTextColor.GREEN);
	private final double fee = 0.3;
	private static boolean enabled = true;

	public Shop()
	{
		materialMoneyMap.put(Material.DIRT, 1);
		materialMoneyMap.put(Material.COBBLESTONE, 2);
		materialMoneyMap.put(Material.OAK_LOG, 3);
		materialMoneyMap.put(Material.COAL, 18);
		materialMoneyMap.put(Material.IRON_INGOT, 80);
		materialMoneyMap.put(Material.GOLD_INGOT, 100);

		aliasMap.put("STONE", Material.COBBLESTONE);
		aliasMap.put("WOOD", Material.OAK_LOG);
		aliasMap.put("IRON", Material.IRON_INGOT);
		aliasMap.put("GOLD", Material.GOLD_INGOT);
	}

	public Set<Material> listMaterials()
	{
		return this.materialMoneyMap.keySet();
	}

	public int moneyForSelling(Material material)
	{
		return materialMoneyMap.getOrDefault(material, 0);
	}

	public int prizeForBuying(Material material)
	{
		return (int)(materialMoneyMap.getOrDefault(material, -1) * (1.0 + fee));
	}

	public String getAliasOrDefaultName(Material material)
	{
		for (String temp : aliasMap.keySet())
		{
			if (material.name().equals(aliasMap.get(temp).name()))
			{
				return temp;
			}
		}
		return material.name();
	}

	public Optional<Material> getMaterialByName(String name)
	{
		name = name.toUpperCase();
		if (aliasMap.containsKey(name))
		{
			return Optional.of(aliasMap.get(name));
		}
		try
		{
			return Optional.of(Material.valueOf(name));
		}
		catch (IllegalArgumentException ex)
		{
			return Optional.empty();
		}
	}

	public int getFeeInPercent()
	{
		return (int)(this.fee * 100);
	}

	public void sendMessage(@NotNull Player player, String message)
	{
		Component msg = prefix.append(text(message).color(NamedTextColor.WHITE));
		player.sendMessage(msg);
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		Shop.enabled = enabled;
	}
}
