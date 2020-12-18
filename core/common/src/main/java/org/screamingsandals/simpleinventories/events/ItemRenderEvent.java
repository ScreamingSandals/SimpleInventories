package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class ItemRenderEvent {
    private final Inventory format;
    private final PlayerItemInfo item;
    private final PlayerWrapper player;

    /*
    @Deprecated
    public ItemInfo getOriginalInfo() {
        return info.getOriginal();
    }

    public Item getStack() {
        return info.getStack();
    }

    public void setStack(Item stack) {
        info.setStack(stack);
    }

    public boolean isVisible() {
        return info.isVisible();
    }

    public void setVisible(boolean visible) {
        info.setVisible(visible);
    }

    public boolean isDisabled() {
        return info.isDisabled();
    }

    public void setDisabled(boolean disabled) {
        info.setDisabled(disabled);
    }*/
}
