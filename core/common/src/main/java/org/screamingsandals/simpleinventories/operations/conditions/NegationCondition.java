package org.screamingsandals.simpleinventories.operations.conditions;

import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.lib.player.PlayerWrapper;

public class NegationCondition extends BooleanCondition {
	
	public NegationCondition(InventorySet format, Object obj) {
		super(format, obj);
	}

	@Override
	public boolean process(PlayerWrapper player, PlayerItemInfo info) {
		return !super.process(player, info);
	}
}
