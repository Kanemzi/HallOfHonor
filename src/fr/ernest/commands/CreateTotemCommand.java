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

public class CreateTotemCommand implements CommandExecutor {

	public static final String NAME = "createtotem";

	private HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		if (args.length != 4)
			return false;

		Player player = (Player) sender;

		Location location = null;
		try {
			location = new Location(player.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]),
					Integer.parseInt(args[2]));
		} catch (NumberFormatException e) {
			return false;
		}

		String name = args[3];

		if (plugin.getTotemsManager().getTotems().containsKey(name)) {
			sender.sendMessage(
					ChatColor.RED + "The totem " + ChatColor.YELLOW + name + ChatColor.RED + " does already exists");
			return true;
		}

		AbstractTotem totem = new AbstractTotem(location, name);
		plugin.getTotemsManager().getTotems().put(name, totem);

		sender.sendMessage(ChatColor.GREEN + "The totem " + ChatColor.YELLOW + name + ChatColor.GREEN
				+ " has been created at " + ChatColor.AQUA
				+ location.getBlockX() + " "
				+ location.getBlockY() + " "
				+ location.getBlockZ());
		return true;
	}

}
