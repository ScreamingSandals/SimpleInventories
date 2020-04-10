package org.screamingsandals.simpleinventories.operations;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

public interface Operation {
	@Deprecated
	default Object resolveFor(Player player) {
		return resolveFor(player, null);
	}
	
	public Object resolveFor(Player player, PlayerItemInfo info);
}
