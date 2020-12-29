package org.screamingsandals.simpleinventories.placeholders;

import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public class PagePlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments) {
		if (arguments.length > 0) {
			if (arguments.length == 1) {
				switch(arguments[0]) {
					case "main":
						return item.getParent().isMain() ? "true" : "false";
					case "id":
						return item.getParent().isMain() ? "" : item.getParent().getItemOwner().getId();
					case "number":
						return String.valueOf(item.getPosition() / (item.getParent() != null ? item.getParent().getLocalOptions().getItemsOnPage() : item.getFormat().getMainSubInventory().getLocalOptions().getItemsOnPage()));
				}
			}
		}
		return "%" + key + "%"; // ignore this placeholder
	}

}
