package org.screamingsandals.simpleinventories.builder;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.Queueable;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.utils.ConsumerExecutor;

import java.util.function.Consumer;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBuilder extends AbstractSubInventoryBuilder {
    protected SubInventory subInventory;

    @Override
    protected Inventory getFormat() {
        return getSubInventory().getFormat();
    }

    @Override
    protected void putObjectToQueue(@NonNull Queueable queueable) {
        getSubInventory().getWaitingQueue().add(queueable);
    }

    public CategoryBuilder categoryOptions(Consumer<LocalOptionsBuilder> consumer) {
        ConsumerExecutor.execute(consumer, getCategoryOptions());
        return this;
    }

    public LocalOptionsBuilder getCategoryOptions() {
        return new LocalOptionsBuilder(getSubInventory().getLocalOptions());
    }
}
