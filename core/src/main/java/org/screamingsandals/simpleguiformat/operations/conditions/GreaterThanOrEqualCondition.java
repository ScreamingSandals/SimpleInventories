package org.screamingsandals.simpleguiformat.operations.conditions;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleguiformat.SimpleGuiFormat;
import org.screamingsandals.simpleguiformat.item.PlayerItemInfo;

public class GreaterThanOrEqualCondition extends AbstractCondition {

	public GreaterThanOrEqualCondition(SimpleGuiFormat format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(Player player, Object obj1, Object obj2, PlayerItemInfo info) {
		return new GreaterThanCondition(format, obj1, obj2).process(player, info)
				|| new EqualsCondition(format, obj1, obj2).process(player, info);
	}

}
