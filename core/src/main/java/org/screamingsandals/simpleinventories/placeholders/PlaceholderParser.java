package org.screamingsandals.simpleinventories.placeholders;

import org.bukkit.entity.Player;

public interface PlaceholderParser {
	public String processPlaceholder(String key, Player player, String[] arguments);
}
