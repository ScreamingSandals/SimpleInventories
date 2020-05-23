package org.screamingsandals.simpleinventories.inventory;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.simpleinventories.placeholders.AdvancedPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PlaceholderConstantParser;
import org.screamingsandals.simpleinventories.placeholders.PlaceholderParser;

@Getter
@Setter
@RequiredArgsConstructor
public class Options extends LocalOptions {

	private final Plugin plugin;

	private boolean genericShop = false;
	private boolean genericShopPriceTypeRequired = true;
	private boolean animationsEnabled = false;
	private boolean showPageNumber = true;
	private String prefix = "Inventory";
	private final Map<String, PlaceholderParser> placeholders = new HashMap<>();
	private final Map<String, AdvancedPlaceholderParser> advancedPlaceholders = new HashMap<>();
	private boolean allowAccessToConsole = false;
	private boolean allowBungeecordPlayerSending = false;

	public boolean registerPlaceholder(String name, String value) {
		return registerPlaceholder(name, new PlaceholderConstantParser(value));
	}

	public boolean registerPlaceholder(String name, PlaceholderParser parser) {
		if (name.contains(".") || name.contains(":") || name.contains("%") || name.contains(" ")) {
			return false;
		}
		placeholders.put(name, parser);
		return true;
	}

	public boolean registerPlaceholder(String name, AdvancedPlaceholderParser parser) {
		if (name.contains(".") || name.contains(":") || name.contains("%") || name.contains(" ")) {
			return false;
		}
		advancedPlaceholders.put(name, parser);
		return true;
	}
	
	public static Options deserialize(ConfigurationSection map, Plugin plugin) {
		Options options = new Options(plugin);

		options.deserializeInternal(map);

		return options;
	}

	@Override
	protected void deserializeInternal(ConfigurationSection map) {
		super.deserializeInternal(map);

		entry(map, "genericShop", entry -> setGenericShop((boolean) entry));
		entry(map, "genericShopPriceTypeRequired", entry -> setGenericShopPriceTypeRequired((boolean) entry));
		entry(map, "animationsEnabled", entry -> setAnimationsEnabled((boolean) entry));
		entry(map, "showPageNumber", entry -> setShowPageNumber((boolean) entry));
		entry(map, "prefix", entry -> setPrefix(entry.toString()));
		entry(map, "allowAccessToConsole", entry -> setAllowAccessToConsole((boolean) entry));
	}
}
