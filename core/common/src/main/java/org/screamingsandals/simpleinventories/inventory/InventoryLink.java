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

package org.screamingsandals.simpleinventories.inventory;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryLink {
    @ToString.Exclude
    private InventorySet inventorySet;
    private String link;

    private SubInventory subInventory;

    @NotNull
    public static InventoryLink of(@NotNull InventorySet inventorySet, @NotNull String link) {
        var locate = new InventoryLink();
        locate.inventorySet = inventorySet;
        locate.link = link;
        return locate;
    }

    @NotNull
    public static InventoryLink of(@NotNull SubInventory subInventory) {
        var locate = new InventoryLink();
        locate.subInventory = subInventory;
        return locate;
    }

    @NotNull
    public static InventoryLink of(@NotNull InventorySet inventorySet) {
        return of(inventorySet.getMainSubInventory());
    }

    public Optional<SubInventory> resolve() {
        if (subInventory != null) {
            return Optional.of(subInventory);
        } else {
            return inventorySet.resolveCategoryLink(link);
        }
    }
}
