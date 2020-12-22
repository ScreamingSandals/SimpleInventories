package org.screamingsandals.simpleinventories.inventory;

import org.screamingsandals.simpleinventories.events.CloseInventoryEvent;

public interface CloseCallback {
    void close(CloseInventoryEvent event);
}
