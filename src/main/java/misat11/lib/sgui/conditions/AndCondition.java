package misat11.lib.sgui.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.SimpleGuiFormat;

public class AndCondition extends AbstractCondition {

	public AndCondition(SimpleGuiFormat format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(Player player, Object obj1, Object obj2) {
		return new BooleanCondition(format, obj1).process(player) && new BooleanCondition(format, obj2).process(player);
	}

}
