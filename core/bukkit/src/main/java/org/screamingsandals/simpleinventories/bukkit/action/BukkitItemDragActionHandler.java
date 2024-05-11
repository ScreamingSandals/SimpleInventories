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

package org.screamingsandals.simpleinventories.bukkit.action;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.screamingsandals.lib.player.Players;
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
            if (!inventoryRenderer.getPlayer().equals(Players.wrapPlayer(player))) {
                event.setCancelled(true);
                return; // HOW???
            }
            if (event.getRawSlots().stream().anyMatch(slot -> primaryInventory.equals(InventoryUtils.getInventory(event.getView(), slot)))) {
                event.setCancelled(true);
            }
        }
    }
}
