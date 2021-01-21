package org.screamingsandals.simpleinventories.bukkit.action;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.simpleinventories.action.CloseInventoryActionHandler;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;

public class BukkitCloseInventoryActionHandler extends CloseInventoryActionHandler implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        var primaryInventory = event.getInventory();
        var possibleHolder = primaryInventory.getHolder();

        if (possibleHolder instanceof AbstractHolder) {
            var player = (Player) event.getPlayer();
            var inventoryRenderer = ((AbstractHolder) possibleHolder).getInventoryRenderer();
            if (!inventoryRenderer.getPlayer().equals(PlayerMapper.wrapPlayer(player))) {
                return; // HOW???
            }
            handleAction(inventoryRenderer);
        }
    }
}
