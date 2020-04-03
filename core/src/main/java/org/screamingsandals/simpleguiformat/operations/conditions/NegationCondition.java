package org.screamingsandals.simpleguiformat.operations.conditions;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleguiformat.SimpleGuiFormat;
import org.screamingsandals.simpleguiformat.item.PlayerItemInfo;

public class NegationCondition extends BooleanCondition {
	
	public NegationCondition(SimpleGuiFormat format, Object obj) {
		super(format, obj);
	}

	@Override
	public boolean process(Player player, PlayerItemInfo info) {
		return !super.process(player, info);
	}
}
