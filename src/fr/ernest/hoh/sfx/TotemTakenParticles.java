package fr.ernest.hoh.sfx;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ernest.hoh.entities.AbstractTotem;

public class TotemTakenParticles {
	private AbstractTotem totem;
	private Plugin plugin;
	
	public TotemTakenParticles(Plugin p, AbstractTotem t) {
		plugin = p;
		totem = t;
	}
	
	/**
	 * Starts the animation 
	 */
	public void start() {
		new BukkitRunnable() {
			
			private float RAD = 1.0f;
			private float angle = 0.0f;
			private long time = 0;
			private Location offset = new Location(totem.getLocation().getWorld(), 0.5, 0.5, 0.5);
			
			@Override
			public void run() {
				if (time++ > 20 * 4) this.cancel();
				System.out.println(time);
				angle += Math.PI / 8;
				RAD -= 0.03;
				
				Location loc = totem.getLocation().clone();
				loc.add(offset);
				loc.setY(loc.getY() + (double)time / 8.0);
				loc.setX(loc.getX() + Math.cos(angle) * RAD);
				loc.setZ(loc.getZ() + Math.sin(angle) * RAD);
				loc.getWorld().spawnParticle(Particle.FLAME, loc, 0);
				
				Location loc2 = totem.getLocation().clone();
				loc2.add(offset);
				loc2.setY(loc2.getY() + (double)time / 8.0);
				loc2.setX(loc2.getX() + Math.cos(angle) * -RAD);
				loc2.setZ(loc2.getZ() + Math.sin(angle) * -RAD);
				loc.getWorld().spawnParticle(Particle.FLAME, loc2, 0);
			}
		}.runTaskTimerAsynchronously(plugin, 0, 1);
	}
}
