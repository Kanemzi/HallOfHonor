package fr.ernest.hoh;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ernest.hoh.listeners.PlayerDropItemListener;
import fr.ernest.hoh.utils.StoreManager;
import net.md_5.bungee.api.ChatColor;

public class HallOfHonor extends JavaPlugin {

	private StoreManager store;
	private TotemsManager totemsManager;

	@Override
	public void onEnable() {
		System.out.println("[HallOfHonor] enabled");
		getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);

		store = new StoreManager();
		store.setup();

		totemsManager = new TotemsManager();
		totemsManager.setup();
	}

	@Override
	public void onDisable() {
		System.out.println("[HallOfHonor] disabled");
	}

	public StoreManager getStoreManager() {
		return store;
	}

	public TotemsManager getTotemsManager() {
		return totemsManager;
	}
}
