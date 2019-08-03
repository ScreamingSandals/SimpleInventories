package misat11.lib.sgui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import misat11.lib.sgui.placeholders.PlaceholderConstantParser;
import misat11.lib.sgui.placeholders.PlaceholderParser;

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
	private Plugin animationPlugin = null;
	private String prefix = "Inventory";
	private Map<String, PlaceholderParser> placeholders = new HashMap<>();
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
}
