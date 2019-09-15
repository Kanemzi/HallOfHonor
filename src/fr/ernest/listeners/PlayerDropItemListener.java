package fr.ernest.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.SkullMeta;
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

		if (i.getItemStack().getType().equals(Material.PLAYER_HEAD)) {
			revokeTotem(p, i);
			return;
		} else {
			try {
				BannerMeta banner = (BannerMeta) i.getItemStack().getItemMeta();
				claimTotem(p, i, banner);
			} catch(ClassCastException e) {
				return; // not a banner
			}
		}
		
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
	
	public void revokeTotem(final Player p, final Item i) {
		final SkullMeta head = (SkullMeta) i.getItemStack().getItemMeta();
		// System.out.println(head.getOwningPlayer().getName() + " " + head.getOwningPlayer().getUniqueId());
		
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

				//p.sendMessage("dropping head " + minDistance + "  " + nearestTotem.getName());

				if (nearestTotem.getOwner() != null) {
					OfflinePlayer totemOwner = nearestTotem.getOwner();
					OfflinePlayer headOwner = head.getOwningPlayer();
					// System.out.println(totemOwner.getUniqueId() + " " + headOwner.getUniqueId());
					if (totemOwner.getUniqueId().equals(headOwner.getUniqueId())) {
						nearestTotem.removeOwner();
						nearestTotem.save(plugin.getStoreManager().getStore("totems"));
						plugin.getStoreManager().saveStore("totems");
						// System.out.println("OWNER REMOVED " + totemOwner);
					}
				} else {
					System.out.println(nearestTotem.getName());
				}
				// p.sendMessage(ChatColor.GOLD + "Near totem : " + ChatColor.YELLOW + nearestTotem.getName());
				
				return;
			}
		}.runTaskTimerAsynchronously(plugin, 0, 5);
	}
	
	// @TODO : trouver un système plus safe basé sur les UUID des joueurs (possibilité de changement de pseudo)
	public void claimTotem(final Player p, final Item i, final BannerMeta b) {
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

				if (nearestTotem.getOwner() == null) {
					OfflinePlayer bannerOwner = Bukkit.getOfflinePlayer(b.getDisplayName());
					// System.out.println(totemOwner.getUniqueId() + " " + bannerOwner.getUniqueId());
					if (bannerOwner.getUniqueId() != null) {
						nearestTotem.setOwner(bannerOwner);
						nearestTotem.save(plugin.getStoreManager().getStore("totems"));
						plugin.getStoreManager().saveStore("totems");
						// System.out.println("OWNER REMOVED " + totemOwner);
					}
				}
				// p.sendMessage(ChatColor.GOLD + "Near totem : " + ChatColor.YELLOW + nearestTotem.getName());
				
				return;
			}
		}.runTaskTimerAsynchronously(plugin, 0, 5);
	}
}
