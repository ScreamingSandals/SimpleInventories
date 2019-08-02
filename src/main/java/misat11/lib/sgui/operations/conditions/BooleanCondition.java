package misat11.lib.sgui.operations.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.SimpleGuiFormat;
import misat11.lib.sgui.operations.Operation;

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
		if (ob instanceof Operation) {
			ob = ((Operation) ob).resolveFor(player);
		}
		if (ob instanceof Boolean) {
			return (Boolean) ob;
		}
		if (ob instanceof String) {
			ob = format.processPlaceholders(player, (String) ob);
			return !((String) ob).isEmpty() && !"false".equalsIgnoreCase((String) ob)
					&& !"null".equalsIgnoreCase((String) ob);
		}
		if (ob instanceof Number) {
			return ((Number) ob).doubleValue() != 0;
		}
		return ob != null;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "[format=" + format + ";obj=" + obj + "]";
	}
}
