package org.screamingsandals.simpleguiformat.operations.conditions;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleguiformat.item.PlayerItemInfo;
import org.screamingsandals.simpleguiformat.operations.Operation;

public interface Condition extends Operation {
	default Object resolveFor(Player player, PlayerItemInfo info) {
		return process(player, info);
	}
	
	public boolean process(Player player, PlayerItemInfo info);
}
