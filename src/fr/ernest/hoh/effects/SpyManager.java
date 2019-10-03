package fr.ernest.hoh.effects;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ernest.hoh.entities.AbstractTotem;
import net.md_5.bungee.api.ChatColor;

public class SpyManager extends DailyEffect implements TotemManager {
	
	
	public static final String NAME = "Spy";
	public static float DROP_CHANCE = 0.3f;
	public static float SPEED_CHANCE = 0.7f;
	
	public static SpyManager instance = null;
	private BukkitRunnable spyRunnable;
	
	public SpyManager() {
		super(13000);
		if (instance == null) {
			instance = this;
		}
	}

	@Override
	public void start() {
		spyRunnable = new BukkitRunnable() {	
			@Override
			public void run() {
				AbstractTotem totem = plugin.getTotemsManager().getTotem(NAME);
				if (totem == null) return; 

				OfflinePlayer owner = totem.getOwner();
				if (owner == null) return;
				
				Player p = owner.getPlayer();
				if (p == null) return;
				
				Random r = new Random();
				
				float rd = r.nextFloat();
				if (rd < DROP_CHANCE) {
					PotionType type = (r.nextFloat() < SPEED_CHANCE) ? 
						PotionType.SPEED: PotionType.INVISIBILITY;
				
					ItemStack potion = new ItemStack(Material.POTION);
					PotionMeta meta = (PotionMeta) potion.getItemMeta();
					meta.setBasePotionData(new PotionData(type));
					potion.setItemMeta(meta);
					
					if(p.getInventory().addItem(potion).size() != 0) {
						p.getWorld().dropItem(p.getLocation(), potion);
					}
					p.sendMessage(ChatColor.DARK_GRAY + "Vous recevez une potion pour cette nuit");	
				}
			}
		};
		spyRunnable.runTaskTimer(plugin, getDelayBeforeTime(), 24000);
	}
}
