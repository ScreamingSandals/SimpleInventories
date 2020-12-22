package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.utils.ClickType;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class PostClickEvent implements Cancellable {
    private final PlayerWrapper player;
    // Bukkit's Inventory inv
    private final PlayerItemInfo item;
    private final ClickType clickType;
    private final SubInventory subInventory;

    public Inventory getFormat() {
        return item.getFormat();
    }

    public GenericItemInfo getParent() {
        return subInventory.getItemOwner();
    }

    private boolean cancelled;
}
