package misat11.lib.sgui.operations.conditions;

import org.bukkit.entity.Player;

import misat11.lib.sgui.PlayerItemInfo;
import misat11.lib.sgui.SimpleGuiFormat;

public class NotFullEqualsCondition extends FullEqualsCondition {

	public NotFullEqualsCondition(SimpleGuiFormat format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}
	
	@Override
	protected boolean process(Player player, Object obj1, Object obj2, PlayerItemInfo info) {
		return !super.process(player, obj1, obj2, info);
	}

}
