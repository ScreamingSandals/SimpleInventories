package misat11.lib.sgui.operations.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.SimpleGuiFormat;

public class EqualsCondition extends AbstractCondition {

	public EqualsCondition(SimpleGuiFormat format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(Player player, Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 instanceof String && obj2 instanceof String) {
			return ((String) obj1).equals((String) obj2);
		}
		if ((obj1 instanceof Number || obj2 instanceof Number) && (obj1 instanceof String || obj2 instanceof String)) {
			try {
				if (obj1 instanceof Number) {
					obj2 = Double.parseDouble((String) obj2);
				} else {
					obj1 = Double.parseDouble((String) obj1);
				}
				if (((Number) obj1).doubleValue() == ((Number) obj2).doubleValue()) {
					return true;
				}
			} catch (NumberFormatException ex) {
			}
		}
		return false;
	}

}
