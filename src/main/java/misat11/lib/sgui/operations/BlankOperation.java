package misat11.lib.sgui.operations;

import org.bukkit.entity.Player;

import misat11.lib.sgui.SimpleGuiFormat;

public class BlankOperation implements Operation {

	private SimpleGuiFormat format;
	private Object obj;
	
	public BlankOperation(SimpleGuiFormat format, Object obj) {
		this.format = format;
		this.obj = obj;
	}
	
	public Object getBlankObject() {
		return this.obj;
	}
	
	@Override
	public Object resolveFor(Player player) {
		Object ob = this.obj;
		if (ob instanceof Operation) {
			ob = ((Operation) ob).resolveFor(player);
		}
		if (ob instanceof String) {
			ob = format.processPlaceholders(player, (String) ob);
		}
		return ob;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "[format=" + format + ";obj=" + obj + "]";
	}
	
}
