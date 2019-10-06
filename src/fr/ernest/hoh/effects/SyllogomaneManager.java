package fr.ernest.hoh.effects;

import java.util.Random;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.entities.AbstractTotem;

public class SyllogomaneManager implements Listener {

	public static final String NAME = "Syllogomane";
	public static float ITEM_DROP_RATE = 0.2f;
	
	private HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	private Random random;

	{
		random = new Random();
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setKeepInventory(true);
		
		Player dead = (Player) event.getEntity();
		Player killer = dead.getKiller();
		
		AbstractTotem totem = plugin.getTotemsManager().getTotem(NAME);
		if (totem == null) return;
		
		OfflinePlayer owner = totem.getOwner();
		
		if (owner != null) {
			Player syllgm = plugin.getServer().getPlayer(owner.getName());
			if (syllgm != null) {
				if (syllgm.equals(dead)) return;
			}
		}
		
		//if (killer == null) return;
		
		PlayerInventory inv = dead.getInventory();
		for (int i = 0; i < inv.getSize(); i++) {
			ItemStack it = inv.getItem(i);
			if (it == null) continue;
			
			if (random.nextFloat() < ITEM_DROP_RATE) {
				inv.clear(i);
				dead.getWorld().dropItem(dead.getLocation(), it);
			}
		}
	}
}
