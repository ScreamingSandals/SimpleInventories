package misat11.lib.sgui.placeholders;

import org.bukkit.entity.Player;

import misat11.lib.sgui.MapReader;
import misat11.lib.sgui.PlayerItemInfo;

public class ThisPlaceholderParser implements AdvancedPlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, PlayerItemInfo item, String[] arguments) {
		if (arguments.length > 0) {
			if (arguments.length == 1) {
				switch(arguments[0]) {
					case "stack":
						return item.getStack().getType().name();
					case "visible":
						return String.valueOf(item.isVisible());
					case "disabled":
						return String.valueOf(item.isDisabled());
				}
			}
			try {
				MapReader reader = item.getReader();
				for (int i = 0; i < arguments.length; i++) {
					if (i == (arguments.length - 1)) {
						return reader.getString(arguments[i]);
					} else {
						reader = reader.getMap(arguments[i]);
					}
				}
			} catch (Throwable t) {
				// Placeholder is not valid
			}
		}
		return "%" + key + "%"; // ignore this placeholder
	}

}
