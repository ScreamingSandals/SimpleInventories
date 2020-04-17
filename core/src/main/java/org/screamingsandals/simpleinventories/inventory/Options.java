package org.screamingsandals.simpleinventories.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.simpleinventories.placeholders.AdvancedPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PlaceholderConstantParser;
import org.screamingsandals.simpleinventories.placeholders.PlaceholderParser;
import org.screamingsandals.simpleinventories.utils.StackParser;

@Data
public class Options {

	// RENDER CONSTANTS
	public static final int ROWS = 4;
	public static final int ITEMS_ON_ROW = 9;
	public static final int RENDER_ACTUAL_ROWS = 6;
	public static final int RENDER_OFFSET = ITEMS_ON_ROW;
	public static final int RENDER_HEADER_START = 0;
	public static final int RENDER_FOOTER_START = 45;
	
	private ItemStack backItem = new ItemStack(Material.BARRIER);
	private ItemStack pageBackItem = new ItemStack(Material.ARROW);
	private ItemStack pageForwardItem = new ItemStack(Material.ARROW);
	private ItemStack cosmeticItem = new ItemStack(Material.AIR);
	private boolean genericShop = false;
	private boolean genericShopPriceTypeRequired = true;
	@Setter(AccessLevel.NONE)
	private boolean animationsEnabled = false;
	private boolean showPageNumber = true;
	@Setter(AccessLevel.NONE)
	private Plugin animationPlugin = null;
	private String prefix = "Inventory";
	private final Map<String, PlaceholderParser> placeholders = new HashMap<>();
	private final Map<String, AdvancedPlaceholderParser> advancedPlaceholders = new HashMap<>();
	private boolean allowAccessToConsole = false;
	// Render
	private int rows = ROWS;
	private int items_on_row = ITEMS_ON_ROW;
	private int render_actual_rows = RENDER_ACTUAL_ROWS;
	private int render_offset = RENDER_OFFSET;
	private int render_header_start = RENDER_HEADER_START;
	private int render_footer_start = RENDER_FOOTER_START;
	
	public Options() {
		
	}

	public void setAnimationsEnabled(boolean animationsEnabled, Plugin animationPlugin) {
		this.animationsEnabled = animationsEnabled;
		this.animationPlugin = animationPlugin;
	}

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
		Options options = new Options();
	
		entry(map, "backItem", entry -> options.setBackItem(StackParser.parse(entry)));
		entry(map, "pageBackItem", entry -> options.setPageBackItem(StackParser.parse(entry)));
		entry(map, "pageForwardItem", entry -> options.setPageForwardItem(StackParser.parse(entry)));
		entry(map, "cosmeticItem", entry -> options.setCosmeticItem(StackParser.parse(entry)));
		
		entry(map, "genericShop", entry -> options.setGenericShop((boolean) entry));
		entry(map, "genericShopPriceTypeRequired", entry -> options.setGenericShopPriceTypeRequired((boolean) entry));
		entry(map, "animationsEnabled", entry -> options.setAnimationsEnabled((boolean) entry, plugin));
		entry(map, "showPageNumber", entry -> options.setShowPageNumber((boolean) entry));
		entry(map, "prefix", entry -> options.setPrefix(entry.toString()));
		entry(map, "allowAccessToConsole", entry -> options.setAllowAccessToConsole((boolean) entry));
		
		// DANGER
		entry(map, "rows", entry -> options.setRows(((Number) entry).intValue()));
		entry(map, "render_actual_rows", entry -> options.setRender_actual_rows(((Number) entry).intValue()));
		entry(map, "render_offset", entry -> options.setRender_offset(((Number) entry).intValue()));
		entry(map, "render_header_start", entry -> options.setRender_header_start(((Number) entry).intValue()));
		entry(map, "render_footer_start", entry -> options.setRender_footer_start(((Number) entry).intValue()));
		
		// MOST DANGER
		entry(map, "items_on_row", entry -> options.setItems_on_row(((Number) entry).intValue()));
		
		return options;
	}
	
	private static void entry(ConfigurationSection map, String path, Consumer<Object> consumer) {
		if (map.contains(path)) {
			try {
				consumer.accept(map.get(path));
			} catch (Throwable ignored) {
			}
		}
	}
}
