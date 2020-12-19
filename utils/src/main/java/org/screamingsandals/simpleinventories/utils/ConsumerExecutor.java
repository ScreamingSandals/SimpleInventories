package org.screamingsandals.simpleinventories.utils;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConsumerExecutor {
    public static <R> R execute(Consumer<R> consumer, R object) {
        try {
            // TODO: test that
            consumer.getClass().getMethod("setDelegate", Object.class).invoke(consumer, object);
            consumer.getClass().getMethod("setResolveStrategy", int.class).invoke(consumer, 3);
        } catch (Throwable ignored) {}

        consumer.accept(object);

        return object;
    }
    public static <R> boolean execute(Predicate<R> predicate, R object) {
        try {
            // TODO: test that
            predicate.getClass().getMethod("setDelegate", Object.class).invoke(predicate, object);
            predicate.getClass().getMethod("setResolveStrategy", int.class).invoke(predicate, 3);
        } catch (Throwable ignored) {}

        return predicate.test(object);
    }
}
