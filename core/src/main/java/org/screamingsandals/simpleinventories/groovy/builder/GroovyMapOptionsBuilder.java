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

package org.screamingsandals.simpleinventories.groovy.builder;

import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@AllArgsConstructor
public class GroovyMapOptionsBuilder implements IGroovyLocalOptionsBuilder{
    private final Map<String, Object> map;

    @Override
    public void backItem(ItemStack stack) {
        map.put("backItem", stack);
    }

    @Override
    public void pageBackItem(ItemStack stack) {
        map.put("pageBackItem", stack);
    }

    @Override
    public void pageForwardItem(ItemStack stack) {
        map.put("pageForwardItem", stack);
    }

    @Override
    public void cosmeticItem(ItemStack stack) {
        map.put("cosmeticItem", stack);
    }

    @Override
    public void rows(int rows) {
        map.put("rows", rows);
    }

    @Override
    public void itemsOnRow(int itemsOnRow) {
        map.put("items_on_row", itemsOnRow);
    }

    @Override
    public void renderActualRows(int renderActualItems) {
        map.put("render_actual_rows", renderActualItems);
    }

    @Override
    public void renderOffset(int renderOffset) {
        map.put("render_offset", renderOffset);
    }

    @Override
    public void renderHeaderStart(int renderHeaderStart) {
        map.put("render_header_start", renderHeaderStart);
    }

    @Override
    public void renderFooterStart(int renderFooterStart) {
        map.put("render_footer_start", renderFooterStart);
    }

    @Override
    public void inventoryType(String inventoryType) {
        map.put("inventoryType", inventoryType);
    }
}
