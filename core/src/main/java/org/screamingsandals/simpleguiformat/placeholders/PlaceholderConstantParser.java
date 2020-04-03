package org.screamingsandals.simpleguiformat.placeholders;

import org.bukkit.entity.Player;

public class PlaceholderConstantParser implements PlaceholderParser {
	public String str;
	
	public PlaceholderConstantParser(String str) {
		this.str = str;
	}

	@Override
	public String processPlaceholder(String key, Player player, String[] arguments) {
		return this.str;
	}
}
