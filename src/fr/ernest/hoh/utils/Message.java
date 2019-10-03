package fr.ernest.hoh.utils;

import org.bukkit.ChatColor;

public enum Message {
	
	TOTEM_CREATED(
		ChatColor.GREEN + "Le totem " 
		+ ChatColor.YELLOW + "${0}"
		+ ChatColor.GREEN + " a été créé en "
		+ ChatColor.AQUA + "${1} ${2} ${3}"
	),
	
	TOTEM_ALREADY_EXISTS(
		ChatColor.RED + "Le totem " 
		+ ChatColor.YELLOW + "${0}"
		+ ChatColor.RED + " existe déjà "
	), 
	
	TOTEM_REMOVED (
		ChatColor.GREEN + "Le totem " 
		+ ChatColor.YELLOW + "${0}"
		+ ChatColor.GREEN + " a été supprimé"
	),
		
	TOTEM_NOT_EXISTS(
		ChatColor.RED + "Le totem "
		+ ChatColor.YELLOW + "${0}"
		+ ChatColor.RED + " n'existe pas"
	),

	TOTEM_ALREADY_OWNED(
		ChatColor.YELLOW + "${0}"
		+ ChatColor.RED + " possède déjà  le totem "
		+ ChatColor.YELLOW + "${1}"
	),
	
	TOTEM_REVOKED(
		ChatColor.YELLOW + "${0}"
		+ ChatColor.GOLD + " a perdu le totem "
		+ ChatColor.YELLOW + "${1}"
	),
	
	TOTEM_TAKEN(
		ChatColor.GOLD + "Le totem "
		+ ChatColor.YELLOW + "${0}"
		+ ChatColor.GOLD + " a été pris par "
		+ ChatColor.YELLOW + "${1}"
	);

	private String text;

	private Message(String text) {
		this.text = text;
	}

	public String text() {
		return text;
	}

	public String format(Object... args) {

		String formated = "" + text;

		for (int i = 0; i < args.length; i++) {
			String search = "${" + i + "}";
			boolean found = formated.contains(search);
			if (!found)
				break;

			formated = formated.replace(search, args[i].toString());
		}

		return formated;
	}
}