package org.screamingsandals.simpleinventories.inventory;

import org.screamingsandals.simpleinventories.events.OpenInventoryEvent;

public interface OpenCallback {
    void open(OpenInventoryEvent event);
}
