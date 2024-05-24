package de.mymiggi.mc.commands;

import de.mymiggi.mc.money.Bank;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CommandSendMoney implements CommandExecutor
{
	private static final Logger log = LoggerFactory.getLogger(CommandSendMoney.class);
	private Server server;
	private Bank bank;

	public CommandSendMoney(Server server, Bank bank)
	{
		this.server = server;
		this.bank = bank;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
	{
		if (sender instanceof Player)
		{
			log.info("Args: {}", Arrays.toString(args));
			if (args.length != 2)
			{
				return false;
			}

			Player playerSend = (Player)sender;
			Player receiver = server.getPlayer(args[0]);

			if (receiver == null)
			{
				bank.sendMessage(playerSend, "No Player with name " + args[0] + " found!");
				return true;
			}

			executeTransaction(args[1], playerSend, receiver);
		}
		return true;
	}

	private void executeTransaction(@NotNull String moneyToMove, Player sender, Player receiver)
	{
		try
		{
			long amount = Long.parseLong(moneyToMove);
			boolean success = bank.removeFromBalance(sender, amount);
			if (success)
			{
				bank.addToBalance(receiver, amount);
				bank.sendMessage(sender, "Money successfully send to " + receiver.getName());
				bank.sendMessage(sender, "You new balance is " + bank.getBalance(sender));
				bank.sendMessage(receiver, "You received money from " + sender.getName());
				bank.sendMessage(receiver, "You new balance is " + bank.getBalance(receiver));
			}
			else
			{
				bank.sendMessage(sender, "You have no enough money!");
			}
		}
		catch (NumberFormatException exception)
		{
			bank.sendMessage(sender, "An error occurred while trying to send your money...");
		}
		catch (IllegalArgumentException e)
		{
			bank.sendMessage(sender, "You have no permission to scam " + receiver.getName());
		}
	}
}