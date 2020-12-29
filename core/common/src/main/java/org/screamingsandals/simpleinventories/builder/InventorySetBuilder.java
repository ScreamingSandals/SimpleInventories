package org.screamingsandals.simpleinventories.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.events.*;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class InventorySetBuilder extends CategoryBuilder {
    private final InventorySet inventory;

    @Override
    public SubInventory getSubInventory() {
        if (subInventory == null) {
            subInventory = inventory.getMainSubInventory();
        }
        return subInventory;
    }

    public InventorySetBuilder define(String definition) {
        BuilderUtils.buildDefinition(inventory, definition);
        return this;
    }

    public InventorySetBuilder render(Consumer<ItemRenderEvent> consumer) {
        inventory.getEventManager().register(ItemRenderEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder preClick(Consumer<PreClickEvent> consumer) {
        inventory.getEventManager().register(PreClickEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder click(Consumer<PostClickEvent> consumer) {
        inventory.getEventManager().register(PostClickEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder open(Consumer<SubInventoryOpenEvent> consumer) {
        inventory.getEventManager().register(SubInventoryOpenEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder close(Consumer<SubInventoryCloseEvent> consumer) {
        inventory.getEventManager().register(SubInventoryCloseEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder buy(Consumer<OnTradeEvent> consumer) {
        inventory.getEventManager().register(OnTradeEvent.class, consumer);
        return this;
    }
}
