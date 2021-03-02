package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;

@Data
public class HiddenCategory implements IdentifiableEntry {
    private final InventorySet format;
    private final String id;
    private SubInventory childInventory;
    // will be filled in by SubInventory#process
    private SubInventory parent;

    @Override
    public boolean hasChildInventory() {
        return childInventory != null;
    }
}
