package org.screamingsandals.simpleinventories.inventory;

import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.simpleinventories.events.EventManager;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.operations.conditions.Condition;
import org.screamingsandals.simpleinventories.utils.MapReader;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Data
public class GenericItemInfo implements Cloneable {
    @ToString.Exclude
    private final Inventory format;
    @ToString.Exclude
    private SubInventory parent;
    private int position;
    private Item item;
    private final List<Item> animation = new ArrayList<>();
    private Supplier<Boolean> visible;
    private Supplier<Boolean> disabled;
    private String id;
    private final List<Property> properties = new ArrayList<>();
    @Getter(onMethod_ = @Deprecated)
    @Setter(onMethod_ = @Deprecated)
    private Map<String, Object> data;
    @Deprecated
    private Item book;
    @Deprecated(forRemoval = true) // may be replaced by event handlers
    private final Map<Condition, Map<String, Object>> conditions = new HashMap<>();
    private Origin origin;
    private Supplier<Boolean> written;
    private final EventManager eventManager;
    private SubInventory childInventory;
    private String locate;
    private final List<String> executions = new ArrayList<>();
    private final List<Price> prices = new ArrayList<>();

    /**
     *  GENERATION ONLY
     */
    private Position requestedPosition = new Position();
    /**
     *  GENERATION ONLY
     */
    @Nullable
    private Clone requestedClone;
    /**
     *  GENERATION ONLY
     */
    @Nullable
    private Times requestedTimes;
    /**
     *  GENERATION ONLY
     */
    @Setter(onMethod_ = @Deprecated)
    @Nullable
    private String defaultCurrency;

    public GenericItemInfo(Inventory format) {
        this.format = format;
        this.eventManager = new EventManager(format.getEventManager());
    }

    @Deprecated
    public MapReader getReader(PlayerWrapper owner) {
        return new MapReader(getFormat(), data == null ? Map.of() : data, owner, null);
    }

    public boolean hasId() {
        return id != null;
    }

    public boolean hasProperties() {
        return !properties.isEmpty();
    }

    public boolean hasData() {
        return data != null && !data.isEmpty();
    }

    public boolean hasAnimation() {
        return !animation.isEmpty();
    }

    @Deprecated
    public boolean hasBook() {
        return book != null;
    }

    public boolean hasChildInventory() {
        return childInventory != null;
    }

    public boolean isWritten() {
        return written != null ? written.get() : true;
    }

    public boolean isDisabled() {
        return disabled != null ? disabled.get() : false;
    }

    public boolean isVisible() {
        return visible != null ? visible.get() : true;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public GenericItemInfo clone() {
        var info = new GenericItemInfo(format);
        info.item = item != null ? item.clone() : null;
        animation.stream().map(Item::clone).forEach(info.animation::add);
        info.visible = visible;
        info.disabled = disabled;
        info.written = written;
        info.id = id;
        properties.stream().map(Property::clone).forEach(info.properties::add);
        info.data = data != null ? Map.copyOf(data) : null; // TODO: deep copy
        info.book = book != null ? book.clone() : null;
        info.conditions.putAll(conditions);
        info.eventManager.cloneEventManager(eventManager);
        info.executions.addAll(executions);
        prices.stream().map(Price::clone).forEach(info.prices::add);
        info.prices.addAll(prices);
        info.origin = origin;
        info.requestedPosition = requestedPosition;
        info.requestedClone = requestedClone;
        info.requestedTimes = requestedTimes;
        info.defaultCurrency = defaultCurrency;
        if (hasChildInventory()) {
            info.childInventory = new SubInventory(false, info, format);
            childInventory.getContents().stream().map(GenericItemInfo::clone).forEach(info.childInventory.getWaitingQueue()::add);
            childInventory.getWaitingQueue().forEach(o -> {
                if (o instanceof GenericItemInfo) {
                    info.childInventory.getWaitingQueue().add(((GenericItemInfo) o).clone());
                } else {
                    info.childInventory.getWaitingQueue().add(o);
                }
            });
        }
        return info;
    }
}
