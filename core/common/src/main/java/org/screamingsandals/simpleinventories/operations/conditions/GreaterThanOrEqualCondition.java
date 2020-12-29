package org.screamingsandals.simpleinventories.operations.conditions;

import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public class GreaterThanOrEqualCondition extends AbstractCondition {

	public GreaterThanOrEqualCondition(InventorySet format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(PlayerWrapper player, Object obj1, Object obj2, PlayerItemInfo info) {
		return new GreaterThanCondition(format, obj1, obj2).process(player, info)
				|| new EqualsCondition(format, obj1, obj2).process(player, info);
	}

}
