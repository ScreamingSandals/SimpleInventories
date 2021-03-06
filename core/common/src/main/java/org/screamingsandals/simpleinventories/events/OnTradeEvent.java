package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.inventory.Price;
import org.screamingsandals.simpleinventories.inventory.Property;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.simpleinventories.utils.ClickType;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class OnTradeEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final List<Price> prices;
    private final Item stack;
    private final PlayerItemInfo item;
    private final ClickType clickType;

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
}
