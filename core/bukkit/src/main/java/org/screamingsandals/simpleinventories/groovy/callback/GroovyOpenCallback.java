package org.screamingsandals.simpleinventories.groovy.callback;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import org.screamingsandals.simpleinventories.events.OpenInventoryEvent;
import org.screamingsandals.simpleinventories.inventory.OpenCallback;

import static org.screamingsandals.simpleinventories.groovy.utils.GroovyUtils.internalCallClosure;

@AllArgsConstructor
public class GroovyOpenCallback implements OpenCallback {
    private final Closure<OpenInventoryEvent> closure;

    @Override
    public void open(OpenInventoryEvent event) {
        internalCallClosure(closure, event);
    }
}
