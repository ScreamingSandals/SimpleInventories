package org.screamingsandals.simpleinventories.builder;

import lombok.NonNull;
import org.screamingsandals.lib.utils.ConsumerExecutor;
import org.screamingsandals.simpleinventories.inventory.*;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractQueueBuilder<T extends AbstractQueueBuilder<T>> {
    public T category(Object material) {
        item(material);
        return self();
    }

    public T category(Object material, Consumer<ItemInfoBuilder> consumer) {
        item(material, consumer);
        return self();
    }

    public T item(Object material) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        return self();
    }

    public T item(Object material, Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, ItemInfoBuilder.of(itemInfo));
        return self();
    }

    public T cosmetic() {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink("cosmetic");
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        return self();
    }

    public T cosmetic(Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink("cosmetic");
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, ItemInfoBuilder.of(itemInfo));
        return self();
    }

    public T itemClone(String link) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink(link);
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        return self();
    }

    public T itemClone(String link, Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink(link);
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, ItemInfoBuilder.of(itemInfo));
        return self();
    }

    public T include(String include) {
        putObjectToQueue(Include.of(include));
        return self();
    }

    public T include(Include include) {
        putObjectToQueue(include);
        return self();
    }

    public T hidden(String id, Consumer<CategoryBuilder> consumer) {
        var itemInfo = new HiddenCategory(getFormat(), id);
        itemInfo.setChildInventory(new SubInventory(false, itemInfo, getFormat()));
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, CategoryBuilder.of(itemInfo.getChildInventory()));
        return self();
    }

    public T hidden(String id) {
        var itemInfo = new HiddenCategory(getFormat(), id);
        itemInfo.setChildInventory(new SubInventory(false, itemInfo, getFormat()));
        putObjectToQueue(itemInfo);
        return self();
    }

    public T insert(String link, Consumer<QueueBuilder> consumer) {
        var prebuiltInventory = SimpleItemQueue.of();
        var insert = new Insert(link, prebuiltInventory);
        putObjectToQueue(insert);
        ConsumerExecutor.execute(consumer, QueueBuilder.of(getFormat(), prebuiltInventory));
        return self();
    }

    public T insert(String link, SubInventoryLike<?> prebuiltInventory) {
        var insert = new Insert(link, prebuiltInventory);
        putObjectToQueue(insert);
        return self();
    }

    public T insert(List<String> links, Consumer<QueueBuilder> consumer) {
        var prebuiltInventory = SimpleItemQueue.of();
        ConsumerExecutor.execute(consumer, QueueBuilder.of(getFormat(), prebuiltInventory));
        return insert(links, prebuiltInventory);
    }

    public T insert(List<String> links, SubInventoryLike<?> prebuiltInventory) {
        links.forEach(s -> insert(s, prebuiltInventory));
        return self();
    }

    protected GenericItemInfo build(Object stack) {
        return BuilderUtils.buildItem(getFormat(), stack);
    }

    /**
     * @param consumer Consumer that consumes AbstractSubInventoryBuilder
     * @return returns self
     */
    public T call(Consumer<T> consumer) {
        ConsumerExecutor.execute(consumer, self());
        return self();
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    protected abstract void putObjectToQueue(@NonNull Queueable queueable);

    protected abstract InventorySet getFormat();

}
