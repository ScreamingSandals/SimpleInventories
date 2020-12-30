package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.inventory.Price;
import org.screamingsandals.simpleinventories.inventory.Property;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.List;

@Data
@RequiredArgsConstructor
public class OnTradeEvent implements Cancellable {
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
        return player.hasInInventory(item);
    }

    public List<Item> sellStack() {
        return sellStack(this.stack);
    }

    public List<Item> sellStack(Item item) {
        return player.removeItem(item);
    }

    public List<Item> buyStack() {
        return buyStack(this.stack);
    }

    public List<Item> buyStack(Item item) {
        return player.addItem(item);
    }

    public List<Property> getProperties() {
        return item.getProperties();
    }

    public boolean hasProperties() {
        return item.hasProperties();
    }
}
