package org.screamingsandals.simpleinventories.bukkit.action;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;

public class BukkitItemMoveActionHandler implements Listener {
    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        var possibleHolder = event.getSource().getHolder();
        var possibleDestHolder = event.getDestination().getHolder();

        if (possibleHolder instanceof AbstractHolder || possibleDestHolder instanceof AbstractHolder) {
            event.setCancelled(true);
        }
    }
}
