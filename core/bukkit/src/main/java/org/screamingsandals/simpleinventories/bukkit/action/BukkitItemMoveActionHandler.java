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
