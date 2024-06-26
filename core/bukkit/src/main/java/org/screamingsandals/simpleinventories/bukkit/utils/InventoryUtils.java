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

package org.screamingsandals.simpleinventories.bukkit.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.utils.feature.PlatformFeature;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class InventoryUtils {
    private static final @NotNull PlatformFeature HOLDER_WITHOUT_SNAPSHOT = PlatformFeature.of(() -> Reflect.hasMethod(Inventory.class, "getHolder", boolean.class));

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

    public static InventoryHolder getInventoryHolderWithoutSnapshot(Inventory inventory) {
        if (HOLDER_WITHOUT_SNAPSHOT.isSupported()) {
            return inventory.getHolder(false);
        } else {
            return inventory.getHolder();
        }
    }
}
