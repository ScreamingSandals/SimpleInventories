package misat11.lib.sgui.placeholders;

import org.bukkit.entity.Player;

public class PlayerPlaceholderParser implements PlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, String[] arguments) {
		return player.getName();
	}

}
