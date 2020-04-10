package org.screamingsandals.simpleinventories.placeholders;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

public interface AdvancedPlaceholderParser {
	public String processPlaceholder(String key, Player player, PlayerItemInfo item, String[] arguments);
}
