package fr.ernest.hoh.effects;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.entities.AbstractTotem;
import fr.ernest.hoh.entities.AbstractTotem;

public class ThiefManager implements Listener {
	
	private static HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		
		AbstractTotem totem = plugin.getTotemsManager().getTotem("Thief");
		if (totem == null) return;
		
		OfflinePlayer owner = totem.getOwner();
		if (owner == null) return;
		
		Player thief = owner.getPlayer();
		if (thief == null) return;
		
		Entity damaged = e.getEntity();
		if (!(damaged instanceof Player)) return;
		
		Entity damager = e.getDamager();
		if (!(damager instanceof Player)) return;
		
		Player victim = (Player) damaged;
		
		if (thief.equals((Player) damager)) {
			if (victim.getInventory().contains(Material.EMERALD)) {
				int slot = victim.getInventory().first(Material.EMERALD);
			}
		}
	}
}
