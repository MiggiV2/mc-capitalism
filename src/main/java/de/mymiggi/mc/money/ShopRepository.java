package de.mymiggi.mc.money;

import de.mymiggi.mc.util.FileHelper;
import org.bukkit.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ShopRepository
{
	private static final Logger log = LoggerFactory.getLogger(ShopRepository.class);
	private final FileHelper fileHelper = new FileHelper("/price_list.data");
	private static final String DEFAULT_LIST = """
		COBBLESTONE;2
		COAL;18
		DIRT;1
		OAK_LOG;3
		ACACIA_LOG;3
		BIRCH_LOG;3
		CHERRY_LOG;3
		MANGROVE_LOG;3
		SPRUCE_LOG;3
		SAND;40
		IRON_INGOT;80
		GOLD_INGOT;100
		DIAMOND;2000""";

	public void savePriceList(Map<Material, Integer> priceList)
	{
		StringBuilder content = new StringBuilder();
		for (Map.Entry<Material, Integer> entry : priceList.entrySet())
		{
			Material material = entry.getKey();
			int amount = entry.getValue();
			content.append(material.name()).append(";").append(amount).append("\n");
		}
		boolean success = fileHelper.writeToFile(content.toString());
		if (!success)
		{
			log.warn("Could not save price list");
		}
	}

	public Map<Material, Integer> loadPriceList()
	{
		Map<Material, Integer> priceList = new HashMap<>();
		for (String line : fileHelper.readData())
		{
			String[] split = line.split(";");
			if (split.length != 2)
			{

				log.warn("This line was not correctly saved or modified! -> {}", line);
				continue;
			}
			Material material = Material.getMaterial(split[0]);
			int amount;
			try
			{
				amount = Integer.parseInt(split[1]);
			}
			catch (NumberFormatException ex)
			{
				log.warn("Prices was not correctly saved or modified! -> {}", split[1]);
				continue;
			}
			priceList.put(material, amount);
		}
		if (priceList.isEmpty())
		{
			fileHelper.writeToFile(DEFAULT_LIST);
			return loadPriceList();
		}
		return priceList;
	}
}
