package de.mymiggi.mc.money;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankRepository
{
	private static final String BANK_DIR = "./plugins/capitalism";
	private static final String DATA_FILE = BANK_DIR + "/bank.data";
	private static final Logger log = LoggerFactory.getLogger(BankRepository.class);

	public boolean writeToFile(String data)
	{
		Path path = Paths.get(BANK_DIR);
		try
		{
			Files.createDirectories(path);
		}
		catch (IOException e)
		{
			log.error("Failed to create directory!", e);
			log.warn("Path -> {}", path);
			return false;
		}

		try (PrintWriter out = new PrintWriter(DATA_FILE))
		{
			out.println(data);
		}
		catch (FileNotFoundException e)
		{
			log.error("Failed to write to file!", e);
			return false;
		}
		return true;
	}

	public Map<String, BigInteger> readData()
	{
		Map<String, BigInteger> map = new ConcurrentHashMap<>();
		try
		{
			Path dataPath = Paths.get(DATA_FILE);
			if (!dataPath.toFile().exists())
			{
				return map;
			}

			Charset charset = StandardCharsets.UTF_8;
			List<String> content = Files.readAllLines(dataPath, charset);

			for (String line : content)
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
		}
		catch (IOException e)
		{
			log.error("Failed to read data from file!", e);
		}
		return map;
	}
}