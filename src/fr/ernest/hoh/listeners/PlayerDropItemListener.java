package fr.ernest.hoh.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.WallSign;
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
import fr.ernest.hoh.utils.Message;
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
	}
	
	public void revokeTotem(final Player p, final Item i) {
		final SkullMeta head = (SkullMeta) i.getItemStack().getItemMeta();

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

				// System.out.println(nearestTotem.getName() + " " + minDistance);

				if (nearestTotem == null || minDistance > 2)
					return;


				if (nearestTotem.getOwner() != null) {
					OfflinePlayer totemOwner = nearestTotem.getOwner();
					OfflinePlayer headOwner = head.getOwningPlayer();
					// System.out.println(totemOwner.getUniqueId() + " " + headOwner.getUniqueId());
					if (totemOwner.getUniqueId().equals(headOwner.getUniqueId())) {
						nearestTotem.removeOwner();
						i.remove();
						totemRevokedEffects(nearestTotem);
						nearestTotem.save(plugin.getStoreManager().getStore("totems"));
						plugin.getStoreManager().saveStore("totems");
						// System.out.println("OWNER REMOVED " + totemOwner);
					}
				}
				// p.sendMessage(ChatColor.GOLD + "Near totem : " + ChatColor.YELLOW + nearestTotem.getName());
				
				return;
			}
		}.runTaskTimer(plugin, 0, 5);
	}
	
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

				if (b.getLore() != null) {
					OfflinePlayer bannerOwner = Bukkit.getOfflinePlayer(UUID.fromString(b.getLore().get(0)));
					if (nearestTotem.getOwner() == null) { 
						if (bannerOwner.getUniqueId() != null) {
							nearestTotem.setOwner(bannerOwner);
							nearestTotem.save(plugin.getStoreManager().getStore("totems"));
							plugin.getStoreManager().saveStore("totems");
							totemTakenEffects(nearestTotem, i, b);
							i.remove();
						}
					} else {
						p.sendMessage(Message.TOTEM_ALREADY_OWNED.format(nearestTotem.getOwner().getName(), nearestTotem.getName()));
					}
				}
				
				return;
			}
		}.runTaskTimer(plugin, 0, 5);
	}
	
	public void totemRevokedEffects(AbstractTotem t) {
		Location bannerLocation = t.getLocation().clone().add(new Location(t.getLocation().getWorld(), 0, 1, 0));
		System.out.println("LOCATION : " + bannerLocation);
		Block bl = t.getLocation().getWorld().getBlockAt(bannerLocation);
		bl.setType(Material.AIR);	
	}
	
	public void totemTakenEffects(AbstractTotem t, Item i, BannerMeta b) {
		BlockFace[] directions = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
		
		Block totemBlock = t.getLocation().getWorld().getBlockAt(t.getLocation());
		BlockFace totemDirection = null;
		Location bannerLocation = t.getLocation().clone().add(new Location(t.getLocation().getWorld(), 0, 1, 0));
		
		for (BlockFace dir : directions) {
			Block rel = totemBlock.getRelative(dir);
			BlockData data = rel.getBlockData();
			if (data instanceof WallSign) {
				WallSign ws = (WallSign) data;
				totemDirection = ws.getFacing();
				break;
			}
		}
		
		Block bl = t.getLocation().getWorld().getBlockAt(bannerLocation);
		bl.setType(i.getItemStack().getType());
		BlockState state = bl.getState();
		if (state instanceof Banner) {
			Banner ban = (Banner) state;
			ban.setPatterns(b.getPatterns());
			ban.update();
		}
		
		BlockData data = bl.getBlockData();
		if (data instanceof Rotatable) {
			Rotatable rotation = (Rotatable) data;
			if( totemDirection != null) 
				rotation.setRotation(totemDirection);
				bl.setBlockData(rotation);
		}
	}
}
