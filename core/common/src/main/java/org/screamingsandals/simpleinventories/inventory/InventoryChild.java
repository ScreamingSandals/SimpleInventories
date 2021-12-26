package org.screamingsandals.simpleinventories.inventory;

import org.jetbrains.annotations.ApiStatus;

public interface InventoryChild {
    SubInventory getParent();

    @ApiStatus.Internal
    void setParent(SubInventory subInventory);
}
