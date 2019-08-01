package misat11.lib.sgui.operations.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.SimpleGuiFormat;
import misat11.lib.sgui.operations.Operation;

public abstract class AbstractCondition implements Condition {
	protected SimpleGuiFormat format;
	protected Object obj1;
	protected Object obj2;

	public AbstractCondition(SimpleGuiFormat format, Object obj1, Object obj2) {
		this.format = format;
		this.obj1 = obj1;
		this.obj2 = obj2;
	}

	@Override
	public boolean process(Player player) {
		Object ob1 = this.obj1;
		Object ob2 = this.obj2;
		if (ob1 instanceof Operation) {
			ob1 = ((Operation) ob1).resolveFor(player);
		}
		if (ob2 instanceof Operation) {
			ob2 = ((Operation) ob2).resolveFor(player);
		}
		if (ob1 instanceof String) {
			ob1 = format.processPlaceholders(player, (String) ob1);
		}
		if (ob2 instanceof String) {
			ob2 = format.processPlaceholders(player, (String) ob2);
		}
		return process(player, ob1, ob2);
	}

	protected abstract boolean process(Player player, Object obj1, Object obj2);

	@Override
	public String toString() {
		return this.getClass().getName() + "[format=" + format + ";obj1=" + obj1 + ";obj2=" + obj2 + "]";
	}
}
