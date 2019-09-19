package fr.ernest.hoh;

import org.bukkit.plugin.java.JavaPlugin;

import fr.ernest.hoh.utils.StoreManager;
import fr.ernest.listeners.PlayerDropItemListener;

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
