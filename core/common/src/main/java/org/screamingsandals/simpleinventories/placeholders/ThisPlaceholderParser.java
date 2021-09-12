package org.screamingsandals.simpleinventories.placeholders;

import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.lib.player.PlayerWrapper;

public class ThisPlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments) {
		if (arguments.length > 0) {
			if (arguments.length == 1) {
				switch(arguments[0]) {
					case "stack":
						return item.getStack().getMaterial().platformName();
					case "visible":
						return String.valueOf(item.isVisible());
					case "disabled":
						return String.valueOf(item.isDisabled());
				}
			}
		}
		return "%" + key + "%"; // ignore this placeholder
	}

}
