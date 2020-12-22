package org.screamingsandals.simpleinventories.operations.conditions;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;
import org.screamingsandals.simpleinventories.operations.Operation;

public interface Condition extends Operation {
	default Object resolveFor(Player player, PlayerItemInfo info) {
		return process(player, info);
	}
	
	public boolean process(Player player, PlayerItemInfo info);
}
