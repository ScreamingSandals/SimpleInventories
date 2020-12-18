package org.screamingsandals.simpleinventories.operations.conditions;

import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.operations.Operation;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public interface Condition extends Operation {
	default Object resolveFor(PlayerWrapper player, PlayerItemInfo info) {
		return process(player, info);
	}
	
	boolean process(PlayerWrapper player, PlayerItemInfo info);
}
