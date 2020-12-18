package org.screamingsandals.simpleinventories.operations.conditions;

import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public class NegationCondition extends BooleanCondition {
	
	public NegationCondition(Inventory format, Object obj) {
		super(format, obj);
	}

	@Override
	public boolean process(PlayerWrapper player, PlayerItemInfo info) {
		return !super.process(player, info);
	}
}
