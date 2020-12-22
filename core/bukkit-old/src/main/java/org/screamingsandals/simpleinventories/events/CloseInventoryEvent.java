package org.screamingsandals.simpleinventories.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.inventory.GuiHolder;

@Getter
@RequiredArgsConstructor
public class CloseInventoryEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final SimpleInventories format;
    private final GuiHolder guiHolder;
    private final Inventory inventory;
    private boolean isCanceled = false;

    public static HandlerList getHandlerList() {
        return CloseInventoryEvent.handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return CloseInventoryEvent.handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.isCanceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCanceled = cancel;
    }
}
