package de.mymiggi.mc.entity;

import org.bukkit.Material;

public class Discount
{
	private Material material;
	private double newFee;

	public Discount(Material material, double newFee)
	{
		this.material = material;
		this.newFee = newFee;
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public double getNewFee()
	{
		return newFee;
	}

	public void setNewFee(double newFee)
	{
		this.newFee = newFee;
	}
}