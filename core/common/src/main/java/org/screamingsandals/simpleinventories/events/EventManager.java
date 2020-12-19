package org.screamingsandals.simpleinventories.events;

import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.utils.ConsumerExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class EventManager {
    private final EventManager parent;
    private final Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();

    public <T> void register(Class<T> event, Consumer<T> handler) {
        if (!handlers.containsKey(event)) {
            handlers.put(event, new ArrayList<>());
        }
        handlers.get(event).add(handler);
    }

    public <T> void unregister(Consumer<T> handler) {
        handlers.forEach((aClass, consumers) -> consumers.removeIf(e -> handler == e));
    }

    public void fireEvent(Object event) {
        handlers.entrySet().stream()
                .filter(classListEntry -> classListEntry.getKey().isInstance(event))
                .forEach(classListEntry -> classListEntry.getValue().forEach(consumer -> {
                    try {
                        //noinspection unchecked
                        ConsumerExecutor.execute((Consumer<Object>) consumer, event);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }));

        if (parent != null) {
            parent.fireEvent(event);
        }
    }
}
