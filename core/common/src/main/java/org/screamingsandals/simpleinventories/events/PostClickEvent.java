package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.simpleinventories.utils.ClickType;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class PostClickEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final PlayerItemInfo item;
    private final ClickType clickType;
    private final SubInventory subInventory;

    public InventorySet getFormat() {
        return item.getFormat();
    }

    public IdentifiableEntry getParent() {
        return subInventory.getItemOwner();
    }
}
