package misat11.lib.sgui.operations.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.SimpleGuiFormat;

public class NegationCondition extends BooleanCondition {
	
	public NegationCondition(SimpleGuiFormat format, Object obj) {
		super(format, obj);
	}

	@Override
	public boolean process(Player player) {
		return !super.process(player);
	}
}
