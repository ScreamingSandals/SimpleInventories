package org.screamingsandals.simpleinventories.groovy.builder;

import groovy.lang.Closure;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleinventories.inventory.LocalOptions;
import org.screamingsandals.simpleinventories.utils.StackParser;

import java.util.HashMap;
import java.util.Map;

import static org.screamingsandals.simpleinventories.groovy.utils.GroovyUtils.internalCallClosure;

public interface IGroovyLocalOptionsBuilder {
    public static final int ROWS = LocalOptions.ROWS;
    public static final int ITEMS_ON_ROW = LocalOptions.ITEMS_ON_ROW;
    public static final int RENDER_ACTUAL_ROWS = LocalOptions.RENDER_ACTUAL_ROWS;
    public static final int RENDER_OFFSET = LocalOptions.RENDER_OFFSET;
    public static final int RENDER_HEADER_START = LocalOptions.RENDER_HEADER_START;
    public static final int RENDER_FOOTER_START = LocalOptions.RENDER_FOOTER_START;

    default void backItem(Closure<IGroovyStackBuilder> closure) {
        Map<String, Object> stack = new HashMap<>();
        internalCallClosure(closure, new GroovyLongStackBuilder(stack));
        backItem(StackParser.parseLongStack(stack));
    }
    default void pageBackItem(Closure<IGroovyStackBuilder> closure) {
        Map<String, Object> stack = new HashMap<>();
        internalCallClosure(closure, new GroovyLongStackBuilder(stack));
        pageBackItem(StackParser.parseLongStack(stack));
    }
    default void pageForwardItem(Closure<IGroovyStackBuilder> closure) {
        Map<String, Object> stack = new HashMap<>();
        internalCallClosure(closure, new GroovyLongStackBuilder(stack));
        pageForwardItem(StackParser.parseLongStack(stack));
    }
    default void cosmeticItem(Closure<IGroovyStackBuilder> closure) {
        Map<String, Object> stack = new HashMap<>();
        internalCallClosure(closure, new GroovyLongStackBuilder(stack));
        cosmeticItem(StackParser.parseLongStack(stack));
    }

    void backItem(ItemStack stack);
    void pageBackItem(ItemStack stack);
    void pageForwardItem(ItemStack stack);
    void cosmeticItem(ItemStack stack);

    void rows(int rows);
    void itemsOnRow(int itemsOnRow);
    void renderActualRows(int renderActualItems);
    void renderOffset(int renderOffset);
    void renderHeaderStart(int renderHeaderStart);
    void renderFooterStart(int renderFooterStart);
}
