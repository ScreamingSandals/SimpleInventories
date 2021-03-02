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
