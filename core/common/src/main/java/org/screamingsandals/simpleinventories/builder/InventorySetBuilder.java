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

package org.screamingsandals.simpleinventories.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.ReceiverConsumer;
import org.screamingsandals.simpleinventories.events.*;
import org.screamingsandals.simpleinventories.inventory.Include;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.inventory.SubInventoryLike;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;

import java.util.List;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class InventorySetBuilder extends CategoryBuilder {
    private final InventorySet inventorySet;

    @Override
    public SubInventory getSubInventory() {
        if (subInventory == null) {
            subInventory = inventorySet.getMainSubInventory();
        }
        return subInventory;
    }

    /**
     * Custom variables were replaced with properties. This method remaps the old variables to properties.
     *
     * @param variable the name of custom variable
     * @param property the name of property
     * @return self
     */
    public InventorySetBuilder variableToProperty(String variable, String property) {
        inventorySet.getVariableToPropertyMap().put(variable, property);
        return this;
    }

    public InventorySetBuilder genericShop(boolean genericShop) {
        inventorySet.setGenericShop(genericShop);
        return this;
    }

    public InventorySetBuilder genericShopPriceTypeRequired(boolean genericShop) {
        inventorySet.setGenericShopPriceTypeRequired(genericShop);
        return this;
    }

    public InventorySetBuilder animationsEnabled(boolean animationsEnabled) {
        inventorySet.setAnimationsEnabled(animationsEnabled);
        return this;
    }

    public InventorySetBuilder allowAccessToConsole(boolean allowAccessToConsole) {
        inventorySet.setAllowAccessToConsole(allowAccessToConsole);
        return this;
    }

    public InventorySetBuilder allowBungeecordPlayerSending(boolean allowBungeecordPlayerSending) {
        inventorySet.setAllowBungeecordPlayerSending(allowBungeecordPlayerSending);
        return this;
    }

    public InventorySetBuilder define(String definition) {
        BuilderUtils.buildDefinition(inventorySet, definition);
        return this;
    }

    public InventorySetBuilder define(String placeholder, IPlaceholderParser placeholderParser) {
        inventorySet.registerPlaceholder(placeholder, placeholderParser);
        return this;
    }

    public InventorySetBuilder define(String placeholder, String value) {
        inventorySet.registerPlaceholder(placeholder, value);
        return this;
    }

    public InventorySetBuilder render(ReceiverConsumer<ItemRenderEvent> consumer) {
        inventorySet.getEventManager().register(ItemRenderEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder preClick(ReceiverConsumer<PreClickEvent> consumer) {
        inventorySet.getEventManager().register(PreClickEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder click(ReceiverConsumer<PostClickEvent> consumer) {
        inventorySet.getEventManager().register(PostClickEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder open(ReceiverConsumer<SubInventoryOpenEvent> consumer) {
        inventorySet.getEventManager().register(SubInventoryOpenEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder close(ReceiverConsumer<SubInventoryCloseEvent> consumer) {
        inventorySet.getEventManager().register(SubInventoryCloseEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder buy(ReceiverConsumer<OnTradeEvent> consumer) {
        inventorySet.getEventManager().register(OnTradeEvent.class, consumer);
        return this;
    }

    /* TODO: Create annotation processor that will generate overrides of self-returning methods you can see below */

    @Override
    public InventorySetBuilder categoryOptions(ReceiverConsumer<LocalOptionsBuilder> consumer) {
        return (InventorySetBuilder) super.categoryOptions(consumer);
    }

    @Override
    public InventorySetBuilder category(Object material) {
        return (InventorySetBuilder) super.category(material);
    }

    @Override
    public InventorySetBuilder category(Object material, ReceiverConsumer<ItemInfoBuilder> consumer) {
        return (InventorySetBuilder) super.category(material, consumer);
    }

    @Override
    public InventorySetBuilder item(Object material) {
        return (InventorySetBuilder) super.item(material);
    }

    @Override
    public InventorySetBuilder item(Object material, ReceiverConsumer<ItemInfoBuilder> consumer) {
        return (InventorySetBuilder) super.item(material, consumer);
    }

    @Override
    public InventorySetBuilder cosmetic() {
        return (InventorySetBuilder) super.cosmetic();
    }

    @Override
    public InventorySetBuilder cosmetic(ReceiverConsumer<ItemInfoBuilder> consumer) {
        return (InventorySetBuilder) super.cosmetic(consumer);
    }

    @Override
    public InventorySetBuilder itemClone(String link) {
        return (InventorySetBuilder) super.itemClone(link);
    }

    @Override
    public InventorySetBuilder itemClone(String link, ReceiverConsumer<ItemInfoBuilder> consumer) {
        return (InventorySetBuilder) super.itemClone(link, consumer);
    }

    @Override
    public InventorySetBuilder include(String include) {
        return (InventorySetBuilder) super.include(include);
    }

    @Override
    public InventorySetBuilder include(Include include) {
        return (InventorySetBuilder) super.include(include);
    }

    @Override
    public InventorySetBuilder hidden(String id, ReceiverConsumer<CategoryBuilder> consumer) {
        return (InventorySetBuilder) super.hidden(id, consumer);
    }

    @Override
    public InventorySetBuilder hidden(String id) {
        return (InventorySetBuilder) super.hidden(id);
    }

    @Override
    public InventorySetBuilder insert(String link, ReceiverConsumer<QueueBuilder> consumer) {
        return (InventorySetBuilder) super.insert(link, consumer);
    }

    @Override
    public InventorySetBuilder insert(String link, SubInventoryLike<?> prebuiltInventory) {
        return (InventorySetBuilder) super.insert(link, prebuiltInventory);
    }

    @Override
    public InventorySetBuilder insert(List<String> links, ReceiverConsumer<QueueBuilder> consumer) {
        return (InventorySetBuilder) super.insert(links, consumer);
    }

    @Override
    public InventorySetBuilder insert(List<String> links, SubInventoryLike<?> prebuiltInventory) {
        return (InventorySetBuilder) super.insert(links, prebuiltInventory);
    }

    @Override
    public InventorySetBuilder call(ReceiverConsumer<CategoryBuilder> consumer) {
        return (InventorySetBuilder) super.call(consumer);
    }
}
