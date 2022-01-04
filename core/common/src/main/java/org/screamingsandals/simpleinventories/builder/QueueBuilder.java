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

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.Queueable;
import org.screamingsandals.simpleinventories.inventory.SubInventoryLike;

@Getter
@RequiredArgsConstructor(staticName = "of")
public final class QueueBuilder extends AbstractQueueBuilder<QueueBuilder> {
    private final InventorySet format;
    private final SubInventoryLike<?> queue;

    @Override
    protected void putObjectToQueue(@NonNull Queueable queueable) {
        queue.putIntoQueue(queueable);
    }
}
