package org.screamingsandals.simpleinventories.operations;

import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.lib.player.PlayerWrapper;

public interface Operation {
	@Deprecated
	default Object resolveFor(PlayerWrapper player) {
		return resolveFor(player, null);
	}
	
	Object resolveFor(PlayerWrapper player, PlayerItemInfo info);
}
