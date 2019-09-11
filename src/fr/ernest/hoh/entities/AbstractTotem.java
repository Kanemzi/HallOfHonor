package fr.ernest.hoh.entities;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class AbstractTotem {
	private String name;
	private Location location;

	public AbstractTotem(Location location, String name) {
		this.location = location;
		this.name = name;
	}
	
	/**
	 * Saves the totem in the store file
	 */
	public void save(FileConfiguration store) {
	}
}
