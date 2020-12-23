package org.screamingsandals.simpleinventories.bukkit.placeholders;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public class PlayerPlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, PlayerWrapper playerWrapper, PlayerItemInfo item, String[] arguments) {
		var player = playerWrapper.as(Player.class);
		if (arguments.length >= 1) {
			switch (arguments[0]) {
				case "exp":
				case "xp":
					return Float.toString(player.getExp());
				case "level":
					return Integer.toString(player.getLevel());
				case "displayName":
					return player.getDisplayName();
				case "tabName":
					return player.getPlayerListName();
				case "health":
					return Double.toString(player.getHealth());
				case "maxhealth":
					return Double.toString(player.getMaxHealth());
				case "isdead":
				case "dead":
					return Boolean.toString(player.isDead());
				case "isalive":
				case "alive":
					return Boolean.toString(!player.isDead());
				case "food":
					return Double.toString(player.getFoodLevel());
				case "firsttime":
					return Long.toString(player.getFirstPlayed());
				case "lasttime":
					return Long.toString(player.getLastPlayed());
				case "playertime":
					return Long.toString(player.getPlayerTime());
				case "gamemode":
					return player.getGameMode().name();
			}
		}
		return player.getName();
	}
}
