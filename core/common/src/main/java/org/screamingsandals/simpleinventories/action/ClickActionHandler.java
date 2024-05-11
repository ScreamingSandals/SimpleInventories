/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.simpleinventories.action;

import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.simpleinventories.events.OnTradeEvent;
import org.screamingsandals.simpleinventories.events.PostClickEvent;
import org.screamingsandals.simpleinventories.events.PreClickEvent;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.lib.player.Player;

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

        if (preClickEvent.cancelled()) {
            inventoryRenderer.close();
            return;
        }

        if (playerItemInfo == null) {
            if (slot == options.getRenderHeaderStart()) {
                if (!subInventory.isMain() && parentItem != null) {
                    var parent = parentItem.getParent();
                    var pageOfParent = parentItem instanceof GenericItemInfo ? ((GenericItemInfo) parentItem).getPosition() / parent.getLocalOptions().getItemsOnPage() : 0;
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
            playerWrapper.openBook(playerItemInfo.getOriginal().getBook());
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
            var list = playerItemInfo.getOriginal().getPrices().stream().map(price -> {
                var price1 = price.clone();
                if (price1.getCurrency() == null) {
                    price1.setCurrency(playerItemInfo.getOriginal().getDefaultCurrency());
                }
                if (price1.getCurrency() == null && playerItemInfo.getFormat().isGenericShopPriceTypeRequired()) {
                    price1 = null;
                }
                return price1;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            if (!list.isEmpty()) {
                var tradeEvent = new OnTradeEvent(playerWrapper, list, playerItemInfo.getOriginal().getItem().clone(), playerItemInfo, clickType);
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

    protected abstract void dispatchPlayerCommand(Player playerWrapper, String command);

    protected abstract void dispatchConsoleCommand(String command);

    protected abstract void movePlayerOnProxy(Player playerWrapper, String server);
}
