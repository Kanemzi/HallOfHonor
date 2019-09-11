package fr.ernest.hoh.entities;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class AbstractTotem {
	private String name;
	private Location location;

	public AbstractTotem(String name, Location location) {
		this.name = name;
		this.location = location;
	}
	
	/**
	 * Saves the totem in the store
	 */
	public void save(FileConfiguration store) {
		store.set("totems." + name + ".x", location.getBlockX());
		store.set("totems." + name + ".y", location.getBlockY());
		store.set("totems." + name + ".z", location.getBlockZ());
	}
	
	/**
	 * Removes the totem from the store
	 */
	public void erease(FileConfiguration store) {
		store.set("totems." + name, null);
	}
}
