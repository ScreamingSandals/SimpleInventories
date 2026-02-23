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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleinventories.inventory.LocalOptions;

@AllArgsConstructor
public class GroovyDirectOptionsBuilder implements IGroovyLocalOptionsBuilder {

    private final LocalOptions options;

    @Override
    public void backItem(ItemStack stack) {
        options.setBackItem(stack);
    }

    @Override
    public void pageBackItem(ItemStack stack) {
        options.setPageBackItem(stack);
    }

    @Override
    public void pageForwardItem(ItemStack stack) {
        options.setPageForwardItem(stack);
    }

    @Override
    public void cosmeticItem(ItemStack stack) {
        options.setCosmeticItem(stack);
    }

    @Override
    public void rows(int rows) {
        options.setRows(rows);
    }

    @Override
    public void itemsOnRow(int itemsOnRow) {
        options.setItems_on_row(itemsOnRow);
    }

    @Override
    public void renderActualRows(int renderActualItems) {
        options.setRender_actual_rows(renderActualItems);
    }

    @Override
    public void renderOffset(int renderOffset) {
        options.setRender_offset(renderOffset);
    }

    @Override
    public void renderHeaderStart(int renderHeaderStart) {
        options.setRender_header_start(renderHeaderStart);
    }

    @Override
    public void renderFooterStart(int renderFooterStart) {
        options.setRender_footer_start(renderFooterStart);
    }

    @Override
    public void inventoryType(String inventoryType) {
        options.setInventoryType(InventoryType.valueOf(inventoryType.toUpperCase()));
    }
}
