package org.screamingsandals.simpleinventories.inventory;

import org.screamingsandals.lib.utils.annotations.internal.InternalOnly;

public interface InventoryChild {
    SubInventory getParent();

    @InternalOnly
    void setParent(SubInventory subInventory);
}
