package org.screamingsandals.simpleinventories.action;

import org.screamingsandals.simpleinventories.events.PostClickEvent;
import org.screamingsandals.simpleinventories.events.PreClickEvent;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.simpleinventories.utils.ClickType;

public abstract class ClickActionHandler {
    protected void handleAction(InventoryRenderer inventoryRenderer, int slot, ClickType clickType) {
        var playerWrapper = inventoryRenderer.getPlayer();
        var subInventory = inventoryRenderer.getSubInventory();
        var options = subInventory.getLocalOrParentOptions();

        var playerItemInfo = inventoryRenderer.getItemInfoMap().get(slot);
        var parentItem = subInventory.getItemOwner();
        var preClickEvent = new PreClickEvent(playerWrapper, playerItemInfo, clickType, subInventory);
        if (playerItemInfo != null) {
            playerItemInfo.getOriginal().getEventManager().fireEvent(preClickEvent);
        } else {
            subInventory.getFormat().getEventManager().fireEvent(preClickEvent);
        }

        if (preClickEvent.isCancelled()) {
            inventoryRenderer.close();
            return;
        }

        if (playerItemInfo == null) {
            if (slot == options.getRenderHeaderStart()) {
                if (!subInventory.isMain() && parentItem != null) {
                    var parent = parentItem.getParent();
                    var pageOfParent = parentItem.isWritten() ? parentItem.getPosition() / parent.getLocalOrParentOptions().getItemsOnPage() : 0;
                    inventoryRenderer.jump(parent, pageOfParent);
                }
            } else if (slot == options.getRenderFooterStart()) {
                inventoryRenderer.previousPage();
            } else if (slot == options.getRenderFooterStart() + options.getItemsOnRow() - 1) {
                inventoryRenderer.nextPage();
            }
            return;
        }

        if (playerItemInfo.isDisabled()) {
            return;
        }

        if (playerItemInfo.hasChildInventory()) {
            inventoryRenderer.jump(playerItemInfo.getChildInventory());
        }

        if (playerItemInfo.getOriginal().hasBook()) {
            // TODO: book
            return;
        }

        if (playerItemInfo.getOriginal().getLocate() != null) {
            var opt = playerItemInfo.getFormat().resolveCategoryLink(playerItemInfo.getOriginal().getLocate());
            if (opt.isPresent()) {
                inventoryRenderer.jump(opt.get());
                return;
            }
        }

        if (playerItemInfo.getFormat().isGenericShop() && !playerItemInfo.getOriginal().getPrices().isEmpty()) {
            // TODO: shop
        }

        if (!playerItemInfo.getOriginal().getExecutions().isEmpty()) {
            // TODO: execute
        }

        var postClickEvent = new PostClickEvent(playerWrapper, playerItemInfo, clickType, subInventory);
        playerItemInfo.getOriginal().getEventManager().fireEvent(postClickEvent);

        if (inventoryRenderer.isOpened()) {
            inventoryRenderer.render();
        }
    }
}
