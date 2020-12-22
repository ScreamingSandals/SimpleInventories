package org.screamingsandals.simpleinventories.utils;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor(staticName = "build")
public class BidirectionalConverter<W> {
    private final Map<Class<?>, Function<Object, W>> p2wConverters = new HashMap<>();
    private final Map<Class<?>, Function<W, Object>> w2pConverters = new HashMap<>();
    private boolean finished = false;

    public void finish() {
        finished = true;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> BidirectionalConverter<W> registerW2P(@NotNull Class<P> type, @NotNull Function<W, P> convertor) {
        if (finished) {
            throw new UnsupportedOperationException("Converter has been already fully initialized!");
        }
        w2pConverters.put(type, (Function<W, Object>) convertor);
        return this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> BidirectionalConverter<W> registerP2W(@NotNull Class<P> type, @NotNull Function<P, W> convertor) {
        if (finished) {
            throw new UnsupportedOperationException("Converter has been already fully initialized!");
        }
        p2wConverters.put(type, (Function<Object, W>) convertor);
        return this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> W convert(@NotNull P object) {
        var opt = p2wConverters.entrySet()
                .stream()
                .filter(c -> c.getKey().isInstance(object))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Can't convert object to the wrapper"));
        return opt.getValue().apply(object);
    }

    @NotNull
    public <P> Optional<W> convertOptional(@Nullable P object) {
        if (object == null) {
            return Optional.empty();
        }
        var opt = p2wConverters.entrySet()
                .stream()
                .filter(c -> c.getKey().isInstance(object))
                .findFirst();
        return opt.map(classFunctionEntry -> classFunctionEntry.getValue().apply(object));
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> P convert(@NotNull W object, @NotNull Class<P> newType) {
        var opt = w2pConverters.entrySet()
                .stream()
                .filter(c -> newType.isAssignableFrom(c.getKey()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Can't convert wrapper to " + newType.getName()));
        return (P) opt.getValue().apply(object);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <P> Optional<P> convertOptional(@Nullable W object, @NotNull Class<P> newType) {
        if (object == null) {
            return Optional.empty();
        }
        var opt = w2pConverters.entrySet()
                .stream()
                .filter(c -> newType.isAssignableFrom(c.getKey()))
                .findFirst();
        return opt.map(classFunctionEntry -> (P) classFunctionEntry.getValue().apply(object));
    }

}
