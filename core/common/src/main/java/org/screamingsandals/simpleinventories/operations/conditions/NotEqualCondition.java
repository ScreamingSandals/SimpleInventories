package org.screamingsandals.simpleinventories.operations.conditions;


import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public class NotEqualCondition extends EqualsCondition {

	public NotEqualCondition(InventorySet format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(PlayerWrapper player, Object obj1, Object obj2, PlayerItemInfo info) {
		return !super.process(player, obj1, obj2, info);
	}

}
