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