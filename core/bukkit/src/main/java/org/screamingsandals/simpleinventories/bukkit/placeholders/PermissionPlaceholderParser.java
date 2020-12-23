package org.screamingsandals.simpleinventories.bukkit.placeholders;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public class PermissionPlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments) {
		if (arguments.length >= 2) {
			String[] permission = new String[arguments.length - 1];
			for (int i = 1; i < arguments.length; i++) {
				permission[i - 1] = arguments[i];
			}
			String perms = String.join(".", permission);
			if (arguments[0].equalsIgnoreCase("has")) {
				return Boolean.toString(player.as(Player.class).hasPermission(perms));
			}
		}
		return "";
	}
}
