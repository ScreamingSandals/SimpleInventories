package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class SubInventoryCloseEvent implements SCancellableEvent {
    private final PlayerWrapper player;
    private final SubInventory subInventory;
    private final int page;
    private boolean cancelled;

    public InventorySet getFormat() {
        return subInventory.getInventorySet();
    }
}
