package misat11.lib.sgui.placeholders;

import java.util.Map;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

public class PAPIPlaceholderParser implements PlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, String[] arguments) {
		String format = String.join(".", arguments);
		Map<String, PlaceholderHook> hooks = PlaceholderAPI.getPlaceholders();
		int index = format.indexOf("_");
		if (index <= 0 || index >= format.length()) {
			return "%" + format + "%"; // ignore this placeholder
		}
		String identifier = format.substring(0, index).toLowerCase();
		String params = format.substring(index + 1);
		if (hooks.containsKey(identifier)) {
			String value = hooks.get(identifier).onRequest(player, params);
			if (value != null) {
				return value;
			}
		}
		return "%" + format + "%"; // ignore this placeholder
	}

}
