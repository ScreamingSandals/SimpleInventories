package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleinventories.utils.StackParser;

import java.util.function.Consumer;

@Data
@NoArgsConstructor
public class LocalOptions {

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

	private int rows = ROWS;
	private int items_on_row = ITEMS_ON_ROW;
	private int render_actual_rows = RENDER_ACTUAL_ROWS;
	private int render_offset = RENDER_OFFSET;
	private int render_header_start = RENDER_HEADER_START;
	private int render_footer_start = RENDER_FOOTER_START;

	public LocalOptions(LocalOptions parent) {
		setBackItem(parent.getBackItem().clone());
		setPageBackItem(parent.getPageBackItem().clone());
		setPageForwardItem(parent.getPageForwardItem().clone());
		setCosmeticItem(parent.getCosmeticItem().clone());

		setRows(parent.getRows());
		setItems_on_row(parent.getItems_on_row());
		setRender_actual_rows(parent.getRender_actual_rows());
		setRender_offset(parent.getRender_offset());
		setRender_header_start(parent.getRender_header_start());
		setRender_footer_start(parent.getRender_footer_start());
	}

	public int getItemsOnPage() {
		return items_on_row * rows;
	}
	
	public static LocalOptions deserialize(ConfigurationSection map) {
		LocalOptions options = new LocalOptions();

		options.deserializeInternal(map);
		
		return options;
	}

	public static LocalOptions deserialize(LocalOptions parent, ConfigurationSection map) {
		LocalOptions options = new LocalOptions(parent);

		options.deserializeInternal(map);

		return options;
	}

	protected void deserializeInternal(ConfigurationSection map) {
		entry(map, "backItem", entry -> setBackItem(StackParser.parse(entry)));
		entry(map, "pageBackItem", entry -> setPageBackItem(StackParser.parse(entry)));
		entry(map, "pageForwardItem", entry -> setPageForwardItem(StackParser.parse(entry)));
		entry(map, "cosmeticItem", entry -> setCosmeticItem(StackParser.parse(entry)));

		// DANGER
		entry(map, "rows", entry -> setRows(((Number) entry).intValue()));
		entry(map, "render_actual_rows", entry -> setRender_actual_rows(((Number) entry).intValue()));
		entry(map, "render_offset", entry -> setRender_offset(((Number) entry).intValue()));
		entry(map, "render_header_start", entry -> setRender_header_start(((Number) entry).intValue()));
		entry(map, "render_footer_start", entry -> setRender_footer_start(((Number) entry).intValue()));

		// MOST DANGER
		entry(map, "items_on_row", entry -> setItems_on_row(((Number) entry).intValue()));
	}
	
	protected static void entry(ConfigurationSection map, String path, Consumer<Object> consumer) {
		if (map.contains(path)) {
			try {
				consumer.accept(map.get(path));
			} catch (Throwable ignored) {
			}
		}
	}
}
