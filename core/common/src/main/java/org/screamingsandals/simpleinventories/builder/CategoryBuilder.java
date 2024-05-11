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

import lombok.*;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.lib.utils.ReceiverConsumer;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class CategoryBuilder extends AbstractQueueBuilder<CategoryBuilder> {
    protected SubInventory subInventory;

    protected InventorySet getFormat() {
        return getSubInventory().getInventorySet();
    }

    protected void putObjectToQueue(@NonNull Queueable queueable) {
        getSubInventory().putIntoQueue(queueable);
    }

    public CategoryBuilder categoryOptions(ReceiverConsumer<LocalOptionsBuilder> consumer) {
        consumer.accept(getCategoryOptions());
        return this;
    }

    public LocalOptionsBuilder getCategoryOptions() {
        return LocalOptionsBuilder.of(getSubInventory().getLocalOptions());
    }

    /**
     * Ends the builder. Use only for InventorySetBuilder, it automatically triggers the rest!
     */
    public SubInventory process() {
        getSubInventory().process();
        return getSubInventory();
    }
}
