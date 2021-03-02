package org.screamingsandals.simpleinventories.builder;

import lombok.*;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.lib.utils.ConsumerExecutor;

import java.util.function.Consumer;

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

    public CategoryBuilder categoryOptions(Consumer<LocalOptionsBuilder> consumer) {
        ConsumerExecutor.execute(consumer, getCategoryOptions());
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
