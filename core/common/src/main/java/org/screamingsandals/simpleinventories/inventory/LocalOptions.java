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

package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.spectator.Component;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Optional;

@Data
public class LocalOptions implements Cloneable {
    /*
        DEFAULT VALUES
     */
    public static final int ROWS = 4;
    public static final int ITEMS_ON_ROW = 9;
    public static final int RENDER_ACTUAL_ROWS = 6;
    public static final int RENDER_OFFSET = ITEMS_ON_ROW;
    public static final int RENDER_HEADER_START = 0;
    public static final int RENDER_FOOTER_START = 45;

    public static final boolean SHOW_PAGE_NUMBER = true;
    public static final String INVENTORY_TYPE = "CHEST";
    public static final Component PREFIX = Component.text("Inventory");

    public static final ItemStack BACK_ITEM;
    public static final ItemStack PAGE_BACK_ITEM;
    public static final ItemStack PAGE_FORWARD_ITEM;
    public static final ItemStack COSMETIC_ITEM = ItemStackFactory.getAir();
    public static final ItemStack EMPTY_SLOT_ITEM = ItemStackFactory.getAir();

    static {
        var backItem = ItemStackFactory.build("BARRIER");
        if (backItem == null) {
            backItem = ItemStackFactory.getAir();
        }
        BACK_ITEM = backItem;

        var arrowItem = ItemStackFactory.build("ARROW");
        if (arrowItem == null) {
            arrowItem = ItemStackFactory.getAir();
        }
        PAGE_BACK_ITEM = arrowItem;
        PAGE_FORWARD_ITEM = arrowItem;
    }

    /*
        CURRENT VALUES
     */

    @Getter(onMethod_ = @Deprecated)
    @Setter(onMethod_ = @Deprecated)
    @Nullable
    @ToString.Exclude
    private LocalOptions parent;

    @Nullable
    private ItemStack backItem;
    @Nullable
    private ItemStack pageBackItem;
    @Nullable
    private ItemStack pageForwardItem;
    @Nullable
    private ItemStack cosmeticItem;
    @Nullable
    private ItemStack emptySlotItem;

    @Nullable
    private Boolean showPageNumber;
    @Nullable
    private Component prefix;
    @Nullable
    private String inventoryType;

    @Nullable
    private Integer rows;
    @Nullable
    private Integer itemsOnRow;
    @Nullable
    private Integer renderActualRows;
    @Nullable
    private Integer renderOffset;
    @Nullable
    private Integer renderHeaderStart;
    @Nullable
    private Integer renderFooterStart;


    /*
        GETTERS
     */

    public ItemStack getBackItem() {
        return Optional.ofNullable(backItem).orElseGet(parent != null ? parent::getBackItem : BACK_ITEM::clone);
    }

    public ItemStack getPageBackItem() {
        return Optional.ofNullable(pageBackItem).orElseGet(parent != null ? parent::getPageBackItem : PAGE_BACK_ITEM::clone);
    }

    public ItemStack getPageForwardItem() {
        return Optional.ofNullable(pageForwardItem).orElseGet(parent != null ? parent::getPageForwardItem : PAGE_FORWARD_ITEM::clone);
    }

    public ItemStack getCosmeticItem() {
        return Optional.ofNullable(cosmeticItem).orElseGet(parent != null ? parent::getCosmeticItem : COSMETIC_ITEM::clone);
    }

    public ItemStack getEmptySlotItem() {
        return Optional.ofNullable(emptySlotItem).orElseGet(parent != null ? parent::getEmptySlotItem : EMPTY_SLOT_ITEM::clone);
    }

    public boolean isShowPageNumber() {
        return Optional.ofNullable(showPageNumber).orElseGet(parent != null ? parent::isShowPageNumber : () -> SHOW_PAGE_NUMBER);
    }

    public Component getPrefix() {
        return Optional.ofNullable(prefix).orElseGet(parent != null ? parent::getPrefix : () -> PREFIX);
    }

    public String getInventoryType() {
        return Optional.ofNullable(inventoryType).orElseGet(parent != null ? parent::getInventoryType : () -> INVENTORY_TYPE);
    }

    public int getRows() {
        return Optional.ofNullable(rows).orElseGet(parent != null ? parent::getRows : () -> ROWS);
    }

    public int getItemsOnRow() {
        return Optional.ofNullable(itemsOnRow).orElseGet(parent != null ? parent::getItemsOnRow : () -> ITEMS_ON_ROW);
    }

    public int getRenderActualRows() {
        return Optional.ofNullable(renderActualRows).orElseGet(parent != null ? parent::getRenderActualRows : () -> RENDER_ACTUAL_ROWS);
    }

    public int getRenderOffset() {
        return Optional.ofNullable(renderOffset).orElseGet(parent != null ? parent::getRenderOffset : () -> RENDER_OFFSET);
    }

    public int getRenderHeaderStart() {
        return Optional.ofNullable(renderHeaderStart).orElseGet(parent != null ? parent::getRenderHeaderStart : () -> RENDER_HEADER_START);
    }

    public int getRenderFooterStart() {
        return Optional.ofNullable(renderFooterStart).orElseGet(parent != null ? parent::getRenderFooterStart : () -> RENDER_FOOTER_START);
    }

    public int getItemsOnPage() {
        return getItemsOnRow() * getRows();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public LocalOptions clone() {
        var options = new LocalOptions();
        options.backItem = backItem != null ? backItem.clone() : null;
        options.pageBackItem = pageBackItem != null ? pageBackItem.clone() : null;
        options.pageForwardItem = pageForwardItem != null ? pageForwardItem.clone() : null;
        options.cosmeticItem = cosmeticItem != null ? cosmeticItem.clone() : null;

        options.showPageNumber = showPageNumber;
        options.prefix = prefix;
        options.inventoryType = inventoryType;

        options.rows = rows;
        options.itemsOnRow = itemsOnRow;
        options.renderActualRows = renderActualRows;
        options.renderOffset = renderOffset;
        options.renderHeaderStart = renderHeaderStart;
        options.renderFooterStart = renderFooterStart;
        return options;
    }

    public void fromNode(ConfigurationNode configurationNode) {
        var backItem = configurationNode.node("backItem");
        if (!backItem.empty()) {
            this.backItem = ItemStackFactory.build(backItem);
        }
        var pageBackItem = configurationNode.node("pageBackItem");
        if (!pageBackItem.empty()) {
            this.pageBackItem = ItemStackFactory.build(pageBackItem);
        }
        var pageForwardItem = configurationNode.node("pageForwardItem");
        if (!pageForwardItem.empty()) {
            this.pageForwardItem = ItemStackFactory.build(pageForwardItem);
        }
        var cosmeticItem = configurationNode.node("cosmeticItem");
        if (!cosmeticItem.empty()) {
            this.cosmeticItem = ItemStackFactory.build(cosmeticItem);
        }

        var rows = configurationNode.node("rows");
        if (!rows.empty()) {
            try {
                this.rows = rows.get(Integer.class);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }

        var renderActualRows = configurationNode.node("render_actual_rows");
        if (!renderActualRows.empty()) {
            try {
                this.renderActualRows = renderActualRows.get(Integer.class);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }

        var renderOffset = configurationNode.node("render_offset");
        if (!renderOffset.empty()) {
            try {
                this.renderOffset = renderOffset.get(Integer.class);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }

        var renderHeaderStart = configurationNode.node("render_header_start");
        if (!renderHeaderStart.empty()) {
            try {
                this.renderHeaderStart = renderHeaderStart.get(Integer.class);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }

        var renderFooterStart = configurationNode.node("render_footer_start");
        if (!renderFooterStart.empty()) {
            try {
                this.renderFooterStart = renderFooterStart.get(Integer.class);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }

        var itemsOnRow = configurationNode.node("items_on_row");
        if (!itemsOnRow.empty()) {
            try {
                this.itemsOnRow = itemsOnRow.get(Integer.class);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }

        var inventoryType = configurationNode.node("inventoryType");
        if (!inventoryType.empty()) {
            this.inventoryType = inventoryType.getString();
        }

        var prefix = configurationNode.node("prefix");
        if (!prefix.empty()) {
            try {
                this.prefix = prefix.get(Component.class);
            } catch (SerializationException ignored) {
                try {
                    this.prefix = Component.fromLegacy(prefix.getString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        var showPageNumber = configurationNode.node("showPageNumber");
        if (!showPageNumber.empty()) {
            this.showPageNumber = showPageNumber.getBoolean();
        }
    }
}
