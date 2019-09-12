package fr.ernest.hoh.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.ernest.hoh.HallOfHonor;
import net.md_5.bungee.api.ChatColor;

public class AbstractTotem {
	private String name;
	private Location location;
	private Player owner;

	private static HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);

	public AbstractTotem(String name, Location location) {
		this.name = name;
		this.location = location;
		this.owner = null;
	}

	/**
	 * Changes the current owner of the totem
	 */
	public void setOwner(Player player) {
		if (!(owner == null))
			return;

		owner = player;
		// @TODO: Play sound
		plugin.getServer().broadcast("Hall de l'Honneur", ChatColor.GOLD + "Le totem " + ChatColor.YELLOW + name
				+ ChatColor.GOLD + " a été pris par " + ChatColor.YELLOW + owner.getName());
	}

	/**
	 * Removes the current owner of the totem
	 */
	public void removeOwner() {
		// @TODO: Play sound
		plugin.getServer().broadcast("Hall de l'Honneur",
				ChatColor.YELLOW + owner.getName() + ChatColor.GOLD + " a perdu le totem " + ChatColor.YELLOW + name);
		owner = null;
	}

	/**
	 * Saves the totem in the store
	 */
	public void save(FileConfiguration store) {
		store.set("totems." + name + ".x", location.getBlockX());
		store.set("totems." + name + ".y", location.getBlockY());
		store.set("totems." + name + ".z", location.getBlockZ());
		if (owner != null)
			store.set("totems." + name + ".owner", owner.getUniqueId());
		else
			store.set("totems." + name + ".owner", null);
	}

	/**
	 * Removes the totem from the store
	 */
	public void erease(FileConfiguration store) {
		store.set("totems." + name, null);
	}

	/**
	 * Loads all the totems from the store
	 */
	public static Map<String, AbstractTotem> load(FileConfiguration store) {

		Map<String, AbstractTotem> totems = new HashMap<>();
		ConfigurationSection section = store.getConfigurationSection("totems");
		World world = plugin.getServer().getWorlds().get(0);

		for (String name : section.getKeys(false)) {
			double x = store.getDouble(name + ".x");
			double y = store.getDouble(name + ".y");
			double z = store.getDouble(name + ".z");
			AbstractTotem totem = new AbstractTotem(name, new Location(world, x, y, z));
			totems.put(name, totem);
		}

		return totems;
	}

	@Override
	public String toString() {
		return "AbstractTotem [name=" + name + ", location=" + location + "]";
	}
}
