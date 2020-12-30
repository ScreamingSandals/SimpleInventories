package org.screamingsandals.simpleinventories.builder;

import lombok.*;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.lib.utils.ConsumerExecutor;

import java.util.List;
import java.util.function.Consumer;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class CategoryBuilder {
    protected SubInventory subInventory;

    protected InventorySet getFormat() {
        return getSubInventory().getInventorySet();
    }

    protected void putObjectToQueue(@NonNull Queueable queueable) {
        getSubInventory().getWaitingQueue().add(queueable);
    }

    public CategoryBuilder categoryOptions(Consumer<LocalOptionsBuilder> consumer) {
        ConsumerExecutor.execute(consumer, getCategoryOptions());
        return this;
    }

    public LocalOptionsBuilder getCategoryOptions() {
        return LocalOptionsBuilder.of(getSubInventory().getLocalOptions());
    }


    public CategoryBuilder category(Object material) {
        item(material);
        return this;
    }

    public CategoryBuilder category(Object material, Consumer<ItemInfoBuilder> consumer) {
        item(material, consumer);
        return this;
    }

    public CategoryBuilder item(Object material) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        return this;
    }

    public CategoryBuilder item(Object material, Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, ItemInfoBuilder.of(itemInfo));
        return this;
    }

    public CategoryBuilder cosmetic() {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink("cosmetic");
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        return this;
    }

    public CategoryBuilder cosmetic(Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink("cosmetic");
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, ItemInfoBuilder.of(itemInfo));
        return this;
    }

    public CategoryBuilder itemClone(String link) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink(link);
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        return this;
    }

    public CategoryBuilder itemClone(String link, Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink(link);
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, ItemInfoBuilder.of(itemInfo));
        return this;
    }

    public CategoryBuilder include(String include) {
        putObjectToQueue(Include.of(include));
        return this;
    }

    public CategoryBuilder include(Include include) {
        putObjectToQueue(include);
        return this;
    }

    public CategoryBuilder hidden(String id, Consumer<CategoryBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        itemInfo.setWritten(() -> false);
        itemInfo.setId(id);
        itemInfo.setChildInventory(new SubInventory(false, itemInfo, getFormat()));
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, CategoryBuilder.of(itemInfo.getChildInventory()));
        return this;
    }

    public CategoryBuilder hidden(String id) {
        var itemInfo = new GenericItemInfo(getFormat());
        itemInfo.setWritten(() -> false);
        itemInfo.setId(id);
        putObjectToQueue(itemInfo);
        return this;
    }

    public CategoryBuilder insert(String link, Consumer<CategoryBuilder> consumer) {
        var insert = new Insert(link, new SubInventory(false, null, getFormat()));
        putObjectToQueue(insert);
        ConsumerExecutor.execute(consumer, CategoryBuilder.of(insert.getSubInventory()));
        return this;
    }

    public CategoryBuilder insert(String link, SubInventory prebuiltInventory) {
        var insert = new Insert(link, prebuiltInventory);
        putObjectToQueue(insert);
        return this;
    }

    public CategoryBuilder insert(List<String> links, Consumer<CategoryBuilder> consumer) {
        var prebuiltInventory = new SubInventory(false, null, getFormat());
        ConsumerExecutor.execute(consumer, CategoryBuilder.of(prebuiltInventory));
        return insert(links, prebuiltInventory);
    }

    public CategoryBuilder insert(List<String> links, SubInventory prebuiltInventory) {
        links.forEach(s -> insert(s, prebuiltInventory));
        return this;
    }

    /**
     * Ends the builder. Use only for InventorySetBuilder, it automatically triggers the rest!
     */
    public void process() {
        getSubInventory().process();
    }

    protected GenericItemInfo build(Object stack) {
        return BuilderUtils.buildItem(getFormat(), stack);
    }

    /**
     * Groovy only
     *
     * @param consumer Consumer that consumes AbstractSubInventoryBuilder
     */
    public void call(Consumer<CategoryBuilder> consumer) {
        ConsumerExecutor.execute(consumer, this);
    }
}
