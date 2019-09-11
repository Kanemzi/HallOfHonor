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

import fr.ernest.hoh.utils.StoreManager;
import net.md_5.bungee.api.ChatColor;

public class HallOfHonor extends JavaPlugin implements Listener {

	private StoreManager store;
	private TotemsManager totemsManager;

	@Override
	public void onEnable() {
		System.out.println("[HallOfHonor] enabled");
		getServer().getPluginManager().registerEvents(this, this);

		store = new StoreManager();
		store.setup();

		totemsManager = new TotemsManager();
		totemsManager.setup();
	}

	@Override
	public void onDisable() {
		System.out.println("[HallOfHonor] disabled");
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Item i = event.getItemDrop();
		Player p = event.getPlayer();

		if (!i.getItemStack().getType().equals(Material.PLAYER_HEAD))
			return;

		getServer().broadcastMessage(p.getName() + " balance la tête de "
				+ ((SkullMeta) i.getItemStack().getItemMeta()).getOwningPlayer().getName());

		List<Entity> near = i.getNearbyEntities(2.0, 2.0, 2.0);
		// getServer().broadcastMessage("near : " + near);

		Entity armorStand = null;
		for (Entity e : near) {
			if (e instanceof ArmorStand) {
				getServer().broadcastMessage("near");
				getServer().broadcastMessage(
						ChatColor.GREEN + e.getCustomName() + " armor near : " + e.getScoreboardTags());
				Location l = e.getLocation().clone();
				l.setY(l.getY() + 2);

				Block b = e.getWorld().getBlockAt(l);
				BlockState bs = b.getState();

				if (bs instanceof Banner) {
					if (bs.hasMetadata("Owner")) {
						getServer().broadcastMessage(
								ChatColor.GOLD + "banner owner : " + bs.getMetadata("Owner").get(0).value());
					} else {
						bs.setMetadata("Owner", new FixedMetadataValue(this, p.getDisplayName()));
					}

				} else {
					getServer().broadcastMessage(ChatColor.GREEN + "The totem is available !");
				}
			}
		}
	}

	public StoreManager getStoreManager() {
		return store;
	}

	public TotemsManager getTotemsManager() {
		return totemsManager;
	}
}
