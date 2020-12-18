package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.utils.ClickType;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class OnTradeEvent implements Cancellable {
    private final PlayerWrapper player;
    private final Inventory format;
    private final int price;
    private final String type;
    private final Item stack;
    private final PlayerItemInfo item;
    private final ClickType clickType;

    private boolean cancelled;
}
