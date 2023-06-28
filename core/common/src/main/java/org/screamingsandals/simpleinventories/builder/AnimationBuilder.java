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
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.builder.ItemStackBuilder;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.utils.ReceiverConsumer;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@Getter
public class AnimationBuilder {
    private final List<ItemStack> stacks;

    public AnimationBuilder stack(Object material) {
        var item = ItemStackFactory.build(material);
        stacks.add(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public AnimationBuilder stack(Object material, ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(material, consumer);
        stacks.add(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public AnimationBuilder stack(ReceiverConsumer<ItemStackBuilder> consumer) {
        var item = ItemStackFactory.build(consumer);
        stacks.add(item != null ? item : ItemStackFactory.getAir());
        return this;
    }

    public AnimationBuilder clear() {
        stacks.clear();
        return this;
    }
}
