package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.Getter;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.events.ItemRenderEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class PlayerItemInfo {
    private GenericItemInfo original;
    private Item stack;
    private boolean visible;
    private boolean disabled;
    private final List<Item> animation = new ArrayList<>();
    @Getter
    private PlayerWrapper player;

    @SuppressWarnings("unchecked")
    public PlayerItemInfo(PlayerWrapper player, GenericItemInfo original) {
        this.player = player;
        this.original = original;
        this.stack = original.getItem() != null ? original.getItem().clone() : ItemFactory.getAir();
        this.visible = original.isVisible();
        this.disabled = original.isDisabled();
        original.getAnimation().stream().map(Item::clone).forEach(animation::add);

        if (stack.getDisplayName() != null) {
            stack.setDisplayName(original.getFormat().processPlaceholders(player, stack.getDisplayName(), this));
        }
        if (!stack.getLore().isEmpty()) {
            var newLore = stack.getLore().stream().map(e -> original.getFormat().processPlaceholders(player, e, this)).collect(Collectors.toList());
            stack.getLore().clear();
            stack.getLore().addAll(newLore);
        }

        original.getEventManager().fireEvent(new ItemRenderEvent(this));
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

    public boolean hasId() {
        return original.hasId();
    }

    public boolean hasProperties() {
        return original.hasProperties();
    }

    public boolean hasAnimation() {
        return !animation.isEmpty();
    }

    public boolean hasChildInventory() {
        return original.hasChildInventory();
    }

    public SubInventory getChildInventory() {
        return original.getChildInventory();
    }

    public InventorySet getFormat() {
        return original.getFormat();
    }

    public SubInventory getParent() {
        return original.getParent();
    }

    public Stream<Property> getPropertiesByName(String name) {
        return original.getPropertiesByName(name);
    }

    public Optional<Property> getFirstPropertyByName(String name) {
        return original.getFirstPropertyByName(name);
    }
}
