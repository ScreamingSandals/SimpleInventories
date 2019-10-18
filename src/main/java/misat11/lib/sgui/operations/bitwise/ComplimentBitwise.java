package misat11.lib.sgui.operations.bitwise;

import org.bukkit.entity.Player;

import misat11.lib.sgui.PlayerItemInfo;
import misat11.lib.sgui.SimpleGuiFormat;
import misat11.lib.sgui.operations.Operation;

public class ComplimentBitwise implements Operation {
	
	private SimpleGuiFormat format;
	private Object obj;

	public ComplimentBitwise(SimpleGuiFormat format, Object obj) {
		this.format = format;
		this.obj = obj;
	}

	@Override
	public Object resolveFor(Player player, PlayerItemInfo info) {
		Object ob = this.obj;
		if (ob instanceof Operation) {
			ob = ((Operation) ob).resolveFor(player, info);
		}
		if (ob instanceof String) {
			ob = format.processPlaceholders(player, (String) ob, info);
		}
		if (ob instanceof String) {
			try {
				ob = Double.parseDouble((String) ob);
			} catch (NumberFormatException ex) {
			}
		}
		if (ob instanceof Number) {
			return ~(((Number) ob).intValue());
		}
		return 0;
	}

}
