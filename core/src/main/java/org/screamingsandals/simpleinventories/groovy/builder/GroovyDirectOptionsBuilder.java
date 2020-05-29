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
