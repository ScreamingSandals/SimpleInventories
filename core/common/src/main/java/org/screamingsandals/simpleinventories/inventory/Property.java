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

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

@Data
@AllArgsConstructor
public class Property implements Cloneable {
    @NotNull
    @Setter(onMethod_ = @Deprecated) // unsafe
    private InventorySet inventorySet;
    @Setter(onMethod_ = @Deprecated) // unsafe
    private String propertyName;
    @Setter(onMethod_ = @Deprecated) // unsafe
    private ConfigurationNode propertyData;

    public Property(@NotNull InventorySet inventorySet, String propertyName) {
        this(inventorySet, propertyName, BasicConfigurationNode.root());
    }

    public boolean hasName() {
        return propertyName != null;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Property clone() {
        return new Property(inventorySet, propertyName, propertyData.copy());
    }
}
