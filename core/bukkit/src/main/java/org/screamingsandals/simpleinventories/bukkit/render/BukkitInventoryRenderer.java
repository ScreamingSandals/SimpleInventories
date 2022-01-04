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

package org.screamingsandals.simpleinventories.bukkit.render;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;
import org.screamingsandals.simpleinventories.bukkit.holder.StandardInventoryHolder;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

public class BukkitInventoryRenderer extends InventoryRenderer {
    private AbstractHolder inventoryHolder;

    private static final List<String> SIZEABLE_CONTAINERS = List.of("CHEST", "ENDER_CHEST", "SHULKER_BOX", "BARREL");

    public BukkitInventoryRenderer(PlayerWrapper player, SubInventory subInventory, int page) {
        super(player, subInventory, page);
    }

    @Override
    protected void renderOnPlatform() {
        var options = subInventory.getLocalOptions();
        if (inventoryHolder != null && inventoryHolder.getInventory().getSize() == options.getRenderActualRows() * options.getItemsOnRow()
                && (inventoryHolder.getInventory().getType().name().equalsIgnoreCase(options.getInventoryType()) ||
                (SIZEABLE_CONTAINERS.contains(options.getInventoryType().toUpperCase()) &&
                        SIZEABLE_CONTAINERS.contains(inventoryHolder.getInventory().getType().name())))) {
            inventoryHolder.getInventory().clear();
        } else {
            inventoryHolder = new StandardInventoryHolder();
        }
        Inventory inventory;
        if (SIZEABLE_CONTAINERS.contains(options.getInventoryType().toUpperCase())) {
            inventory = Bukkit.createInventory(
                    inventoryHolder, options.getRenderActualRows() * options.getItemsOnRow(),
                    AdventureHelper.toLegacy(getTitle()));
        } else {
            inventory = Bukkit.createInventory(
                    inventoryHolder, InventoryType.valueOf(options.getInventoryType().toUpperCase()),
                    AdventureHelper.toLegacy(getTitle()));
        }
        inventoryHolder.setInventoryRenderer(this);
        inventoryHolder.setInventory(inventory);
        inventoryHolder.setSubInventory(subInventory);

        var emptyItem = options.getEmptySlotItem();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, emptyItem.as(ItemStack.class));
        }

        itemStacksInInventory.forEach((position, item) -> {
            if (position < inventory.getSize()) {
                var asStack = item.as(ItemStack.class);
                if (!item.getMaterial().isAir()) {
                    inventory.setItem(position, asStack);
                }
            }
        });

        player.asOptional(Player.class).ifPresent(p -> p.openInventory(inventory));

        if (!animations.isEmpty()) {
            animator = Tasker.build(() ->
                    animations.forEach((position, items) ->
                            inventory.setItem(position, items.get(nextAnimationPosition % items.size()).as(ItemStack.class))
                    ))
                    .delay(1L, TaskerTime.TICKS)
                    .repeat(20L, TaskerTime.TICKS)
                    .start();
        }
    }

    @Override
    public boolean isOpened() {
        return player.as(Player.class).getOpenInventory().getTopInventory().getHolder() == inventoryHolder;
    }

    @Override
    public void close() {
        super.close();
        player.closeInventory();
    }
}
