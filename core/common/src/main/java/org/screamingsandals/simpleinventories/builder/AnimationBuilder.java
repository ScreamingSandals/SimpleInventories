package org.screamingsandals.simpleinventories.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemBuilder;
import org.screamingsandals.lib.material.builder.ItemFactory;

import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor(staticName = "of")
@Getter
public class AnimationBuilder {
    private final List<Item> stacks;

    public AnimationBuilder stack(Object material) {
        stacks.add(ItemFactory.build(material).orElse(ItemFactory.getAir()));
        return this;
    }

    public AnimationBuilder stack(Object material, Consumer<ItemBuilder> consumer) {
        stacks.add(ItemFactory.build(material, consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public AnimationBuilder stack(Consumer<ItemBuilder> consumer) {
        stacks.add(ItemFactory.build(consumer).orElse(ItemFactory.getAir()));
        return this;
    }

    public AnimationBuilder clear() {
        stacks.clear();
        return this;
    }
}
