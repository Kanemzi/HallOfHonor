package fr.ernest.hoh;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import fr.ernest.commands.CreateTotemCommand;
import fr.ernest.commands.RemoveTotemCommand;
import fr.ernest.commands.SetTotemOwnerCommand;
import fr.ernest.hoh.entities.AbstractTotem;

public class TotemsManager {

	private HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	private FileConfiguration store;

	private Map<String, AbstractTotem> totems;

	public void setup() {
		totems = new HashMap<>();

		plugin.getCommand(CreateTotemCommand.NAME).setExecutor(new CreateTotemCommand());
		plugin.getCommand(RemoveTotemCommand.NAME).setExecutor(new RemoveTotemCommand());
		plugin.getCommand(SetTotemOwnerCommand.NAME).setExecutor(new SetTotemOwnerCommand());
		
		
		store = plugin.getStoreManager().addStore("totems");
		totems.putAll(AbstractTotem.load(store));
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
	
	public AbstractTotem getTotem(String name) {
		AbstractTotem totem = totems.get(name);
		return totem;
	}

	public boolean totemExists(String name) {
		return totems.containsKey(name);
	}

	public Map<String, AbstractTotem> getTotems() {
		return totems;
	}
}
