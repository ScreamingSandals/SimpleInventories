package org.screamingsandals.simpleinventories.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultConverter<T> {

    private final Map<Class<?>, Function<T, Object>> converters = new HashMap<>();
    private boolean finished = false;

    @NotNull
    public ResultConverter<T> finish() {
        finished = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <R> ResultConverter<T> register(@NotNull Class<R> type, @NotNull Function<T, R> convertor) {
        if (finished) {
            throw new UnsupportedOperationException("Convertor has been already fully initialized!");
        }
        converters.put(type, (Function<T, Object>) convertor);
        return this;
    }

    @NotNull
    public static <T> ResultConverter<T> build() {
        return new ResultConverter<>();
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <R> R convert(@NotNull T object, @NotNull Class<R> newType) {
        Optional<Map.Entry<Class<?>, Function<T, Object>>> opt = converters.entrySet().stream().filter(c -> newType.isAssignableFrom(c.getKey())).findFirst();
        if (!opt.isPresent()) {
            throw new UnsupportedOperationException("Can't convert object to this type!");
        }
        return (R) opt.get().getValue().apply(object);
    }

}
