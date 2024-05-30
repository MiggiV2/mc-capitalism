package de.mymiggi.mc;

import de.mymiggi.mc.commands.CommandBuy;
import de.mymiggi.mc.commands.CommandSell;
import de.mymiggi.mc.commands.CommandSendMoney;
import de.mymiggi.mc.commands.CommandStore;
import de.mymiggi.mc.commands.CommandWizard;
import de.mymiggi.mc.events.JoinListener;
import de.mymiggi.mc.events.SleepListener;
import de.mymiggi.mc.money.Bank;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Capitalism extends JavaPlugin
{
	private final Bank bank = new Bank();

	@Override
	public void onEnable()
	{

		// Register commands
		CommandSell sell = new CommandSell(bank);
		Objects.requireNonNull(getCommand("sell")).setExecutor(sell);

		CommandBuy buy = new CommandBuy(bank);
		Objects.requireNonNull(getCommand("buy")).setExecutor(buy);

		CommandSendMoney money2 = new CommandSendMoney(getServer(), bank);
		Objects.requireNonNull(getCommand("money2")).setExecutor(money2);

		CommandStore store = new CommandStore();
		Objects.requireNonNull(getCommand("store")).setExecutor(store);

		CommandWizard wizard = new CommandWizard();
		Objects.requireNonNull(getCommand("wizard")).setExecutor(wizard);

		Server server = getServer();
		server.getScheduler().runTaskTimer(this, buy::resetUsedCount, 0, 6000);

		// Listener
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		getServer().getPluginManager().registerEvents(new SleepListener(server, this), this);
	}

	@Override
	public void onDisable()
	{
		// Plugin shutdown logic
	}
}