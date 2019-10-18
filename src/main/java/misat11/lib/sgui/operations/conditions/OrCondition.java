package misat11.lib.sgui.operations.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.PlayerItemInfo;
import misat11.lib.sgui.SimpleGuiFormat;

public class OrCondition extends AbstractCondition {

	public OrCondition(SimpleGuiFormat format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(Player player, Object obj1, Object obj2, PlayerItemInfo info) {
		return new BooleanCondition(format, obj1).process(player, info)
			|| new BooleanCondition(format, obj2).process(player, info);
	}

}
