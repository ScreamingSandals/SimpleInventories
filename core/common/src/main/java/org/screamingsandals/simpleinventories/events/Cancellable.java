package org.screamingsandals.simpleinventories.events;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
