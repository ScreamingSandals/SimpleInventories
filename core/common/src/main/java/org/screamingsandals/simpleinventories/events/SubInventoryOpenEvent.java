package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.IdentifiableEntry;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class SubInventoryOpenEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final SubInventory subInventory;
    private final int page;

    public InventorySet getFormat() {
        return subInventory.getInventorySet();
    }

    public IdentifiableEntry getParent() {
        return subInventory.getItemOwner();
    }
}
