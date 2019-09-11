package fr.ernest.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.entities.AbstractTotem;
import net.md_5.bungee.api.ChatColor;

public class RemoveTotemCommand implements CommandExecutor {

	public static final String NAME = "removetotem";

	private HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		if (args.length != 1)
			return false;

		String name = args[0];

		if (plugin.getTotemsManager().totemExists(name)) {
			plugin.getTotemsManager().removeTotem(name);
			sender.sendMessage(
					ChatColor.GREEN + "The totem " + ChatColor.YELLOW + name + ChatColor.GREEN + " has been removed");
			return true;
		} else {
			sender.sendMessage(
					ChatColor.RED + "The totem " + ChatColor.YELLOW + name + ChatColor.RED + " does not exists");
			return true;
		}
	}

}
