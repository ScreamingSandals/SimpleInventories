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

package org.screamingsandals.simpleinventories.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

@Getter
@NoArgsConstructor(staticName = "of")
public final class SimpleItemQueue implements SubInventoryLike<SimpleItemQueue> {
    private final Queue<Queueable> queue = new LinkedList<>();

    @Override
    public SimpleItemQueue putIntoQueue(Queueable... queueableItems) {
        queue.addAll(Arrays.asList(queueableItems));
        return this;
    }
}
