package fr.ernest.hoh.effects;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ernest.hoh.HallOfHonor;
import fr.ernest.hoh.entities.AbstractTotem;
import fr.ernest.hoh.utils.Message;
import fr.ernest.hoh.utils.SoundManager;
import net.md_5.bungee.api.ChatColor;

public class TaxmanManager extends DailyEffect implements TotemManager {
	
	public static final String NAME = "Taxman";
	public static int MIN_EMERALDS = 2;
	public static int MAX_EMERALDS = 5;
	
	public static TaxmanManager instance = null;
	private BukkitRunnable taxmanRunnable;
	
	public TaxmanManager() {
		super(0);
		if (instance == null) {
			instance = this;
		}
	}

	@Override
	public void start() {
		taxmanRunnable = new BukkitRunnable() {	
			@Override
			public void run() {
				AbstractTotem totem = plugin.getTotemsManager().getTotem(NAME);
				if (totem == null) return;
				
				OfflinePlayer owner = totem.getOwner();
				if (owner == null) return;
				
				Player p = plugin.getServer().getPlayer(owner.getName());
				if (p == null) return;
				
				Random r = new Random();
				
				int emeraldCount = MIN_EMERALDS + r.nextInt(MAX_EMERALDS - MIN_EMERALDS + 1);
				ItemStack emeralds = new ItemStack(Material.EMERALD, emeraldCount);
				
				HashMap<Integer, ItemStack> rest = p.getInventory().addItem(emeralds);
				
				if(rest.size() != 0) {
					p.getWorld().dropItem(p.getLocation(), rest.get(0));
				}
				
				p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
				p.sendMessage(Message.TAXES_COLLECTED.format(emeraldCount));
			}
		};
		taxmanRunnable.runTaskTimer(plugin, getDelayBeforeTime(), 24000);
	}
}
