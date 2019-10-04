package fr.ernest.hoh.effects;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.entities.AbstractTotem;

public class WolfManager implements TotemManager, Listener {
	
	public static final String NAME = "Wolf";
	
	public static WolfManager instance = null;
	private BukkitRunnable wolfRunnable;
	
	private static HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	
	public WolfManager() {
		if (instance == null) {
			instance = this;
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		
		AbstractTotem totem = plugin.getTotemsManager().getTotem(NAME);
		if (totem == null) return;
		
		OfflinePlayer owner = totem.getOwner();
		if (owner == null) return;
		
		Player wolf = plugin.getServer().getPlayer(owner.getName());
		if (wolf == null) return;
		
		Entity damaged = e.getEntity();
		if (!(damaged instanceof Player)) return;
		
		Entity damager = e.getDamager();
		if (!(damager instanceof Player)) return;
		
		Player victim = (Player) damaged;
		
		if (wolf.equals(damager)) {
			long time = wolf.getWorld().getTime();
			
			if (time > 13000) {
				victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 4, 0), true);
			}
		}
	}
	
	public void start() {
		wolfRunnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				AbstractTotem totem = plugin.getTotemsManager().getTotem("Wolf");
				if (totem == null) return; 
				
				OfflinePlayer owner = totem.getOwner();
				if (owner == null) return;
				
				Player p = plugin.getServer().getPlayer(owner.getName());
				if (p == null) return;
				
				p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 20, 0), true);
			}
		};
		
		wolfRunnable.runTaskTimer(plugin, 0, 20 * 10);
	}	
}
