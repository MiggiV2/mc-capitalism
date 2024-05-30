package de.mymiggi.mc;

import de.mymiggi.mc.commands.CommandBuy;
import de.mymiggi.mc.commands.CommandSell;
import de.mymiggi.mc.commands.CommandSendMoney;
import de.mymiggi.mc.commands.CommandStore;
import de.mymiggi.mc.commands.CommandWizard;
import de.mymiggi.mc.events.JoinListener;
import de.mymiggi.mc.events.SleepListener;
import de.mymiggi.mc.money.Bank;
import de.mymiggi.mc.money.Shop;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Capitalism extends JavaPlugin
{
	private final Bank bank = new Bank();
	private final Shop shop = new Shop();

	@Override
	public void onEnable()
	{
		// Register commands
		CommandSell sell = new CommandSell(shop, bank);
		Objects.requireNonNull(getCommand("sell")).setExecutor(sell);

		CommandBuy buy = new CommandBuy(shop, bank);
		Objects.requireNonNull(getCommand("buy")).setExecutor(buy);

		CommandSendMoney money2 = new CommandSendMoney(getServer(), bank);
		Objects.requireNonNull(getCommand("money2")).setExecutor(money2);

		CommandStore store = new CommandStore(shop);
		Objects.requireNonNull(getCommand("store")).setExecutor(store);

		CommandWizard wizard = new CommandWizard();
		Objects.requireNonNull(getCommand("wizard")).setExecutor(wizard);

		Server server = getServer();
		server.getScheduler().runTaskTimer(this, buy::resetUsedCount, 0, 6000);

		// Listener
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		getServer().getPluginManager().registerEvents(new SleepListener(server, this, buy), this);
	}

	@Override
	public void onDisable()
	{
		// Plugin shutdown logic
	}
}