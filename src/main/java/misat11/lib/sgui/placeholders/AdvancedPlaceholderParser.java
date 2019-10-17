package misat11.lib.sgui.placeholders;

import org.bukkit.entity.Player;

import misat11.lib.sgui.PlayerItemInfo;

public interface AdvancedPlaceholderParser {
	public String processPlaceholder(String key, Player player, PlayerItemInfo item, String[] arguments);
}
