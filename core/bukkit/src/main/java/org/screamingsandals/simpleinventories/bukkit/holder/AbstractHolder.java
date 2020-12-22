package org.screamingsandals.simpleinventories.bukkit.holder;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

@Getter
@Setter
public abstract class AbstractHolder implements InventoryHolder {
    private Inventory inventory;
    private SubInventory subInventory;
}
