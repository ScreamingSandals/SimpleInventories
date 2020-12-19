package org.screamingsandals.simpleinventories.builder;

import org.screamingsandals.simpleinventories.utils.ConsumerExecutor;

import java.util.function.Consumer;

public abstract class ParametrizedCategoryBuilder extends CategoryBuilder {

    public ParametrizedCategoryBuilder categoryOptions(Consumer<LocalOptionsBuilder> consumer) {
        ConsumerExecutor.execute(consumer, getCategoryOptions());
        return this;
    }

    public abstract LocalOptionsBuilder getCategoryOptions();
}
