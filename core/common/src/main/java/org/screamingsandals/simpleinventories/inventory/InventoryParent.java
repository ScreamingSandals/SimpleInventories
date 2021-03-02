package org.screamingsandals.simpleinventories.inventory;

import org.jetbrains.annotations.Nullable;

public interface InventoryParent {
    @Nullable
    SubInventory getChildInventory();

    void setChildInventory(SubInventory childInventory);

    boolean hasChildInventory();
}
