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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.simpleinventories.action.CloseInventoryActionHandler;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;
import org.screamingsandals.simpleinventories.bukkit.utils.InventoryUtils;

public class BukkitCloseInventoryActionHandler extends CloseInventoryActionHandler implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        var primaryInventory = event.getInventory();
        var possibleHolder = InventoryUtils.getInventoryHolderWithoutSnapshot(primaryInventory);

        if (possibleHolder instanceof AbstractHolder) {
            var player = (Player) event.getPlayer();
            var inventoryRenderer = ((AbstractHolder) possibleHolder).getInventoryRenderer();
            if (!inventoryRenderer.getPlayer().equals(Players.wrapPlayer(player))) {
                return; // HOW???
            }
            handleAction(inventoryRenderer);
        }
    }
}
