package org.screamingsandals.simpleinventories.action;

import org.screamingsandals.simpleinventories.events.OnTradeEvent;
import org.screamingsandals.simpleinventories.events.PostClickEvent;
import org.screamingsandals.simpleinventories.events.PreClickEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.simpleinventories.utils.ClickType;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ClickActionHandler {
    protected void handleAction(InventoryRenderer inventoryRenderer, int slot, ClickType clickType) {
        var playerWrapper = inventoryRenderer.getPlayer();
        var subInventory = inventoryRenderer.getSubInventory();
        var options = subInventory.getLocalOptions();

        var playerItemInfo = inventoryRenderer.getItemInfoMap().get(slot);
        var parentItem = subInventory.getItemOwner();
        var preClickEvent = new PreClickEvent(playerWrapper, playerItemInfo, clickType, subInventory);
        if (playerItemInfo != null) {
            playerItemInfo.getOriginal().getEventManager().fireEvent(preClickEvent);
        } else {
            subInventory.getInventorySet().getEventManager().fireEvent(preClickEvent);
        }

        if (preClickEvent.isCancelled()) {
            inventoryRenderer.close();
            return;
        }

        if (playerItemInfo == null) {
            if (slot == options.getRenderHeaderStart()) {
                if (!subInventory.isMain() && parentItem != null) {
                    var parent = parentItem.getParent();
                    var pageOfParent = parentItem.isWritten() ? parentItem.getPosition() / parent.getLocalOptions().getItemsOnPage() : 0;
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
            return;
        }

        if (playerItemInfo.getOriginal().hasBook()) {
            inventoryRenderer.close();
            openBook(playerWrapper, playerItemInfo.getOriginal().getBook());
            return;
        }

        if (playerItemInfo.getOriginal().getLocate() != null) {
            var opt = playerItemInfo.getOriginal().getLocate().resolve();
            if (opt.isPresent()) {
                inventoryRenderer.jump(opt.get());
                return;
            }
        }

        if (playerItemInfo.getFormat().isGenericShop() && !playerItemInfo.getOriginal().getPrices().isEmpty()) {
            var stream = playerItemInfo.getOriginal().getPrices().stream().map(price -> {
                var price1 = price.clone();
                if (price1.getCurrency() == null) {
                    price1.setCurrency(playerItemInfo.getOriginal().getDefaultCurrency());
                }
                if (price1.getCurrency() == null && playerItemInfo.getFormat().isGenericShopPriceTypeRequired()) {
                    price1 = null;
                }
                return price1;
            }).filter(Objects::nonNull);
            if (stream.count() > 0) {
                var tradeEvent = new OnTradeEvent(playerWrapper, stream.collect(Collectors.toList()), playerItemInfo.getOriginal().getItem().clone(), playerItemInfo, clickType);
                playerItemInfo.getOriginal().getEventManager().fireEvent(tradeEvent);

                if (inventoryRenderer.isOpened()) {
                    inventoryRenderer.render();
                }
                return;
            }
        }

        if (!playerItemInfo.getOriginal().getExecutions().isEmpty()) {
            playerItemInfo.getOriginal().getExecutions().forEach(s -> {
                if (s.startsWith("console:")) {
                    if (playerItemInfo.getFormat().isAllowAccessToConsole()) {
                        var command = s.split(":", 2)[1];
                        dispatchConsoleCommand(command);
                    }
                } else if (s.startsWith("bungee:") || s.startsWith("proxy:")) {
                    if (playerItemInfo.getFormat().isAllowBungeecordPlayerSending()) {
                        var server = s.split(":", 2)[1];
                        movePlayerOnProxy(playerWrapper, server);
                    }
                } else {
                    var command = s.startsWith("player:") ? s.split(":", 2)[1] : s;
                    dispatchPlayerCommand(playerWrapper, command);
                }
            });
        }

        var postClickEvent = new PostClickEvent(playerWrapper, playerItemInfo, clickType, subInventory);
        playerItemInfo.getOriginal().getEventManager().fireEvent(postClickEvent);

        if (inventoryRenderer.isOpened()) {
            inventoryRenderer.render();
        }
    }

    protected abstract void dispatchPlayerCommand(PlayerWrapper playerWrapper, String command);

    protected abstract void dispatchConsoleCommand(String command);

    protected abstract void movePlayerOnProxy(PlayerWrapper playerWrapper, String server);

    protected abstract void openBook(PlayerWrapper playerWrapper, Item book);
}
