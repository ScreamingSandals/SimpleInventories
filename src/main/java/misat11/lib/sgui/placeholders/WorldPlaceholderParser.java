package misat11.lib.sgui.placeholders;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldPlaceholderParser implements PlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, String[] arguments) {
		World world = player.getWorld();
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
