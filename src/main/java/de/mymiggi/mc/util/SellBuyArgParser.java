package de.mymiggi.mc.util;

public class SellBuyArgParser
{

	public int parseOrOne(String arg)
	{
		try
		{
			return Integer.parseInt(arg);
		}
		catch (
			NumberFormatException ex)
		{
			return 1;
		}
	}
}
