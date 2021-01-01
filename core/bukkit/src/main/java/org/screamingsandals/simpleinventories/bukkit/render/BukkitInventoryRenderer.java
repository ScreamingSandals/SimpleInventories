package org.screamingsandals.simpleinventories.bukkit.render;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;
import org.screamingsandals.simpleinventories.bukkit.holder.StandardInventoryHolder;
import org.screamingsandals.simpleinventories.bukkit.tasks.BukkitRepeatingTask;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;
import java.util.Optional;

public class BukkitInventoryRenderer extends InventoryRenderer {
    private AbstractHolder inventoryHolder;

    private static final List<String> SIZEABLE_CONTAINERS = List.of("CHEST", "ENDER_CHEST", "SHULKER_BOX", "BARREL");

    public BukkitInventoryRenderer(PlayerWrapper player, SubInventory subInventory, int page) {
        super(player, subInventory, page);
    }

    @Override
    protected void renderOnPlatform() {
        boolean reopen = true;
        var options = subInventory.getLocalOptions();
        if (inventoryHolder != null && inventoryHolder.getInventory().getSize() == options.getRenderActualRows() * options.getItemsOnRow()
                && (inventoryHolder.getInventory().getType().name().equalsIgnoreCase(options.getInventoryType()) ||
                (SIZEABLE_CONTAINERS.contains(options.getInventoryType().toUpperCase()) &&
                        SIZEABLE_CONTAINERS.contains(inventoryHolder.getInventory().getType().name())))) {
            inventoryHolder.getInventory().clear();
            reopen = false;
        } else {
            inventoryHolder = new StandardInventoryHolder();
        }
        Inventory inventory;
        if (SIZEABLE_CONTAINERS.contains(options.getInventoryType().toUpperCase())) {
            inventory = Bukkit.createInventory(inventoryHolder, options.getRenderActualRows() * options.getItemsOnRow(), getTitle());
        } else {
            inventory = Bukkit.createInventory(inventoryHolder, InventoryType.valueOf(options.getInventoryType().toUpperCase()), getTitle());
        }
        inventoryHolder.setInventoryRenderer(this);
        inventoryHolder.setInventory(inventory);
        inventoryHolder.setSubInventory(subInventory);

        itemStacksInInventory.forEach((position, item) -> {
            if (position < inventory.getSize()) {
                inventory.setItem(position, item.as(ItemStack.class));
            }
        });

        if (!animations.isEmpty()) {
            var task = new BukkitRepeatingTask();
            task.setPeriod(20L);
            task.setDelay(1L);
            task.setTask(() ->
                animations.forEach((position, items) ->
                    inventory.setItem(position, items.get(nextAnimationPosition % items.size()).as(ItemStack.class))
                ));
            animator = task;
        }

        if (reopen) {
            Optional.ofNullable(player.as(Player.class)).ifPresent(p -> p.openInventory(inventory));
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
