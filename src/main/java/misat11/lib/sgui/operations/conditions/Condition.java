package misat11.lib.sgui.operations.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.operations.Operation;

public interface Condition extends Operation {
	default Object resolveFor(Player player) {
		return process(player);
	}
	
	public boolean process(Player player);
}
