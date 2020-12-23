package org.screamingsandals.simpleinventories.bukkit.placeholders;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public class WorldPlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments) {
		World world = player.as(Player.class).getWorld();
		if (arguments.length >= 1) {
			switch(arguments[0]) {
				case "time":
					return Long.toString(world.getTime());
				case "difficulty":
					return world.getDifficulty().name();
				case "type":
				case "environment":
					return world.getEnvironment().name();
				case "weather":
					return world.getWorldType().name();
				case "pvp":
				case "ispvp":
					return Boolean.toString(world.getPVP());
			}
		}
		return world.getName();
	}
}
