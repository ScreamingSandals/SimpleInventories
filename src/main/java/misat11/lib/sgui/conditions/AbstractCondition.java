package misat11.lib.sgui.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.SimpleGuiFormat;

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
		if (ob1 instanceof Condition) {
			ob1 = ((Condition) ob1).process(player);
		}
		if (ob2 instanceof Condition) {
			ob2 = ((Condition) ob2).process(player);
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
}
