/*
 * Copyright (C) 2026 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

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
