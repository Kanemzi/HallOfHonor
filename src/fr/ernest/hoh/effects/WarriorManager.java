package fr.ernest.hoh.effects;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.entities.AbstractTotem;

public class WarriorManager implements TotemManager {
	
	public static WarriorManager instance = null;
	private BukkitRunnable warriorRunnable;
	
	private static HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	
	public WarriorManager() {
		if (instance == null) {
			instance = this;
		}
	}
	
	public void start() {
		warriorRunnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				AbstractTotem totem = plugin.getTotemsManager().getTotem("Warrior");
				if (totem == null) return; 
				
				OfflinePlayer owner = totem.getOwner();
				if (owner == null) return;
				
				Player p = owner.getPlayer();
				if (p == null) return;
				
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 0));
			}
		};
		
		warriorRunnable.runTaskTimer(plugin, 0, 20 * 10);
	}	
}
