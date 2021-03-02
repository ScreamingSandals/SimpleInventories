package org.screamingsandals.simpleinventories.inventory;

import org.jetbrains.annotations.Nullable;

public interface IdentifiableEntry extends InventoryParent, InventoryChild, Queueable {
    @Nullable
    String getId();

    InventorySet getFormat();
}
