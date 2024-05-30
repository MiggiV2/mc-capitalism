package de.mymiggi.mc.commands;

import de.mymiggi.mc.entity.Transaction;
import de.mymiggi.mc.money.Bank;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandSendMoney implements CommandExecutor
{
	private final Server server;
	private final Bank bank;
	private final Map<String, Transaction> pendingTransactions;

	public CommandSendMoney(Server server, Bank bank)
	{
		this.server = server;
		this.bank = bank;
		this.pendingTransactions = new HashMap<>();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
	{
		if (sender instanceof Player playerSend)
		{
			// add to pending list
			if (args.length == 2)
			{
				return addNewTransaction(args, playerSend);
			}
			// execute transaction
			if (args.length == 1 && args[0].equalsIgnoreCase("confirm"))
			{
				Transaction transaction = pendingTransactions.get(playerSend.getName());
				if (transaction != null)
				{
					executeTransaction(transaction);
				}
				else
				{
					bank.sendMessage(playerSend, "There is no pending transaction associated with your name...");
				}
				return true;
			}
		}
		return false;
	}

	private boolean addNewTransaction(@NotNull String @NotNull [] args, Player playerSend)
	{
		Player receiver = server.getPlayer(args[0]);
		if (receiver == null)
		{
			bank.sendMessage(playerSend, "No Player with name " + args[0] + " found!");
			pendingTransactions.remove(playerSend.getName());
			return true;
		}
		try
		{
			long amount = Long.parseLong(args[1]);
			Transaction transaction = new Transaction(playerSend, receiver, amount);
			pendingTransactions.put(playerSend.getName(), transaction);
			bank.sendMessage(playerSend, "We received your transaction. Type /money2 confirm to confirm your transaction!");
		}
		catch (NumberFormatException exception)
		{
			bank.sendMessage(playerSend, "An error occurred while trying to send your money...");
			return false;
		}
		return true;
	}

	private void executeTransaction(Transaction transaction)
	{
		Player sender = transaction.sender();
		Player receiver = transaction.receiver();
		try
		{
			boolean success = bank.removeFromBalance(sender, transaction.amount());
			if (success)
			{
				bank.addToBalance(receiver, transaction.amount());
				bank.sendMessage(sender, "Money successfully send to " + receiver.getName());
				bank.sendMessage(sender, "Your new balance is " + bank.getBalance(sender));
				bank.sendMessage(receiver, "You received money from " + sender.getName());
				bank.sendMessage(receiver, "Your new balance is " + bank.getBalance(receiver));
			}
			else
			{
				bank.sendMessage(sender, "You have no enough money! Transaction cancelled...");
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
		pendingTransactions.remove(sender.getName());
	}
}