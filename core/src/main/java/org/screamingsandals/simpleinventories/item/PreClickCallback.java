package org.screamingsandals.simpleinventories.item;

import org.screamingsandals.simpleinventories.events.PreActionEvent;

public interface PreClickCallback {
    void preClick(PreActionEvent event);
}
