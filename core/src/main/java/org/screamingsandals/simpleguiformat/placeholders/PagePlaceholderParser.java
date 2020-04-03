package org.screamingsandals.simpleguiformat.placeholders;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleguiformat.item.PlayerItemInfo;

public class PagePlaceholderParser implements AdvancedPlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, PlayerItemInfo item, String[] arguments) {
		if (arguments.length > 0) {
			if (arguments.length == 1) {
				switch(arguments[0]) {
					case "main":
						return item.getParent() == null ? "true" : "false";
					case "id":
						return item.getParent() != null ? item.getParent().getId() : "";
					case "number":
						return String.valueOf(item.getPosition() / item.getFormat().getItemsOnPage());
				}
			}
		}
		return "%" + key + "%"; // ignore this placeholder
	}

}
