package fr.ernest.hoh.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.ernest.hoh.HallOfHonor;

public class SoundManager {
	private static HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	
	public static void playToAllPlayers(Sound sound, float pitch) {
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			p.playSound(p.getLocation(), sound, 0.8f, pitch);
		}
	}
}
