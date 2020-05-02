package org.screamingsandals.simpleinventories.groovy;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import org.screamingsandals.simpleinventories.events.PreActionEvent;
import org.screamingsandals.simpleinventories.item.PreClickCallback;

import static org.screamingsandals.simpleinventories.groovy.GroovyUtils.internalCallClosure;

@AllArgsConstructor
public class GroovyPreClickCallback implements PreClickCallback {
    private final Closure<PreActionEvent> closure;

    @Override
    public void preClick(PreActionEvent event) {
        internalCallClosure(closure, event);
    }
}
