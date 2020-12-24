package org.screamingsandals.simpleinventories.builder;

import lombok.NonNull;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.simpleinventories.utils.ConsumerExecutor;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractSubInventoryBuilder {

    public AbstractSubInventoryBuilder category(Object material) {
        item(material);
        return this;
    }

    public AbstractSubInventoryBuilder category(Object material, Consumer<ItemInfoBuilder> consumer) {
        item(material, consumer);
        return this;
    }

    public AbstractSubInventoryBuilder item(Object material) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        return this;
    }

    public AbstractSubInventoryBuilder item(Object material, Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, ItemInfoBuilder.of(itemInfo));
        return this;
    }

    public AbstractSubInventoryBuilder cosmetic() {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink("cosmetic");
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        return this;
    }

    public AbstractSubInventoryBuilder cosmetic(Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink("cosmetic");
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, ItemInfoBuilder.of(itemInfo));
        return this;
    }

    public AbstractSubInventoryBuilder itemClone(String link) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink(link);
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        return this;
    }

    public AbstractSubInventoryBuilder itemClone(String link, Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink(link);
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, ItemInfoBuilder.of(itemInfo));
        return this;
    }

    public AbstractSubInventoryBuilder include(String include) {
        putObjectToQueue(new Include(include));
        return this;
    }

    public AbstractSubInventoryBuilder hidden(String id, Consumer<CategoryBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        itemInfo.setWritten(() -> false);
        itemInfo.setId(id);
        itemInfo.setChildInventory(new SubInventory(false, itemInfo, getFormat()));
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, new CategoryBuilder(itemInfo.getChildInventory()));
        return this;
    }

    public AbstractSubInventoryBuilder hidden(String id) {
        var itemInfo = new GenericItemInfo(getFormat());
        itemInfo.setWritten(() -> false);
        itemInfo.setId(id);
        putObjectToQueue(itemInfo);
        return this;
    }

    public AbstractSubInventoryBuilder insert(String link, Consumer<CategoryBuilder> consumer) {
        var insert = new Insert(link, new SubInventory(false, null, getFormat()));
        putObjectToQueue(insert);
        ConsumerExecutor.execute(consumer, new CategoryBuilder(insert.getSubInventory()));
        return this;
    }

    public AbstractSubInventoryBuilder insert(String link, SubInventory prebuiltInventory) {
        var insert = new Insert(link, prebuiltInventory);
        putObjectToQueue(insert);
        return this;
    }

    public AbstractSubInventoryBuilder insert(List<String> links, Consumer<CategoryBuilder> consumer) {
        var prebuiltInventory = new SubInventory(false, null, getFormat());
        ConsumerExecutor.execute(consumer, new CategoryBuilder(prebuiltInventory));
        return insert(links, prebuiltInventory);
    }

    public AbstractSubInventoryBuilder insert(List<String> links, SubInventory prebuiltInventory) {
        links.forEach(s -> insert(s, prebuiltInventory));
        return this;
    }

    protected abstract Inventory getFormat();
    protected abstract void putObjectToQueue(@NonNull Queueable queueable);

    protected GenericItemInfo build(Object stack) {
        return BuilderUtils.buildItem(getFormat(), stack);
    }

    /**
     * Groovy only
     *
     * @param consumer Consumer that consumes AbstractSubInventoryBuilder
     */
    public void call(Consumer<AbstractSubInventoryBuilder> consumer) {
        ConsumerExecutor.execute(consumer, this);
    }
}
