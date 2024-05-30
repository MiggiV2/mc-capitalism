package de.mymiggi.mc.util;

import de.mymiggi.mc.money.BankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHelper
{
	private final String DATA_DIR;
	private final String DATA_FILE;
	private static final Logger log = LoggerFactory.getLogger(BankRepository.class);

	public FileHelper(String DATA_DIR, String DATA_FILE)
	{
		this.DATA_DIR = DATA_DIR;
		this.DATA_FILE = DATA_DIR + DATA_FILE;
	}

	public FileHelper(String DATA_FILE)
	{
		this.DATA_DIR = "./plugins/capitalism";
		this.DATA_FILE = DATA_DIR + DATA_FILE;
	}

	public boolean writeToFile(String data)
	{
		Path path = Paths.get(DATA_DIR);
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

	public List<String> readData()
	{
		try
		{
			Path dataPath = Paths.get(DATA_FILE);
			if (!dataPath.toFile().exists())
			{
				return new ArrayList<>();
			}
			Charset charset = StandardCharsets.UTF_8;
			List<String> lines = Files.readAllLines(dataPath, charset);
			return lines.stream().filter(l -> !l.isBlank()).toList();
		}
		catch (IOException e)
		{
			log.error("Failed to read data from file!", e);
		}
		return new ArrayList<>();
	}
}