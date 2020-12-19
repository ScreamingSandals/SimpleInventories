package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.simpleinventories.events.ItemRenderEvent;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.operations.conditions.Condition;
import org.screamingsandals.simpleinventories.utils.MapReader;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class PlayerItemInfo {
    private GenericItemInfo original;
    private Item stack;
    private boolean visible;
    private boolean disabled;
    private final List<Item> animation = new ArrayList<>();
    @Getter
    private PlayerWrapper player;
    @Getter(onMethod_ = @Deprecated)
    @Setter(onMethod_ = @Deprecated)
    private Map<String, Object> data;

    @SuppressWarnings("unchecked")
    public PlayerItemInfo(PlayerWrapper player, GenericItemInfo original, Item stack, boolean visible, boolean disabled) {
        this.player = player;
        this.original = original;
        this.stack = stack;
        this.visible = visible;
        this.disabled = disabled;
        original.getAnimation().stream().map(Item::clone).forEach(animation::add);

        this.data = original.getReader(player).convertToMap(); // this will copy whole map

        // It's hardly recommended to use Events or Groovy's callbacks
        original.getConditions().forEach((condition, stringObjectMap) -> {
            if (condition.process(player, this)) {
                stringObjectMap.forEach((key, val) -> {
                    this.data.put(key, val);
                    if (key.equals("visible")) {
                        this.visible = (boolean) val;
                    } else if (key.equals("disabled")) {
                        this.disabled = (boolean) val;
                    } else if (key.equals("stack")) {
                        this.stack = ItemFactory.build(val).orElse(ItemFactory.getAir());
                    } else if (key.equals("animation")) {
                        this.animation.clear();
                        ItemFactory.buildAll((List<Object>) val).stream().map(Item::clone).forEach(animation::add);
                    }
                });
            }
        });

        original.getEventManager().fireEvent(new ItemRenderEvent(original.getFormat(), this, player));
    }
    public String getId() {
        return original.getId();
    }

    public int getPosition() {
        return original.getPosition();
    }

    public List<Property> getProperties() {
        return original.getProperties();
    }

    public MapReader getReader() {
        return new MapReader(original.getFormat(), data, player, this);
    }

    public boolean hasId() {
        return original.hasId();
    }

    public boolean hasProperties() {
        return original.hasProperties();
    }

    public boolean hasData() {
        return data != null && !data.isEmpty();
    }

    public boolean hasAnimation() {
        return !animation.isEmpty();
    }

    public Inventory getFormat() {
        return original.getFormat();
    }

    /* Returned map should be read with MapReader */
    public Map<Condition, Map<String, Object>> getConditions() {
        return original.getConditions();
    }

    public Origin getOrigin() {
        return original.getOrigin();
    }

    public SubInventory getParent() {
        return original.getParent();
    }
}
