package org.screamingsandals.simpleinventories.operations.conditions;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

public class AndCondition extends AbstractCondition {

	public AndCondition(SimpleInventories format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(Player player, Object obj1, Object obj2, PlayerItemInfo info) {
		return new BooleanCondition(format, obj1).process(player, info)
			&& new BooleanCondition(format, obj2).process(player, info);
	}

}
