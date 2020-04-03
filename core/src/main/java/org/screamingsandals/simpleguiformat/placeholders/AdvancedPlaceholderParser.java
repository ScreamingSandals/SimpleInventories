package org.screamingsandals.simpleguiformat.placeholders;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleguiformat.item.PlayerItemInfo;

public interface AdvancedPlaceholderParser {
	public String processPlaceholder(String key, Player player, PlayerItemInfo item, String[] arguments);
}
