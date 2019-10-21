package misat11.lib.sgui.operations.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.PlayerItemInfo;
import misat11.lib.sgui.SimpleGuiFormat;

public class FullEqualsCondition extends AbstractCondition {

	public FullEqualsCondition(SimpleGuiFormat format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(Player player, Object obj1, Object obj2, PlayerItemInfo info) {
		if (obj1 instanceof String && obj2 instanceof String) {
			return obj1.equals(obj2);
		}
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return ((Number) obj1).doubleValue() == ((Number) obj2).doubleValue();
		}
		return obj1 == obj2;
	}

}
