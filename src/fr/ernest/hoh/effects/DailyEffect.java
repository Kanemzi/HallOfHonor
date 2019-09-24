package fr.ernest.hoh.effects;

import fr.ernest.hoh.HallOfHonor;

public class DailyEffect {
	
	protected long time;
	protected static HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);
	
	
	/**
	 * @param time time of the day in tick
	 */
	public DailyEffect(long time) {
		this.time = time;
	}
	
	protected long getDelayBeforeTime() {
		
		long currentTime = plugin.getServer().getWorlds().get(0).getTime();
		
		long delay = time - currentTime;
		if (delay < 0) {
			delay += 24000;
		}
		
		return delay;
	}
}
