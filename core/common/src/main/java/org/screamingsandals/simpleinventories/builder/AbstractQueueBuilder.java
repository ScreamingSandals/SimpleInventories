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

import lombok.NonNull;
import org.screamingsandals.lib.utils.ReceiverConsumer;
import org.screamingsandals.simpleinventories.inventory.*;

import java.util.List;

public abstract class AbstractQueueBuilder<T extends AbstractQueueBuilder<T>> {
    public T category(Object material) {
        item(material);
        return self();
    }

    public T category(Object material, ReceiverConsumer<ItemInfoBuilder> consumer) {
        item(material, consumer);
        return self();
    }

    public T item(Object material) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        return self();
    }

    public T item(Object material, ReceiverConsumer<ItemInfoBuilder> consumer) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        var builder = ItemInfoBuilder.of(itemInfo);
        consumer.accept(builder);
        builder.processItemBuilderIfOpened();
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

    public T cosmetic(ReceiverConsumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink("cosmetic");
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        var builder = ItemInfoBuilder.of(itemInfo);
        consumer.accept(builder);
        builder.processItemBuilderIfOpened();
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

    public T itemClone(String link, ReceiverConsumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink(link);
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        var builder = ItemInfoBuilder.of(itemInfo);
        consumer.accept(builder);
        builder.processItemBuilderIfOpened();
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

    public T hidden(String id, ReceiverConsumer<CategoryBuilder> consumer) {
        var itemInfo = new HiddenCategory(getFormat(), id);
        itemInfo.setChildInventory(new SubInventory(false, itemInfo, getFormat()));
        putObjectToQueue(itemInfo);
        consumer.accept(CategoryBuilder.of(itemInfo.getChildInventory()));
        return self();
    }

    public T hidden(String id) {
        var itemInfo = new HiddenCategory(getFormat(), id);
        itemInfo.setChildInventory(new SubInventory(false, itemInfo, getFormat()));
        putObjectToQueue(itemInfo);
        return self();
    }

    public T insert(String link, ReceiverConsumer<QueueBuilder> consumer) {
        var prebuiltInventory = SimpleItemQueue.of();
        var insert = new Insert(link, prebuiltInventory);
        putObjectToQueue(insert);
        consumer.accept(QueueBuilder.of(getFormat(), prebuiltInventory));
        return self();
    }

    public T insert(String link, SubInventoryLike<?> prebuiltInventory) {
        var insert = new Insert(link, prebuiltInventory);
        putObjectToQueue(insert);
        return self();
    }

    public T insert(List<String> links, ReceiverConsumer<QueueBuilder> consumer) {
        var prebuiltInventory = SimpleItemQueue.of();
        consumer.accept(QueueBuilder.of(getFormat(), prebuiltInventory));
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
    public T call(ReceiverConsumer<T> consumer) {
        consumer.accept(self());
        return self();
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    protected abstract void putObjectToQueue(@NonNull Queueable queueable);

    protected abstract InventorySet getFormat();

}
