package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
    private SubInventory parent;
    private int position;
    private Item item;
    private final List<Item> animation = new ArrayList<>();
    private boolean visible;
    private boolean disabled;
    private String id;
    private final List<Property> properties = new ArrayList<>();
    @Getter(onMethod = @__({@Deprecated}))
    @Setter(onMethod = @__({@Deprecated}))
    private Map<String, Object> data;
    @Deprecated
    private Item book;
    private final Map<Condition, Map<String, Object>> conditions = new HashMap<>();
    private Origin origin;
    private boolean written;
    @Setter(onMethod = @__({@Deprecated}))
    private EventManager eventManager;

    public Inventory getFormat() {
        return parent.getFormat();
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


}
