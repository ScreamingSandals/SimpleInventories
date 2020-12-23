package org.screamingsandals.simpleinventories.bukkit.action;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.screamingsandals.simpleinventories.bukkit.SimpleInventoriesBukkit;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;
import org.screamingsandals.simpleinventories.bukkit.utils.InventoryUtils;

public class BukkitItemDragActionHandler implements Listener {
    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {
        if (event.isCancelled() || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        var primaryInventory = event.getInventory();
        var possibleHolder = primaryInventory.getHolder();

        if (possibleHolder instanceof AbstractHolder) {
            var player = (Player) event.getWhoClicked();
            var inventoryRenderer = ((AbstractHolder) possibleHolder).getInventoryRenderer();
            if (!inventoryRenderer.getPlayer().equals(SimpleInventoriesBukkit.wrapPlayer(player))) {
                event.setCancelled(true);
                return; // HOW???
            }
            if (event.getRawSlots().stream().anyMatch(slot -> primaryInventory.equals(InventoryUtils.getInventory(event.getView(), slot)))) {
                event.setCancelled(true);
            }
        }
    }
}
