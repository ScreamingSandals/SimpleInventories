package org.screamingsandals.simpleinventories.builder;

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

    public AbstractSubInventoryBuilder item(Object category) {
        return this;
    }

    public AbstractSubInventoryBuilder item(Object material, Consumer<ItemInfoBuilder> consumer) {
        return this;
    }

    public AbstractSubInventoryBuilder cosmetic() {

        return this;
    }

    public AbstractSubInventoryBuilder cosmetic(Consumer<ItemInfoBuilder> consumer) {

        return this;
    }

    public AbstractSubInventoryBuilder itemClone(String clone) {

        return this;
    }

    public AbstractSubInventoryBuilder itemClone(String clone, Consumer<ItemInfoBuilder> consumer) {

        return this;
    }

    public AbstractSubInventoryBuilder include(String include) {

        return this;
    }

    public AbstractSubInventoryBuilder hidden(String id, Consumer<CategoryBuilder> consumer) {

        return this;
    }

    public AbstractSubInventoryBuilder hidden(String id) {

        return this;
    }

    public AbstractSubInventoryBuilder insert(String insert, Consumer<CategoryBuilder> consumer) {

        return this;
    }

    public AbstractSubInventoryBuilder insert(List<String> insert, Consumer<CategoryBuilder> consumer) {
        insert.forEach(s -> insert(s, consumer));
        return this;
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
