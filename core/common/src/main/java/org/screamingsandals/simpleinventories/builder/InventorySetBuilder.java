package org.screamingsandals.simpleinventories.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.events.*;
import org.screamingsandals.simpleinventories.inventory.Include;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

import java.util.List;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class InventorySetBuilder extends CategoryBuilder {
    private final InventorySet inventory;

    @Override
    public SubInventory getSubInventory() {
        if (subInventory == null) {
            subInventory = inventory.getMainSubInventory();
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
        inventory.getVariableToPropertyMap().put(variable, property);
        return this;
    }

    public InventorySetBuilder genericShop(boolean genericShop) {
        inventory.setGenericShop(genericShop);
        return this;
    }

    public InventorySetBuilder genericShopPriceTypeRequired(boolean genericShop) {
        inventory.setGenericShopPriceTypeRequired(genericShop);
        return this;
    }

    public InventorySetBuilder animationsEnabled(boolean animationsEnabled) {
        inventory.setAnimationsEnabled(animationsEnabled);
        return this;
    }

    public InventorySetBuilder allowAccessToConsole(boolean allowAccessToConsole) {
        inventory.setAllowAccessToConsole(allowAccessToConsole);
        return this;
    }

    public InventorySetBuilder allowBungeecordPlayerSending(boolean allowBungeecordPlayerSending) {
        inventory.setAllowBungeecordPlayerSending(allowBungeecordPlayerSending);
        return this;
    }

    public InventorySetBuilder define(String definition) {
        BuilderUtils.buildDefinition(inventory, definition);
        return this;
    }

    public InventorySetBuilder render(Consumer<ItemRenderEvent> consumer) {
        inventory.getEventManager().register(ItemRenderEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder preClick(Consumer<PreClickEvent> consumer) {
        inventory.getEventManager().register(PreClickEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder click(Consumer<PostClickEvent> consumer) {
        inventory.getEventManager().register(PostClickEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder open(Consumer<SubInventoryOpenEvent> consumer) {
        inventory.getEventManager().register(SubInventoryOpenEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder close(Consumer<SubInventoryCloseEvent> consumer) {
        inventory.getEventManager().register(SubInventoryCloseEvent.class, consumer);
        return this;
    }

    public InventorySetBuilder buy(Consumer<OnTradeEvent> consumer) {
        inventory.getEventManager().register(OnTradeEvent.class, consumer);
        return this;
    }

    /* TODO: Create annotation processor that will generate overrides of self-returning methods you can see below */

    @Override
    public InventorySetBuilder categoryOptions(Consumer<LocalOptionsBuilder> consumer) {
        return (InventorySetBuilder) super.categoryOptions(consumer);
    }

    @Override
    public InventorySetBuilder category(Object material) {
        return (InventorySetBuilder) super.category(material);
    }

    @Override
    public InventorySetBuilder category(Object material, Consumer<ItemInfoBuilder> consumer) {
        return (InventorySetBuilder) super.category(material, consumer);
    }

    @Override
    public InventorySetBuilder item(Object material) {
        return (InventorySetBuilder) super.item(material);
    }

    @Override
    public InventorySetBuilder item(Object material, Consumer<ItemInfoBuilder> consumer) {
        return (InventorySetBuilder) super.item(material, consumer);
    }

    @Override
    public InventorySetBuilder cosmetic() {
        return (InventorySetBuilder) super.cosmetic();
    }

    @Override
    public InventorySetBuilder cosmetic(Consumer<ItemInfoBuilder> consumer) {
        return (InventorySetBuilder) super.cosmetic(consumer);
    }

    @Override
    public InventorySetBuilder itemClone(String link) {
        return (InventorySetBuilder) super.itemClone(link);
    }

    @Override
    public InventorySetBuilder itemClone(String link, Consumer<ItemInfoBuilder> consumer) {
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
    public InventorySetBuilder hidden(String id, Consumer<CategoryBuilder> consumer) {
        return (InventorySetBuilder) super.hidden(id, consumer);
    }

    @Override
    public InventorySetBuilder hidden(String id) {
        return (InventorySetBuilder) super.hidden(id);
    }

    @Override
    public InventorySetBuilder insert(String link, Consumer<CategoryBuilder> consumer) {
        return (InventorySetBuilder) super.insert(link, consumer);
    }

    @Override
    public InventorySetBuilder insert(String link, SubInventory prebuiltInventory) {
        return (InventorySetBuilder) super.insert(link, prebuiltInventory);
    }

    @Override
    public InventorySetBuilder insert(List<String> links, Consumer<CategoryBuilder> consumer) {
        return (InventorySetBuilder) super.insert(links, consumer);
    }

    @Override
    public InventorySetBuilder insert(List<String> links, SubInventory prebuiltInventory) {
        return (InventorySetBuilder) super.insert(links, prebuiltInventory);
    }
}
