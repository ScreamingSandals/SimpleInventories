package org.screamingsandals.simpleinventories.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.simpleinventories.placeholders.AdvancedPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PlaceholderConstantParser;
import org.screamingsandals.simpleinventories.placeholders.PlaceholderParser;
import org.screamingsandals.simpleinventories.utils.StackParser;

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
	private boolean animationsEnabled = false;
	private boolean showPageNumber = true;
	private Plugin animationPlugin = null;
	private String prefix = "Inventory";
	private Map<String, PlaceholderParser> placeholders = new HashMap<>();
	private Map<String, AdvancedPlaceholderParser> advancedPlaceholders = new HashMap<>();
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

	public ItemStack getBackItem() {
		return backItem;
	}

	public void setBackItem(ItemStack backItem) {
		this.backItem = backItem;
	}

	public ItemStack getPageBackItem() {
		return pageBackItem;
	}

	public void setPageBackItem(ItemStack pageBackItem) {
		this.pageBackItem = pageBackItem;
	}

	public ItemStack getPageForwardItem() {
		return pageForwardItem;
	}

	public void setPageForwardItem(ItemStack pageForwardItem) {
		this.pageForwardItem = pageForwardItem;
	}

	public ItemStack getCosmeticItem() {
		return cosmeticItem;
	}

	public void setCosmeticItem(ItemStack cosmeticItem) {
		this.cosmeticItem = cosmeticItem;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public boolean isGenericShop() {
		return genericShop;
	}

	public void setGenericShop(boolean genericShop) {
		this.genericShop = genericShop;
	}

	public boolean isGenericShopPriceTypeRequired() {
		return genericShopPriceTypeRequired;
	}

	public void setGenericShopPriceTypeRequired(boolean genericShopPriceTypeRequired) {
		this.genericShopPriceTypeRequired = genericShopPriceTypeRequired;
	}

	public boolean isAnimationsEnabled() {
		return animationsEnabled;
	}

	public void setAnimationsEnabled(boolean animationsEnabled, Plugin animationPlugin) {
		this.animationsEnabled = animationsEnabled;
		this.animationPlugin = animationPlugin;
	}

	public Plugin getAnimationPlugin() {
		return animationPlugin;
	}

	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getItems_on_row() {
		return items_on_row;
	}

	/* Can be very dangerous!! */
	@Deprecated
	public void setItems_on_row(int items_on_row) {
		this.items_on_row = items_on_row;
	}

	public int getRender_actual_rows() {
		return render_actual_rows;
	}

	public void setRender_actual_rows(int render_actual_rows) {
		this.render_actual_rows = render_actual_rows;
	}

	public int getRender_offset() {
		return render_offset;
	}

	public void setRender_offset(int render_offset) {
		this.render_offset = render_offset;
	}

	public int getRender_header_start() {
		return render_header_start;
	}

	public void setRender_header_start(int render_header_start) {
		this.render_header_start = render_header_start;
	}

	public int getRender_footer_start() {
		return render_footer_start;
	}

	public void setRender_footer_start(int render_footer_start) {
		this.render_footer_start = render_footer_start;
	}
	
	public Map<String, PlaceholderParser> getPlaceholders() {
		return placeholders;
	}
	
	public Map<String, AdvancedPlaceholderParser> getAdvancedPlaceholders() {
		return advancedPlaceholders;
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

	public boolean isShowPageNumber() {
		return showPageNumber;
	}

	public void setShowPageNumber(boolean showPageNumber) {
		this.showPageNumber = showPageNumber;
	}

	public boolean isAllowAccessToConsole() {
		return allowAccessToConsole;
	}

	public void setAllowAccessToConsole(boolean allowAccessToConsole) {
		this.allowAccessToConsole = allowAccessToConsole;
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
