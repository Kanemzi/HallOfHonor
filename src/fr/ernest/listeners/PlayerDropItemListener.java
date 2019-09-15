package fr.ernest.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
import org.bukkit.scheduler.BukkitRunnable;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.entities.AbstractTotem;
import net.md_5.bungee.api.ChatColor;

public class PlayerDropItemListener implements Listener {

	private HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {

		final Item i = event.getItemDrop();
		final Player p = event.getPlayer();

		if (!i.getItemStack().getType().equals(Material.PLAYER_HEAD))
			return;

		final SkullMeta head = (SkullMeta) i.getItemStack().getItemMeta();
		
		// plugin.getServer().broadcastMessage(p.getName() + " balance la tête de "
		// + ((SkullMeta) i.getItemStack().getItemMeta()).getOwningPlayer().getName());

		// List<Entity> near = i.getNearbyEntities(2.0, 2.0, 2.0);
		// getServer().broadcastMessage("near : " + near);

		new BukkitRunnable() {
			
			private int tries = 0;
			
			@Override
			public void run() {
				tries ++;
				if (tries > 10) { // stop tracking merged or picked head items after some time (avoid infinite runnables)
					this.cancel();
					return;
				}
				
				if (!i.isOnGround())
					return;
				
				this.cancel();
				
				double minDistance = Double.MAX_VALUE;
				AbstractTotem nearestTotem = null;
				for (AbstractTotem totem : plugin.getTotemsManager().getTotems().values()) {
					Location totemLocation = totem.getLocation();
					double distance = i.getLocation().distance(totemLocation);
					if (distance < minDistance) {
						minDistance = distance;
						nearestTotem = totem;
					}
				}


				if (nearestTotem == null || minDistance > 2)
					return;

				p.sendMessage("dropping head " + minDistance + "  " + nearestTotem.getName());

				if (nearestTotem.getOwner() != null) {
					Player totemOwner = nearestTotem.getOwner();
					OfflinePlayer headOwner = head.getOwningPlayer();
					System.out.println(totemOwner.getUniqueId() + " " + headOwner.getUniqueId());
					if (totemOwner.getUniqueId().equals(headOwner.getUniqueId())) {
						nearestTotem.removeOwner();
						nearestTotem.save(plugin.getStoreManager().getStore("totems"));
						plugin.getStoreManager().saveStore("totems");
						System.out.println("OWNER REMOVED " + totemOwner);
					}
				}
				p.sendMessage(ChatColor.GOLD + "Near totem : " + ChatColor.YELLOW + nearestTotem.getName());
				
				return;
			}
		}.runTaskTimerAsynchronously(plugin, 0, 5);

//		Entity armorStand = null;
//		for (Entity e : near) {
//			if (e instanceof ArmorStand) {
//				plugin.getServer().broadcastMessage("near");
//				plugin.getServer().broadcastMessage(
//						ChatColor.GREEN + e.getCustomName() + " armor near : " + e.getScoreboardTags());
//				Location l = e.getLocation().clone();
//				l.setY(l.getY() + 2);
//
//				Block b = e.getWorld().getBlockAt(l);
//				BlockState bs = b.getState();
//
//				if (bs instanceof Banner) {
//					if (bs.hasMetadata("Owner")) {
//						plugin.getServer().broadcastMessage(
//								ChatColor.GOLD + "banner owner : " + bs.getMetadata("Owner").get(0).value());
//					} else {
//						bs.setMetadata("Owner", new FixedMetadataValue(plugin, p.getDisplayName()));
//					}
//
//				} else {
//					plugin.getServer().broadcastMessage(ChatColor.GREEN + "The totem is available !");
//				}
//			}
//		}
	}
}
