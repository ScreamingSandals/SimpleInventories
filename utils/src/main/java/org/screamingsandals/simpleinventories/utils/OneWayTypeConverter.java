package org.screamingsandals.simpleinventories.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OneWayTypeConverter<T> {

    private final Map<Class<?>, Function<T, Object>> converters;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TypeConverterBuilder<T> {
        private final Map<Class<?>, Function<T, Object>> converters = new HashMap<>();

        public TypeConverterBuilder<T> convertor(Class<?> type, Function<T, Object> convertor) {
            converters.put(type, convertor);
            return this;
        }

        public OneWayTypeConverter<T> construct() {
            return new OneWayTypeConverter<>(new HashMap<>(converters));
        }
    }

    public static <T> TypeConverterBuilder<T> builder() {
        return new TypeConverterBuilder<>();
    }

    @SuppressWarnings("unchecked")
    public <R> R convert(T object, Class<R> newType) {
        if (!converters.containsKey(newType)) {
            throw new UnsupportedOperationException("Can't convert object to this type!");
        }
        return (R) converters.get(newType).apply(object);
    }

}
