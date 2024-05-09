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

import java.math.BigInteger;
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
			Player payerTo = server.getPlayer(args[0]);

			if (payerTo == null)
			{
				playerSend.sendMessage("No Player with name " + args[0] + " found!");
				return true;
			}

			executeTransaction(args[1], playerSend, payerTo);
		}
		return true;
	}

	private void executeTransaction(@NotNull String moneyToMove, Player playerSend, Player payerTo)
	{
		try
		{
			long amountL = Long.parseLong(moneyToMove);
			if (amountL < 0)
			{
				throw new IllegalAccessException();
			}
			BigInteger amount = BigInteger.valueOf(amountL);
			boolean success = bank.removeFromBalance(playerSend, amount);
			if (success)
			{
				bank.addToBalance(payerTo, amount);
				playerSend.sendMessage("Bank: Money successfully send to " + payerTo.getName());
				payerTo.sendMessage("Bank: You received money from " + playerSend.getName());
				playerSend.sendMessage("Bank: You new balance is " + bank.getBalance(playerSend));
				payerTo.sendMessage("Bank: You new balance is " + bank.getBalance(payerTo));
			}
			else
			{
				playerSend.sendMessage("Bank: You have no enough money!");
			}
		}
		catch (NumberFormatException exception)
		{
			playerSend.sendMessage("Bank: We can't send " + moneyToMove);
		}
		catch (IllegalAccessException e)
		{
			playerSend.sendMessage("Bank: You have no permission to scam " + payerTo.getName());
		}
	}
}