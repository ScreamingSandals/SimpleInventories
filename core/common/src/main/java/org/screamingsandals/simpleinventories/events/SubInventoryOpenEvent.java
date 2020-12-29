package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class SubInventoryOpenEvent implements Cancellable {
    // Bukkit's Inventory inv
    private final PlayerWrapper player;
    private final SubInventory subInventory;
    private final int page;

    public InventorySet getFormat() {
        return subInventory.getInventorySet();
    }

    public GenericItemInfo getParent() {
        return subInventory.getItemOwner();
    }

    private boolean cancelled;
}
