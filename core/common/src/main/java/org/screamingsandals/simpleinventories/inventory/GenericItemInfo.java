package org.screamingsandals.simpleinventories.inventory;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.material.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Data
public class GenericItemInfo implements Cloneable, Queueable {
    @ToString.Exclude
    @NotNull
    private InventorySet format;
    @ToString.Exclude
    private SubInventory parent;
    private int position;
    private Item item;
    private final List<Item> animation = new ArrayList<>();
    private Supplier<Boolean> visible;
    private Supplier<Boolean> disabled;
    private String id;
    private final List<Property> properties = new ArrayList<>();
    @Deprecated
    private Item book;
    private Supplier<Boolean> written;
    private final EventManager eventManager;
    private SubInventory childInventory;
    private InventoryLink locate;
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

    public GenericItemInfo(@NotNull InventorySet format) {
        this.format = format;
        this.eventManager = new EventManager();
        this.eventManager.setCustomManager(format.getEventManager());
    }

    @Deprecated // unsafe
    public void setFormat(@NotNull InventorySet format) {
        this.format = format;
        this.eventManager.setCustomManager(format.getEventManager());
        this.properties.forEach(property -> property.setInventorySet(format));
    }

    public boolean hasId() {
        return id != null;
    }

    public boolean hasProperties() {
        return !properties.isEmpty();
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

    public Stream<Property> getPropertiesByName(@NotNull String name) {
        return properties.stream()
                .filter(Objects::nonNull)
                .filter(property -> name.equalsIgnoreCase(property.getPropertyName()));
    }

    public Optional<Property> getFirstPropertyByName(String name) {
        return getPropertiesByName(name).findFirst();
    }

    public void moveAbsolute(int position) {
        if (this.parent != null) {
            this.parent.dropContentsOn(position);
            this.position = position;
            this.parent.forceReload();
        }
    }

    public void moveRelative(int relative) {
        if (this.parent != null) {
            this.parent.dropContentsOn(this.position + relative);
            this.position += relative;
            this.parent.forceReload();
        }
    }

    public void repaint() {
        if (this.getParent() != null) {
            this.getParent().forceReload();
        }
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
        info.book = book != null ? book.clone() : null;
        info.eventManager.cloneEventManager(eventManager);
        info.executions.addAll(executions);
        prices.stream().map(Price::clone).forEach(info.prices::add);
        info.prices.addAll(prices);
        info.requestedPosition = requestedPosition != null ? requestedPosition.clone() : null;
        info.requestedClone = requestedClone != null ? requestedClone.clone() : null;
        info.requestedTimes = requestedTimes != null ? requestedTimes.clone() : null;
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
