package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class SubInventoryCloseEvent implements Cancellable {
    private final PlayerWrapper player;
    // Bukkit's Inventory inv
    private final SubInventory subInventory;
    private final int page;

    public Inventory getFormat() {
        return subInventory.getFormat();
    }

    private boolean cancelled;
}