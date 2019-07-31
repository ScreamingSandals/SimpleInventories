package misat11.lib.sgui.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.SimpleGuiFormat;

public class BooleanCondition implements Condition {
	protected SimpleGuiFormat format;
	protected Object obj;
	
	public BooleanCondition(SimpleGuiFormat format, Object obj) {
		this.format = format;
		this.obj = obj;
	}

	@Override
	public boolean process(Player player) {
		Object ob = obj;
		if (ob instanceof Condition) {
			return ((Condition) ob).process(player);
		}
		if (ob instanceof Boolean) {
			return ((Boolean) ob).booleanValue() == true;
		}
		if (ob instanceof String) {
			return !((String) ob).isBlank();
		}
		if (ob instanceof Number) {
			return ((Number) ob).doubleValue() != 0;
		}
		return ob != null;
	}
}
