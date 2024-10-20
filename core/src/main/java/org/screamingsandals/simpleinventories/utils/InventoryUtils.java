package org.screamingsandals.simpleinventories.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryUtils {
    private static final boolean IS_HOLDER_WITHOUT_SNAPSHOT_SUPPORTED;

    static {
        boolean isSupported;
        try {
            Inventory.class.getMethod("getHolder", boolean.class);
            isSupported = true;
        } catch (NoSuchMethodException e) {
            isSupported = false;
        }
        IS_HOLDER_WITHOUT_SNAPSHOT_SUPPORTED = isSupported;
    }

    public static InventoryHolder getInventoryHolderWithoutSnapshot(Inventory inventory) {
        if (IS_HOLDER_WITHOUT_SNAPSHOT_SUPPORTED) {
            return inventory.getHolder(false);
        } else {
            return inventory.getHolder();
        }
    }
}
