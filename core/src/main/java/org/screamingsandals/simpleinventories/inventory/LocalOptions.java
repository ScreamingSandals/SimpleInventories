/*
 * Copyright (C) 2026 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.simpleinventories.utils.StackParser;

import java.util.Locale;
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

	private InventoryType inventoryType = InventoryType.CHEST;

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

		setInventoryType(parent.getInventoryType());
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

	@ApiStatus.Internal
	public void deserializeInternal(ConfigurationSection map) {
		entry(map, "backItem", entry -> setBackItem(StackParser.parse(entry)), "back-item");
		entry(map, "pageBackItem", entry -> setPageBackItem(StackParser.parse(entry)), "page-back-item");
		entry(map, "pageForwardItem", entry -> setPageForwardItem(StackParser.parse(entry)), "page-forward-item");
		entry(map, "cosmeticItem", entry -> setCosmeticItem(StackParser.parse(entry)), "cosmetic-item");

		// DANGER
		entry(map, "rows", entry -> setRows(((Number) entry).intValue()));
		entry(map, "render_actual_rows", entry -> setRender_actual_rows(((Number) entry).intValue()), "render-actual-rows");
		entry(map, "render_offset", entry -> setRender_offset(((Number) entry).intValue()), "render-offset");
		entry(map, "render_header_start", entry -> setRender_header_start(((Number) entry).intValue()), "render-header-start");
		entry(map, "render_footer_start", entry -> setRender_footer_start(((Number) entry).intValue()), "render-footer-start");
		entry(map, "inventoryType", entry -> setInventoryType(InventoryType.valueOf(entry.toString().toUpperCase(Locale.ROOT))), "inventory-type");

		// MOST DANGER
		entry(map, "items_on_row", entry -> setItems_on_row(((Number) entry).intValue()), "items-on-row");
	}
	
	protected static void entry(ConfigurationSection map, String path, Consumer<Object> consumer, String... altKeys) {
		if (map.contains(path)) {
			try {
				consumer.accept(map.get(path));
				return;
			} catch (Throwable ignored) {
			}
		}
		for (String altKey : altKeys) {
			if (map.contains(altKey)) {
				try {
					consumer.accept(map.get(altKey));
					return;
				} catch (Throwable ignored) {
				}
			}
		}
	}
}
