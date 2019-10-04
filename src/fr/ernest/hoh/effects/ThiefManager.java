package fr.ernest.hoh.effects;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.entities.AbstractTotem;

public class ThiefManager implements Listener {
	
	public static final String NAME = "Thief";
	private static final float DROP_RATE = 0.7f;
	
	private static HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	
	private Random random;
	
	public ThiefManager() {
		random = new Random();
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		AbstractTotem totem = plugin.getTotemsManager().getTotem(NAME);
		if (totem == null) return;
		
		OfflinePlayer owner = totem.getOwner();
		if (owner == null) return;
		
		Player thief = plugin.getServer().getPlayer(owner.getName());
		if (thief == null) return;
		
		Entity damaged = e.getEntity();
		if (!(damaged instanceof Player)) return;
		
		Entity damager = e.getDamager();
		if (!(damager instanceof Player)) return;
		
		Player victim = (Player) damaged;
		if (thief.equals(damager)) {
			
			if (random.nextFloat() > DROP_RATE) return;
			
			if (victim.getInventory().contains(Material.EMERALD)) {
				int slot = victim.getInventory().first(Material.EMERALD);

				ItemStack emeraldStack = victim.getInventory().getItem(slot);
				emeraldStack.setAmount(emeraldStack.getAmount() - 1);
				
				ItemStack emerald = new ItemStack(Material.EMERALD);
				
				Item dropped = victim.getWorld().dropItem(victim.getLocation(), emerald);
				dropped.setVelocity(damager.getLocation().subtract(victim.getLocation()).toVector().normalize());
			}
		}
	}
}
