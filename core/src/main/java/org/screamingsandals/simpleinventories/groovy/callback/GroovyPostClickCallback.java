package org.screamingsandals.simpleinventories.groovy.callback;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import org.screamingsandals.simpleinventories.events.PostActionEvent;
import org.screamingsandals.simpleinventories.item.PostClickCallback;

import static org.screamingsandals.simpleinventories.groovy.utils.GroovyUtils.internalCallClosure;

@AllArgsConstructor
public class GroovyPostClickCallback implements PostClickCallback {
    private final Closure<PostActionEvent> closure;

    @Override
    public void postClick(PostActionEvent event) {
        internalCallClosure(closure, event);
    }
}
