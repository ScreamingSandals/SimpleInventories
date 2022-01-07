/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.inventory.Price;
import org.screamingsandals.simpleinventories.inventory.Property;

import java.util.List;

@Data
@RequiredArgsConstructor
public class OnTradeEvent implements SCancellableEvent {
    private final PlayerWrapper player;
    private final List<Price> prices;
    private final Item stack;
    private final PlayerItemInfo item;
    private final ClickType clickType;
    private boolean cancelled;

    public InventorySet getFormat() {
        return item.getFormat();
    }

    @Deprecated
    public int getPrice() {
        return prices.get(0).getAmount();
    }

    @Deprecated
    public String getType() {
        return prices.get(0).getCurrency();
    }

    public boolean hasPlayerInInventory() {
        return hasPlayerInInventory(this.stack);
    }

    public boolean hasPlayerInInventory(Item item) {
        return player.getPlayerInventory().containsAtLeast(item, item.getAmount());
    }

    public List<Item> sellStack() {
        return sellStack(this.stack);
    }

    public List<Item> sellStack(Item item) {
        return player.getPlayerInventory().removeItem(item);
    }

    public List<Item> buyStack() {
        return buyStack(this.stack);
    }

    public List<Item> buyStack(Item item) {
        return player.getPlayerInventory().addItem(item);
    }

    public List<Property> getProperties() {
        return item.getProperties();
    }

    public boolean hasProperties() {
        return item.hasProperties();
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public void cancelled(boolean cancel) {
        cancelled = cancel;
    }
}
