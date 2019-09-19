package fr.ernest.hoh.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.utils.Message;
import net.md_5.bungee.api.ChatColor;

public class AbstractTotem {
	private String name;
	private Location location;
	private OfflinePlayer owner;

	private static HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);

	public AbstractTotem(String name, Location location) {
		this.name = name;
		this.location = location;
		this.owner = null;
	}

	/**
	 * Changes the current owner of the totem
	 */
	public void setOwner(OfflinePlayer player) {
		if (owner != null)
			return;
		owner = player;
		// @TODO: Play sound
		plugin.getServer().broadcastMessage(Message.TOTEM_TAKEN.format(name, owner.getName()));
	}

	/**
	 * Removes the current owner of the totem
	 * @return the ex-owner of the totem
	 */
	public OfflinePlayer removeOwner() {
		// @TODO: Play sound
		plugin.getServer().broadcastMessage(Message.TOTEM_REVOKED.format(owner.getName(), name));
		OfflinePlayer oldowner = owner;
		owner = null;
		return oldowner;
	}

	/**
	 * Saves the totem in the store
	 */
	public void save(FileConfiguration store) {
		store.set("totems." + name + ".x", location.getBlockX());
		store.set("totems." + name + ".y", location.getBlockY());
		store.set("totems." + name + ".z", location.getBlockZ());
		if (owner != null) {
			store.set("totems." + name + ".owner", owner.getUniqueId().toString());
		} else {
			store.set("totems." + name + ".owner", null);
		}
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
		if (section == null) return totems;
				
		World world = plugin.getServer().getWorlds().get(0);

		for (String name : section.getKeys(false)) {
			double x = section.getDouble(name + ".x");
			double y = section.getDouble(name + ".y");
			double z = section.getDouble(name + ".z");
			AbstractTotem totem = new AbstractTotem(name, new Location(world, x, y, z));
			String ownerid = section.getString(name + ".owner");
			if (ownerid != null) {
				OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(ownerid));
				if (owner != null) totem.setOwner(owner);
			}
			totems.put(name, totem);
		}

		return totems;
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}
	
	public OfflinePlayer getOwner() {
		return owner;
	}
	
	@Override
	public String toString() {
		return "AbstractTotem [name=" + name + ", location=" + location + ", owner=" + owner + "]";
	}
}
