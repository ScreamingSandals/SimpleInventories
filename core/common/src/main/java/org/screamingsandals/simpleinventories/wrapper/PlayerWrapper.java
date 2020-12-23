package org.screamingsandals.simpleinventories.wrapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class PlayerWrapper {
    private final String name;
    private final UUID uuid;

    public InventoryRenderer openInventory(SubInventory subInventory) {
        return SimpleInventoriesCore.openInventory(this, subInventory);
    }

    public InventoryRenderer openInventory(Inventory inventory) {
        return SimpleInventoriesCore.openInventory(this, inventory.getMainSubInventory());
    }

    public void closeInventory() {
        SimpleInventoriesCore.closeInventory(this);
    }

    public boolean hasInInventory(Item item) {
        return SimpleInventoriesCore.hasPlayerInInventory(this, item);
    }

    public List<Item> addItem(Item item) {
        return addItems(List.of(item));
    }

    public List<Item> addItems(List<Item> items) {
        return SimpleInventoriesCore.giveItemsToPlayer(this, items);
    }

    public List<Item> removeItem(Item item) {
        return removeItems(List.of(item));
    }

    public List<Item> removeItems(List<Item> items) {
        return SimpleInventoriesCore.removeItemsFromPlayer(this, items);
    }

    public <T> T as(Class<T> type) {
        return SimpleInventoriesCore.convertPlayerWrapper(this, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerWrapper)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return ((PlayerWrapper) obj).uuid.equals(this.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
