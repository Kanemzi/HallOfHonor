package fr.ernest.hoh.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.utils.Message;

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

		if (plugin.getTotemsManager().totemExists(name)) {
			sender.sendMessage(Message.TOTEM_ALREADY_EXISTS.format(name));
			return true;
		}

		plugin.getTotemsManager().addTotem(name, location);
		plugin.getStoreManager().saveStore("totems");

		sender.sendMessage(Message.TOTEM_CREATED.format(name, location.getBlockX(), location.getBlockY(), location.getBlockZ()));
		return true;
	}

}
