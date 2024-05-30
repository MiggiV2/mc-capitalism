package de.mymiggi.mc.money;

import de.mymiggi.mc.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankRepository
{
	private static final Logger log = LoggerFactory.getLogger(BankRepository.class);
	private final FileHelper fileHelper = new FileHelper("/bank.data");

	public void saveBalances(Bank bank)
	{
		boolean success = fileHelper.writeToFile(bank.toString());
		if (!success)
		{
			log.warn("Failed to save balances!");
		}
	}

	public Map<String, BigInteger> readData()
	{
		Map<String, BigInteger> map = new ConcurrentHashMap<>();

		for (String line : fileHelper.readData())
		{
			String[] parts = line.split(";");
			if (parts.length != 2)
			{
				log.warn("This line was not correctly saved! -> {}", line);
				continue;
			}
			String name = parts[1];
			BigInteger balance = BigInteger.valueOf(Long.parseLong(parts[0]));
			map.put(name, balance);
		}

		return map;
	}
}