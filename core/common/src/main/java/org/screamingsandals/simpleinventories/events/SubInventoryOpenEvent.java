package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class SubInventoryOpenEvent implements Cancellable {
    private final Inventory format;
    // Bukkit's Inventory inv
    private final PlayerWrapper player;
    private final GenericItemInfo parent;
    private final int page;
    private final SubInventory subInventory;

    private boolean cancelled;
}
