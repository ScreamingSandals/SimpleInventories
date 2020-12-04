package org.screamingsandals.simpleinventories.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArgumentConverter<T> {

    private final Map<Class<?>, Function<Object, T>> converters = new HashMap<>();
    private boolean finished = false;

    @NotNull
    public ArgumentConverter<T> finish() {
        finished = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <A> ArgumentConverter<T> register(@NotNull Class<A> type, @NotNull Function<A, T> convertor) {
        if (finished) {
            throw new UnsupportedOperationException("Convertor has been already fully initialized!");
        }
        converters.put(type, (Function<Object, T>) convertor);
        return this;
    }

    @NotNull
    public static <T> ArgumentConverter<T> build() {
        return new ArgumentConverter<>();
    }


    @SuppressWarnings("unchecked")
    @NotNull
    public <A> T convert(@NotNull A object) {
        Optional<Map.Entry<Class<?>, Function<Object, T>>> opt = converters.entrySet().stream().filter(c -> c.getKey().isInstance(object)).findFirst();
        if (!opt.isPresent()) {
            throw new UnsupportedOperationException("Can't convert this argument type");
        }

        return opt.get().getValue().apply(object);
    }

    @NotNull
    public <A> Optional<T> convertOptional(@Nullable A object) {
        if (object == null) {
            return Optional.empty();
        }
        Optional<Map.Entry<Class<?>, Function<Object, T>>> opt = converters.entrySet().stream().filter(c -> c.getKey().isInstance(object)).findFirst();
        return opt.map(classFunctionEntry -> classFunctionEntry.getValue().apply(object));
    }
}
