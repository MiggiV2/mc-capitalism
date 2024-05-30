package de.mymiggi.mc.util;

import de.mymiggi.mc.entity.Discount;
import de.mymiggi.mc.money.Shop;
import org.bukkit.Material;

import java.util.Optional;

public class DiscountWheel
{
	private final Shop shop;

	public DiscountWheel(Shop shop)
	{
		this.shop = shop;
	}

	public Optional<Discount> generateDiscount()
	{
		if (Math.random() < 0.8)
		{
			return Optional.empty();
		}

		// Get material
		Material[] materials = shop.listMaterials().toArray(new Material[0]);
		int randomIndex = (int)(materials.length * Math.random());
		Material material = materials[randomIndex];

		// Get new fee
		int oldPrice = shop.prizeForBuying(material);
		double randomFee = (shop.getFeeInPercent() * Math.random()) / 100;
		Discount discount = new Discount(material, randomFee);

		shop.setDiscount(discount);

		// check if changed
		int newPrice = shop.prizeForBuying(material);
		if (oldPrice != newPrice && newPrice >= shop.moneyForSelling(material))
		{
			return Optional.of(discount);
		}
		return Optional.empty();
	}
}