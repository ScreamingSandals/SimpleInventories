package misat11.lib.sgui.placeholders;

import org.bukkit.entity.Player;

public class PermissionPlaceholderParser implements PlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, String[] arguments) {
		if (arguments.length >= 2) {
			String[] permission = new String[arguments.length - 1];
			for (int i = 1; i < arguments.length; i++) {
				permission[i - 1] = arguments[i];
			}
			String perms = String.join(".", permission);
			if (arguments[0].equalsIgnoreCase("has")) {
				return Boolean.toString(player.hasPermission(perms));
			}
		}
		return "";
	}

}
