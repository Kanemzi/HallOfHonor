package fr.ernest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.utils.Message;

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
			sender.sendMessage(Message.TOTEM_REMOVED.format(name));
			plugin.getStoreManager().saveStore("totems");
			return true;
		} else {
			sender.sendMessage(Message.TOTEM_NOT_EXISTS.format(name));
			return true;
		}
	}
}
