package org.screamingsandals.simpleinventories.item;

import org.screamingsandals.simpleinventories.events.PostActionEvent;

public interface PostClickCallback {
    void postClick(PostActionEvent event);
}
