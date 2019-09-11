package fr.ernest.hoh.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.ernest.hoh.HallOfHonor;

public class StoreManager {

	private HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);

	private HashMap<String, Pair<FileConfiguration, File>> stores;

	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		stores = new HashMap<>();
	}

	public FileConfiguration addStore(String name) {
		File storeFile = new File(plugin.getDataFolder(), name + ".yml");

		if (!storeFile.exists()) {
			try {
				storeFile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().severe("Could not create the " + name + ".yml file");
			}
		}

		FileConfiguration config = YamlConfiguration.loadConfiguration(storeFile);
		Bukkit.getServer().getLogger().info("The " + name + ".yml file has been loaded");

		stores.put(name, new Pair<>(config, storeFile));
		return config;
	}

	public FileConfiguration getStore(String name) {
		return stores.get(name).getLeft();
	}
	
	public File getFile(String name) {
		return stores.get(name).getRight();
	}

	public void reloadStore(String name) {
		stores.get(name).setLeft(YamlConfiguration.loadConfiguration(stores.get(name).getRight()));
		Bukkit.getServer().getLogger().info("The " + name + ".yml file has been reloaded");
	}

	public void saveStore(String name) {
		try {
			getStore(name).save(stores.get(name).getRight());
			Bukkit.getServer().getLogger().info(name + " has been saved");
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe("Could not save " + name);
		}
	}
}
