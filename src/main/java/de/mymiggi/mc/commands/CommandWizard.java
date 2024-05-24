package de.mymiggi.mc.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandWizard implements CommandExecutor
{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
	{
		final Component component = MiniMessage.miniMessage().deserialize("<b>Welcome, adventurer!</b> I'm thrilled to introduce you to the <b>Capitalism Pulgin</b>, a revolutionary tool that will change the way you interact with blocks and money in Minecraft. As the command wizard, I'll be guiding you through the basics of this fantastic plugin.\n"
			+ "\n"
			+ "<i>The Capitalism Pulgin offers four main commands:</i>\n"
			+ "\n"
			+ "<u>/sell amount block-type</u>\n"
			+ "This command allows you to <b>sell a specific type of block for a set amount of in-game money.</b> Simply type `/sell  <amount>  <block-type>` to get started. For example, if you want to sell 10 cobblestone blocks, you'd enter `/sell 10 cobblestone`. The plugin will automatically calculate the total value and credit your account accordingly.\n"
			+ "\n"
			+ "<u>/buy amount block-type</u>\n"
			+ "The opposite of selling, this command enables you to <b>purchase blocks with in-game money.</b> Use the same syntax as before: `/buy  <amount>  <block-type>`. For instance, if you want to buy 5 diamonds, you'd enter `/buy 5 diamond`.\n"
			+ "\n"
			+ "<u>/money2 PlayerName amount</u>\n"
			+ "Need to send some cash to a fellow player? This command makes it easy! Simply type `/money2  <PlayerName>  <amount>` to transfer the specified amount of money to another player. Make sure to replace `<PlayerName>` with the actual name of the recipient.\n"
			+ "\n"
			+ "<u>/store</u>\n"
			+ "Curious about what's available in the shop and at what prices? This command will give you a comprehensive list of all items and their corresponding values. Just type `/store`, and you'll see everything that can be bought or sold.\n"
			+ "\n"
			+ "These four commands form the foundation of the Capitalism Pulgin, allowing you to manage your in-game finances like never before. <i>Happy trading, adventurer!</i>");
		sender.sendMessage(component);
		return true;
	}
}
