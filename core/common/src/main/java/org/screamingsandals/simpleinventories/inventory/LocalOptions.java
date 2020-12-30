package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemFactory;

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
    public static final String PREFIX = "Inventory";

    public static final Item BACK_ITEM = ItemFactory.build("BARRIER").orElse(ItemFactory.getAir());
    public static final Item PAGE_BACK_ITEM = ItemFactory.build("ARROW").orElse(ItemFactory.getAir());
    public static final Item PAGE_FORWARD_ITEM = ItemFactory.build("ARROW").orElse(ItemFactory.getAir());
    public static final Item COSMETIC_ITEM = ItemFactory.getAir();

    /*
        CURRENT VALUES
     */

    @Getter(onMethod_ = @Deprecated)
    @Setter(onMethod_ = @Deprecated)
    @Nullable
    @ToString.Exclude
    private LocalOptions parent;

    @Nullable
    private Item backItem;
    @Nullable
    private Item pageBackItem;
    @Nullable
    private Item pageForwardItem;
    @Nullable
    private Item cosmeticItem;

    @Nullable
    private Boolean showPageNumber;
    @Nullable
    private String prefix;
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

    public Item getBackItem() {
        return Optional.ofNullable(backItem).orElseGet(parent != null ? parent::getBackItem : BACK_ITEM::clone);
    }

    public Item getPageBackItem() {
        return Optional.ofNullable(pageBackItem).orElseGet(parent != null ? parent::getPageBackItem : PAGE_BACK_ITEM::clone);
    }

    public Item getPageForwardItem() {
        return Optional.ofNullable(pageForwardItem).orElseGet(parent != null ? parent::getPageForwardItem : PAGE_FORWARD_ITEM::clone);
    }

    public Item getCosmeticItem() {
        return Optional.ofNullable(cosmeticItem).orElseGet(parent != null ? parent::getCosmeticItem : COSMETIC_ITEM::clone);
    }

    public boolean isShowPageNumber() {
        return Optional.ofNullable(showPageNumber).orElseGet(parent != null ? parent::isShowPageNumber : () -> SHOW_PAGE_NUMBER);
    }

    public String getPrefix() {
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
}
