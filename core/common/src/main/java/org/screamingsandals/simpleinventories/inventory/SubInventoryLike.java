package org.screamingsandals.simpleinventories.inventory;

import java.util.LinkedList;
import java.util.Queue;

public interface SubInventoryLike<T extends SubInventoryLike<T>> {

    T putIntoQueue(Queueable... queueableItems);

    Queue<Queueable> getQueue();

    default Queue<Queueable> copyOfQueue() {
        return new LinkedList<>(getQueue());
    }
}
