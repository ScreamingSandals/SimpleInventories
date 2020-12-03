package org.screamingsandals.simpleinventories.utils;

import java.util.function.Consumer;

public class ConsumerExecutor {
    public static <R> void execute(Consumer<R> consumer, R object) {
        try {
            // TODO: test that
            consumer.getClass().getMethod("setDelegate", Object.class).invoke(consumer, object);
            consumer.getClass().getMethod("setResolveStrategy", int.class).invoke(consumer, 3);
        } catch (Throwable ignored) {}

        consumer.accept(object);
    }
}
