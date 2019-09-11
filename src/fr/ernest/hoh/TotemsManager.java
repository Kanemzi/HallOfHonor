package fr.ernest.hoh;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import fr.ernest.commands.CreateTotemCommand;
import fr.ernest.commands.RemoveTotemCommand;
import fr.ernest.hoh.entities.AbstractTotem;

public class TotemsManager {

	private HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	private FileConfiguration store;

	private HashMap<String, AbstractTotem> totems;

	public void setup() {
		totems = new HashMap<>();

		plugin.getCommand(CreateTotemCommand.NAME).setExecutor(new CreateTotemCommand());
		plugin.getCommand(RemoveTotemCommand.NAME).setExecutor(new RemoveTotemCommand());
		
		store = plugin.getStoreManager().addStore("totems");
	}

	public void addTotem(String name, Location location) {
		AbstractTotem totem = new AbstractTotem(name, location);
		totems.put(name, totem);
		totem.save(store);
	}

	public void removeTotem(String name) {
		AbstractTotem totem = totems.remove(name);
		totem.erease(store);
	}

	public boolean totemExists(String name) {
		return totems.containsKey(name);
	}

	public HashMap<String, AbstractTotem> getTotems() {
		return totems;
	}
}
