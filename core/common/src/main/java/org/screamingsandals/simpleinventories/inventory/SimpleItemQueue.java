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
