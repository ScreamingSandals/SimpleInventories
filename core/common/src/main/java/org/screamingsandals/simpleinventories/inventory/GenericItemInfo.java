package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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

@Data
public class GenericItemInfo {
    private final Inventory format;
    private SubInventory parent;
    private int position;
    private Item item;
    private final List<Item> animation = new ArrayList<>();
    private boolean visible;
    private boolean disabled;
    private String id;
    private final List<Property> properties = new ArrayList<>();
    @Getter(onMethod_ = @Deprecated)
    @Setter(onMethod_ = @Deprecated)
    private Map<String, Object> data;
    @Deprecated
    private Item book;
    private final Map<Condition, Map<String, Object>> conditions = new HashMap<>();
    private Origin origin;
    private boolean written;
    private final EventManager eventManager = new EventManager(format.getEventManager());
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


}
