package org.screamingsandals.simpleinventories.bukkit.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class InventoryUtils {

    public static Inventory getInventory(InventoryView view, int rawSlot) {
        if (rawSlot == InventoryView.OUTSIDE) {
            return null;
        }

        if (rawSlot < view.getTopInventory().getSize()) {
            return view.getTopInventory();
        } else {
            return view.getBottomInventory();
        }
    }
}
