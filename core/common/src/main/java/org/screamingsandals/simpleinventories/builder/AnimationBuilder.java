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

package org.screamingsandals.simpleinventories.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.builder.ItemFactory;

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
