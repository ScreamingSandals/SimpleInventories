package org.screamingsandals.simpleinventories.action;

import org.screamingsandals.simpleinventories.events.SubInventoryCloseEvent;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;

public abstract class CloseInventoryActionHandler {
    protected boolean handleAction(InventoryRenderer inventoryRenderer) {
        var player = inventoryRenderer.getPlayer();
        var subInventory = inventoryRenderer.getSubInventory();
        var page = inventoryRenderer.getPage();

        var closeEvent = new SubInventoryCloseEvent(player, subInventory, page);
        subInventory.getFormat().getEventManager().fireEvent(closeEvent);

        if (closeEvent.isCancelled()) {
            inventoryRenderer.jump(subInventory, page);
            return false;
        }
        return true;
    }
}
