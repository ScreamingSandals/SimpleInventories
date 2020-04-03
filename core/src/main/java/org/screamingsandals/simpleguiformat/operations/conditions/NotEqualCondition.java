package org.screamingsandals.simpleguiformat.operations.conditions;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleguiformat.SimpleGuiFormat;
import org.screamingsandals.simpleguiformat.item.PlayerItemInfo;

public class NotEqualCondition extends EqualsCondition {

	public NotEqualCondition(SimpleGuiFormat format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(Player player, Object obj1, Object obj2, PlayerItemInfo info) {
		return !super.process(player, obj1, obj2, info);
	}

}
