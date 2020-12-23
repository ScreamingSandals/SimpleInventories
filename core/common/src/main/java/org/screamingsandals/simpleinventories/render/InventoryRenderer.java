package org.screamingsandals.simpleinventories.render;

import lombok.*;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.events.SubInventoryOpenEvent;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.tasks.RepeatingTask;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class InventoryRenderer {
    protected final PlayerWrapper player;
    @NonNull
    protected SubInventory subInventory;
    @NonNull
    protected int page;

    protected final Map<Integer, PlayerItemInfo> itemInfoMap = new HashMap<>();
    protected final Map<Integer, Item> itemStacksInInventory = new HashMap<>();
    protected final Map<Integer, List<Item>> animations = new HashMap<>();
    protected RepeatingTask animator;
    protected int nextAnimationPosition = 0;
    protected boolean mainEventCalled = false;

    public boolean nextPage() {
        if (subInventory.getHighestPage() > page) {
            page++;
            this.mainEventCalled = false;
            render();
            return true;
        }
        return false;
    }

    public boolean previousPage() {
        if (page > 0) {
            page--;
            this.mainEventCalled = false;
            render();
            return true;
        }
        return false;
    }

    public boolean jumpToPage(int page) {
        if (subInventory.getHighestPage() > page && page >= 0) {
            this.page = page;
            this.mainEventCalled = false;
            render();
            return true;
        }
        return false;
    }

    public boolean jump(SubInventory subInventory) {
        if (subInventory == null) {
            return false;
        }
        this.subInventory = subInventory;
        this.mainEventCalled = false;
        render();
        return true;
    }

    public boolean jump(SubInventory subInventory, int page) {
        if (subInventory == null) {
            return false;
        }
        this.subInventory = subInventory;
        this.mainEventCalled = false;
        this.page = 0;
        if (subInventory.getHighestPage() > page && page >= 0) {
            this.page = page;
        }
        render();
        return true;
    }

    public boolean jump(Inventory inventory) {
        if (inventory == null) {
            return false;
        }
        this.subInventory = inventory.getMainSubInventory();
        this.mainEventCalled = false;
        render();
        return true;
    }

    public boolean jump(Inventory inventory, int page) {
        if (inventory == null) {
            return false;
        }
        this.subInventory = inventory.getMainSubInventory();
        this.mainEventCalled = false;
        this.page = 0;
        if (subInventory.getHighestPage() > page && page >= 0) {
            this.page = page;
        }
        render();
        return true;
    }

    public void render() {
        clear();

        if (!mainEventCalled) {
            var openInventoryEvent = new SubInventoryOpenEvent(player, subInventory, page);
            subInventory.getFormat().getEventManager().fireEvent(openInventoryEvent);

            if (openInventoryEvent.isCancelled()) {
                close();
                return;
            }
            mainEventCalled = true;
        }

        generateNewData();
        renderOnPlatform();

        if (animator != null) {
            animator.start();
        }
    }

    public void close() {
        clear();
        SimpleInventoriesCore.closeInventory(player);
    }

    protected void clear() {
        if (animator != null) {
            animator.cancel();
        }
        nextAnimationPosition = 0;
        itemInfoMap.clear();
        itemStacksInInventory.clear();
        animations.clear();
    }

    protected String getTitle() {
        var options = subInventory.getLocalOrParentOptions();
        var prefix = options.getPrefix();
        if (options.isShowPageNumber()) {
            prefix += "&r - " + (page + 1);
        }
        return prefix;
    }

    protected void generateNewData() {
        var options = subInventory.getLocalOrParentOptions();
        var size = options.getRenderActualRows() * options.getItemsOnRow();

        for (var i = 0; i < options.getItemsOnRow(); i++) {
            safePutStackToInventory(size, options.getRenderHeaderStart() + i, options.getCosmeticItem().clone());
        }

        for (var i = 0; i < options.getItemsOnRow(); i++) {
            safePutStackToInventory(size, options.getRenderFooterStart() + i, options.getCosmeticItem().clone());
        }

        if (!subInventory.isMain()) {
            safePutStackToInventory(size, options.getRenderHeaderStart(), options.getBackItem().clone());
        }

        if (page > 0) {
            safePutStackToInventory(size, options.getRenderFooterStart(), options.getPageBackItem().clone());
        }

        if (subInventory.getHighestPage() > page) {
            safePutStackToInventory(size, options.getRenderFooterStart() + options.getItemsOnRow() - 1, options.getPageForwardItem().clone());
        }

        subInventory.getContents().stream().filter(this::isOnThisPage).forEach(itemInfo -> {
            var playerItemInfo = new PlayerItemInfo(player, itemInfo);

            var pos = (itemInfo.getPosition() % options.getItemsOnPage()) + options.getRenderOffset();
            if (playerItemInfo.isVisible()) {
                safePutStackToInventory(size, pos, playerItemInfo.getStack());
                if (subInventory.getFormat().isAnimationsEnabled() && playerItemInfo.hasAnimation()) {
                    animations.put(pos, List.copyOf(playerItemInfo.getAnimation()));
                }
            }
            itemInfoMap.put(pos, playerItemInfo);
        });
    }

    protected void safePutStackToInventory(int max, int position, Item stack) {
        if (position >= 0 && position < max) {
            itemStacksInInventory.put(position, stack);
        }
    }

    protected boolean isOnThisPage(GenericItemInfo item) {
        var options = subInventory.getLocalOrParentOptions();
        return page == item.getPosition() / options.getItemsOnPage();
    }

    protected abstract void renderOnPlatform();

    public abstract boolean isOpened();
}
