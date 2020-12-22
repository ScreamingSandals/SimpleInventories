package org.screamingsandals.simpleinventories.groovy.callback;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import org.screamingsandals.simpleinventories.events.CloseInventoryEvent;
import org.screamingsandals.simpleinventories.inventory.CloseCallback;

import static org.screamingsandals.simpleinventories.groovy.utils.GroovyUtils.internalCallClosure;

@AllArgsConstructor
public class GroovyCloseCallback implements CloseCallback {
    private final Closure<CloseInventoryEvent> closure;

    @Override
    public void close(CloseInventoryEvent event) {
        internalCallClosure(closure, event);
    }
}
