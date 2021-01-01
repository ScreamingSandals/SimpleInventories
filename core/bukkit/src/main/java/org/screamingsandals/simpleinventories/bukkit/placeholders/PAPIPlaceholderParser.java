package org.screamingsandals.simpleinventories.bukkit.placeholders;

import org.bukkit.Bukkit;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.lib.player.PlayerWrapper;

public class PAPIPlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments) {
		var format = String.join(".", arguments);
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			var hooks = PlaceholderAPI.getPlaceholders();
			int index = format.indexOf("_");
			if (index <= 0 || index >= format.length()) {
				return "%" + format + "%"; // ignore this placeholder
			}
			var identifier = format.substring(0, index).toLowerCase();
			var params = format.substring(index + 1);
			if (hooks.containsKey(identifier)) {
				var value = hooks.get(identifier).onPlaceholderRequest(player.as(Player.class), params);
				if (value != null) {
					return value;
				}
			}
		}
		return "%" + format + "%"; // ignore this placeholder
	}
}
