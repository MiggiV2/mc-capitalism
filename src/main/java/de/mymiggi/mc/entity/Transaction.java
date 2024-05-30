package de.mymiggi.mc.entity;

import org.bukkit.entity.Player;

public record Transaction(Player sender, Player receiver, long amount)
{

}
