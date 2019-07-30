package misat11.lib.sgui.placeholders;

import org.bukkit.entity.Player;

public interface PlaceholderParser {
	public String processPlaceholder(String key, Player player, String[] arguments);
}
