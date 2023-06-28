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
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.ReceiverConsumer;
import org.screamingsandals.simpleinventories.inventory.LocalOptions;
import org.screamingsandals.lib.item.builder.ItemStackBuilder;
import org.screamingsandals.lib.item.builder.ItemStackFactory;

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

    public LocalOptionsBuilder backItem(ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(consumer);
        localOptions.setBackItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder pageBackItem(ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(consumer);
        localOptions.setPageBackItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder pageForwardItem(ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(consumer);
        localOptions.setPageForwardItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder cosmeticItem(ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(consumer);
        localOptions.setCosmeticItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder emptySlotItem(ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(consumer);
        localOptions.setEmptySlotItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder backItem(Object stack) {
        var item = ItemStackFactory.build(stack);
        localOptions.setBackItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder pageBackItem(Object stack) {
        var item = ItemStackFactory.build(stack);
        localOptions.setPageBackItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder pageForwardItem(Object stack) {
        var item = ItemStackFactory.build(stack);
        localOptions.setPageForwardItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder cosmeticItem(Object stack) {
        var item = ItemStackFactory.build(stack);
        localOptions.setCosmeticItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder emptySlotItem(Object stack) {
        var item = ItemStackFactory.build(stack);
        localOptions.setEmptySlotItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder backItem(Object stack, ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(stack, consumer);
        localOptions.setBackItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder pageBackItem(Object stack, ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(stack, consumer);
        localOptions.setPageBackItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder pageForwardItem(Object stack, ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(stack, consumer);
        localOptions.setPageForwardItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder cosmeticItem(Object stack, ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(stack, consumer);
        localOptions.setCosmeticItem(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public LocalOptionsBuilder emptySlotItem(Object stack, ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(stack, consumer);
        localOptions.setEmptySlotItem(item != null ? item : ItemStackFactory.getAir());
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
        localOptions.setPrefix(Component.fromLegacy(prefix));
        return this;
    }

    public LocalOptionsBuilder prefix(Component prefix) {
        localOptions.setPrefix(prefix);
        return this;
    }
}
