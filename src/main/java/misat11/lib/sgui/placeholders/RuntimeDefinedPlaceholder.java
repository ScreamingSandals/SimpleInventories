package misat11.lib.sgui.placeholders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import misat11.lib.sgui.PlayerItemInfo;

public class RuntimeDefinedPlaceholder implements AdvancedPlaceholderParser {

	private Map<String, String> map = new HashMap<>();
	private String defstr = null;
	
	@Override
	public String processPlaceholder(String key, Player player, PlayerItemInfo item, String[] arguments) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String keys = entry.getKey();
			String value = entry.getValue();
			String[] split = keys.split("(?<!\\.)\\.(?!\\.)");
			if (split.length <= arguments.length) {
				List<String> dollars = new ArrayList<>();
				for (int i = 0; i < arguments.length && i < split.length; i++) {
					if (!split[i].equals("$") && !arguments[i].equals(split[i])) {
						break;
					}
					if (split[i].equals("$")) {
						dollars.add(arguments[i]);
					}
					if (i == (arguments.length - 1)) {
						String parsedOutput = "";
						int lastIndexOfEscape = -2;
						for (int j = 0; j < value.length(); j++) {
							if (lastIndexOfEscape != j - 1 && value.charAt(j) == '$' && !dollars.isEmpty()) {
								parsedOutput += dollars.get(0);
								dollars.remove(0);
							} else if (lastIndexOfEscape != j - 1 && value.charAt(j) == '\\') {
								lastIndexOfEscape = j;
							} else {
								parsedOutput += value.charAt(j);
							}
						}
						return item.getFormat().processPlaceholders(player, parsedOutput, item);
					}
				}
			}
		}
		if (this.defstr != null) {
			return item.getFormat().processPlaceholders(player, this.defstr, item);
		}
		return null;
	}
	
	public void putDefault(String defstr) {
		this.defstr = defstr;
	}
	
	public void register(String keys, String value) {
		map.put(keys, value);
	}
	
	public void unregister(String keys) {
		map.remove(keys);
	}
	
}
