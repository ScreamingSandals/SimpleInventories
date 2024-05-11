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

import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.simpleinventories.action.ClickActionHandler;
import org.screamingsandals.simpleinventories.bukkit.SimpleInventoriesBukkit;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;
import org.screamingsandals.simpleinventories.bukkit.utils.InventoryUtils;

public class BukkitClickActionHandler extends ClickActionHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled() || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        var primaryInventory = event.getInventory();
        var possibleHolder = primaryInventory.getHolder();
        // TODO: Tile inventory holder converter
        if (possibleHolder instanceof AbstractHolder) {
            var player = (Player) event.getWhoClicked();
            var inventoryRenderer = ((AbstractHolder) possibleHolder).getInventoryRenderer();
            if (!inventoryRenderer.getPlayer().equals(Players.wrapPlayer(player))) {
                event.setCancelled(true);
                return; // HOW???
            }
            var inventory = InventoryUtils.getInventory(event.getView(), event.getRawSlot());
            if (!primaryInventory.equals(inventory)) {
                if (event.getClick().isShiftClick() || event.getClick().isKeyboardClick() || event.getClick().isCreativeAction()) {
                    event.setCancelled(true);
                }
                return;
            }
            event.setCancelled(true);
            handleAction(inventoryRenderer, event.getSlot(), ClickType.convert(event.getClick().name()));
        }
    }

    @Override
    protected void dispatchPlayerCommand(org.screamingsandals.lib.player.Player playerWrapper, String command) {
        playerWrapper.as(Player.class).performCommand(command);
    }

    @Override
    protected void dispatchConsoleCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    @Override
    protected void movePlayerOnProxy(org.screamingsandals.lib.player.Player playerWrapper, String server) {
        if (!Bukkit.getMessenger().getOutgoingChannels(SimpleInventoriesBukkit.getPlugin()).contains("BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(SimpleInventoriesBukkit.getPlugin(), "BungeeCord");
        }

        //noinspection UnstableApiUsage
        var out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);

        playerWrapper.as(Player.class).sendPluginMessage(SimpleInventoriesBukkit.getPlugin(), "BungeeCord", out.toByteArray());
    }
}
