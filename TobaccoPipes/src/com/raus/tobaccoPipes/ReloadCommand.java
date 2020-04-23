package com.raus.tobaccoPipes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.ChatColor;

public class ReloadCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 0)
		{
			return false;
		}
		else if (args[0].equals("info"))
		{
			// Send message
			sender.sendMessage(ChatColor.GOLD + "[Pipes]" + ChatColor.GRAY + " Version " + Main.getInstance().getDescription().getVersion());
			return true;
		}
		else if (args[0].equals("reload"))
		{
			if (!sender.hasPermission("pipes.reload"))
			{
				// Send message
				sender.sendMessage(ChatColor.GOLD + "[Pipes]" + ChatColor.RED + " You do not have permission to run this command.");
				return true;
			}
			
			// Reload config
			Main.getInstance().rebuild();
			
			// Send message
			sender.sendMessage(ChatColor.GOLD + "[Pipes]" + ChatColor.GRAY + " Config reloaded!");
			
			return true;
		}
		else
		{
			return false;
		}
	}
}