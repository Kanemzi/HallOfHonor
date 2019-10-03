package fr.ernest.hoh.effects;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.entities.AbstractTotem;

public class MurdererManager implements Listener {
	
	public static final String NAME = "Murderer";
	public static final int POISON_DURATION = 200; // 10 seconds
	
	private static HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	
	public MurdererManager() {
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		AbstractTotem totem = plugin.getTotemsManager().getTotem(NAME);
		if (totem == null) return;
		
		OfflinePlayer owner = totem.getOwner();
		if (owner == null) return;
		
		Player murderer = owner.getPlayer();
		if (murderer == null) return;
		
		Entity damaged = e.getEntity();
		if (!(damaged instanceof Player)) return;
		
		Entity damager = e.getDamager();
		if (!(damager instanceof Arrow)) return;
		
		Arrow arrow = (Arrow) damager;
		Player victim = (Player) damaged;

		ProjectileSource cause = arrow.getShooter();
		if (!(cause instanceof Player)) return;
		Player source = (Player) cause;
		
		if (murderer.equals((Player) source)) {
			System.out.println("CAUSE : HIT BY MURDERER");

			ItemStack poison = null;
			PotionMeta poisonMeta = null;
			HashMap<Integer, ? extends ItemStack> potions = murderer.getInventory().all(Material.POTION);
			for (ItemStack p : potions.values()) {
				PotionMeta data = (PotionMeta) p.getItemMeta();
				if (data.getBasePotionData().getType() == PotionType.POISON) {
					poison = p;
					poisonMeta = data;
					break;
				}
			}
			
			if (poison != null) {
				murderer.getInventory().clear(murderer.getInventory().first(poison));
				victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, POISON_DURATION, poisonMeta.getBasePotionData().isUpgraded() ? 1 : 0), true);
			}
		}
	}
}
