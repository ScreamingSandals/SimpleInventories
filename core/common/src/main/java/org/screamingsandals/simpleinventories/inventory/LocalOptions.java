package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;

@Data
public class LocalOptions implements Cloneable {
    public static final int ROWS = 4;
    public static final int ITEMS_ON_ROW = 9;
    public static final int RENDER_ACTUAL_ROWS = 6;
    public static final int RENDER_OFFSET = ITEMS_ON_ROW;
    public static final int RENDER_HEADER_START = 0;
    public static final int RENDER_FOOTER_START = 45;

    private Item backItem = ItemFactory.build("BARRIER").orElse(ItemFactory.getAir());
    private Item pageBackItem = ItemFactory.build("ARROW").orElse(ItemFactory.getAir());
    private Item pageForwardItem = ItemFactory.build("ARROW").orElse(ItemFactory.getAir());
    private Item cosmeticItem = ItemFactory.getAir();

    private int rows = ROWS;
    private int items_on_row = ITEMS_ON_ROW;
    private int render_actual_rows = RENDER_ACTUAL_ROWS;
    private int render_offset = RENDER_OFFSET;
    private int render_header_start = RENDER_HEADER_START;
    private int render_footer_start = RENDER_FOOTER_START;

    public int getItemsOnPage() {
        return items_on_row * rows;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public LocalOptions clone() {
        var options = new LocalOptions();
        options.backItem = backItem != null ? backItem.clone() : null;
        options.pageBackItem = pageBackItem != null ? pageBackItem.clone() : null;
        options.pageForwardItem = pageForwardItem != null ? pageForwardItem.clone() : null;
        options.cosmeticItem = cosmeticItem != null ? cosmeticItem.clone() : null;

        options.rows = rows;
        options.items_on_row = items_on_row;
        options.render_actual_rows = render_actual_rows;
        options.render_offset = render_offset;
        options.render_header_start = render_header_start;
        options.render_footer_start = render_footer_start;
        return options;
    }
}
