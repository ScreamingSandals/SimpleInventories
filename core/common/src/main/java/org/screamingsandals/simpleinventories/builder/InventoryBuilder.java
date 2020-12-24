package org.screamingsandals.simpleinventories.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.events.*;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.placeholders.RuntimeDefinedPlaceholder;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class InventoryBuilder extends CategoryBuilder {
    private final Inventory inventory;

    @Override
    public SubInventory getSubInventory() {
        if (subInventory == null) {
            subInventory = inventory.getMainSubInventory();
        }
        return subInventory;
    }

    public InventoryBuilder define(String definition) {
        BuilderUtils.buildDefinition(inventory, definition);
        return this;
    }

    public InventoryBuilder render(Consumer<ItemRenderEvent> consumer) {
        inventory.getEventManager().register(ItemRenderEvent.class, consumer);
        return this;
    }

    public InventoryBuilder preClick(Consumer<PreClickEvent> consumer) {
        inventory.getEventManager().register(PreClickEvent.class, consumer);
        return this;
    }

    public InventoryBuilder click(Consumer<PostClickEvent> consumer) {
        inventory.getEventManager().register(PostClickEvent.class, consumer);
        return this;
    }

    public InventoryBuilder open(Consumer<SubInventoryOpenEvent> consumer) {
        inventory.getEventManager().register(SubInventoryOpenEvent.class, consumer);
        return this;
    }

    public InventoryBuilder close(Consumer<SubInventoryCloseEvent> consumer) {
        inventory.getEventManager().register(SubInventoryCloseEvent.class, consumer);
        return this;
    }

    public InventoryBuilder buy(Consumer<OnTradeEvent> consumer) {
        inventory.getEventManager().register(OnTradeEvent.class, consumer);
        return this;
    }
}
