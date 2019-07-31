package misat11.lib.sgui.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.SimpleGuiFormat;

public class GreaterThanOrEqualCondition extends AbstractCondition {

	public GreaterThanOrEqualCondition(SimpleGuiFormat format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(Player player, Object obj1, Object obj2) {
		return new GreaterThanCondition(format, obj1, obj2).process(player)
				|| new EqualsCondition(format, obj1, obj2).process(player);
	}

}
