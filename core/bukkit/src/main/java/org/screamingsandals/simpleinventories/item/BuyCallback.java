package org.screamingsandals.simpleinventories.item;

import org.screamingsandals.simpleinventories.events.ShopTransactionEvent;

public interface BuyCallback {
    void buy(ShopTransactionEvent event);
}
