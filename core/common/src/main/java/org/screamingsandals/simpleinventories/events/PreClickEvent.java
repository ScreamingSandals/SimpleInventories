package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class PreClickEvent implements SCancellableEvent {
    private final PlayerWrapper player;
    private final PlayerItemInfo item;
    private final ClickType clickType;
    private final SubInventory subInventory;
    private boolean cancelled;

    public boolean hasItem() {
        return item != null;
    }

    public InventorySet getFormat() {
        return item.getFormat();
    }

    public IdentifiableEntry getParent() {
        return subInventory.getItemOwner();
    }
}
