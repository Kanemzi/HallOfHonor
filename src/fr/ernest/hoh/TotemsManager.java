package fr.ernest.hoh;

import java.util.HashMap;

import fr.ernest.commands.CreateTotemCommand;
import fr.ernest.commands.RemoveTotemCommand;
import fr.ernest.hoh.entities.AbstractTotem;

public class TotemsManager {

	private HallOfHonor plugin = HallOfHonor.getPlugin(HallOfHonor.class);

	private HashMap<String, AbstractTotem> totems;

	public void setup() {
		totems = new HashMap<>();

		plugin.getCommand(CreateTotemCommand.NAME).setExecutor(new CreateTotemCommand());
		plugin.getCommand(RemoveTotemCommand.NAME).setExecutor(new RemoveTotemCommand());
		plugin.getStoreManager().addStore("totems");
	}

	public HashMap<String, AbstractTotem> getTotems() {
		return totems;
	}
}
