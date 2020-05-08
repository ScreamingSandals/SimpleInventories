package org.screamingsandals.simpleinventories.groovy.callback;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import org.screamingsandals.simpleinventories.events.ShopTransactionEvent;
import org.screamingsandals.simpleinventories.item.BuyCallback;

import static org.screamingsandals.simpleinventories.groovy.utils.GroovyUtils.internalCallClosure;

@AllArgsConstructor
public class GroovyBuyCallback implements BuyCallback {
    private final Closure<ShopTransactionEvent> closure;

    @Override
    public void buy(ShopTransactionEvent event) {
        internalCallClosure(closure, event);
    }
}
