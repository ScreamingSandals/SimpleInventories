package org.screamingsandals.simpleinventories.operations.conditions;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

public class NegationCondition extends BooleanCondition {
	
	public NegationCondition(SimpleInventories format, Object obj) {
		super(format, obj);
	}

	@Override
	public boolean process(Player player, PlayerItemInfo info) {
		return !super.process(player, info);
	}
}
