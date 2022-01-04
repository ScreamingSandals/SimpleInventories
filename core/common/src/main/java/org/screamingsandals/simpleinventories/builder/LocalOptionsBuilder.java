/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.simpleinventories.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.simpleinventories.inventory.LocalOptions;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.builder.ItemFactory;

import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class LocalOptionsBuilder {
    private final LocalOptions localOptions;

    public static final int ROWS = LocalOptions.ROWS;
    public static final int ITEMS_ON_ROW = LocalOptions.ITEMS_ON_ROW;
    public static final int RENDER_ACTUAL_ROWS = LocalOptions.RENDER_ACTUAL_ROWS;
    public static final int RENDER_OFFSET = LocalOptions.RENDER_OFFSET;
    public static final int RENDER_HEADER_START = LocalOptions.RENDER_HEADER_START;
    public static final int RENDER_FOOTER_START = LocalOptions.RENDER_FOOTER_START;

    public LocalOptionsBuilder backItem(Consumer<ItemBuilder> consumer) {
        localOptions.setBackItem(ItemFactory.build(consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder pageBackItem(Consumer<ItemBuilder> consumer) {
        localOptions.setPageBackItem(ItemFactory.build(consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder pageForwardItem(Consumer<ItemBuilder> consumer) {
        localOptions.setPageForwardItem(ItemFactory.build(consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder cosmeticItem(Consumer<ItemBuilder> consumer) {
        localOptions.setCosmeticItem(ItemFactory.build(consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder emptySlotItem(Consumer<ItemBuilder> consumer) {
        localOptions.setEmptySlotItem(ItemFactory.build(consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder backItem(Object stack) {
        localOptions.setBackItem(ItemFactory.build(stack).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder pageBackItem(Object stack) {
        localOptions.setPageBackItem(ItemFactory.build(stack).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder pageForwardItem(Object stack) {
        localOptions.setPageForwardItem(ItemFactory.build(stack).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder cosmeticItem(Object stack) {
        localOptions.setCosmeticItem(ItemFactory.build(stack).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder emptySlotItem(Object stack) {
        localOptions.setEmptySlotItem(ItemFactory.build(stack).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder backItem(Object stack, Consumer<ItemBuilder> consumer) {
        localOptions.setBackItem(ItemFactory.build(stack, consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder pageBackItem(Object stack, Consumer<ItemBuilder> consumer) {
        localOptions.setPageBackItem(ItemFactory.build(stack, consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder pageForwardItem(Object stack, Consumer<ItemBuilder> consumer) {
        localOptions.setPageForwardItem(ItemFactory.build(stack, consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder cosmeticItem(Object stack, Consumer<ItemBuilder> consumer) {
        localOptions.setCosmeticItem(ItemFactory.build(stack, consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder emptySlotItem(Object stack, Consumer<ItemBuilder> consumer) {
        localOptions.setEmptySlotItem(ItemFactory.build(stack, consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public LocalOptionsBuilder rows(int rows) {
        localOptions.setRows(rows);
        return this;
    }

    public LocalOptionsBuilder itemsOnRow(int itemsOnRow) {
        localOptions.setItemsOnRow(itemsOnRow);
        return this;
    }

    public LocalOptionsBuilder renderActualRows(int renderActualItems) {
        localOptions.setRenderActualRows(renderActualItems);
        return this;
    }

    public LocalOptionsBuilder renderOffset(int renderOffset) {
        localOptions.setRenderOffset(renderOffset);
        return this;
    }

    public LocalOptionsBuilder renderHeaderStart(int renderHeaderStart) {
        localOptions.setRenderHeaderStart(renderHeaderStart);
        return this;
    }

    public LocalOptionsBuilder renderFooterStart(int renderFooterStart) {
        localOptions.setRenderFooterStart(renderFooterStart);
        return this;
    }

    public LocalOptionsBuilder inventoryType(String inventoryType) {
        localOptions.setInventoryType(inventoryType);
        return this;
    }

    public LocalOptionsBuilder showPageNumber(boolean showPageNumber) {
        localOptions.setShowPageNumber(showPageNumber);
        return this;
    }

    public LocalOptionsBuilder prefix(String prefix) {
        localOptions.setPrefix(AdventureHelper.toComponent(prefix));
        return this;
    }

    public LocalOptionsBuilder prefix(Component prefix) {
        localOptions.setPrefix(prefix);
        return this;
    }
}
