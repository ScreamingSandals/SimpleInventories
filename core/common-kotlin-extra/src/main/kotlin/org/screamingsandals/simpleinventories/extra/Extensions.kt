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

package org.screamingsandals.simpleinventories.extra

import org.screamingsandals.simpleinventories.SimpleInventoriesCore
import org.screamingsandals.simpleinventories.builder.InventorySetBuilder
import org.screamingsandals.simpleinventories.builder.LocalOptionsBuilder
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo
import org.screamingsandals.simpleinventories.inventory.Property
import org.screamingsandals.simpleinventories.inventory.SubInventory

inline fun inventoryBuilder(action: InventorySetBuilder.() -> Unit): SubInventory {
    val setBuilder = SimpleInventoriesCore.builder()
    action.invoke(setBuilder)
    return setBuilder.process()
}

inline fun InventorySetBuilder.options(action: LocalOptionsBuilder.() -> Unit): InventorySetBuilder {
    action.invoke(categoryOptions)
    return this
}

fun PlayerItemInfo.findPropertyByName(name: String): Property? {
    return getFirstPropertyByName(name).orElse(null)
}

fun PlayerItemInfo.findPropertyByName(name: String, action: Property.() -> Unit): Property? {
    val property = findPropertyByName(name)
    if (property != null) {
        action.invoke(property)
    }
    return property
}