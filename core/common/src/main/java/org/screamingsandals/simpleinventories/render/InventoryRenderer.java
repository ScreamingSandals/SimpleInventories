package org.screamingsandals.simpleinventories.render;

import lombok.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.screamingsandals.simpleinventories.events.SubInventoryOpenEvent;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.simpleinventories.tasks.RepeatingTask;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class InventoryRenderer {
    protected final PlayerWrapper player;
    protected SubInventory subInventory;
    protected int page;

    protected final Map<Integer, PlayerItemInfo> itemInfoMap = new HashMap<>();
    protected final Map<Integer, Item> itemStacksInInventory = new HashMap<>();
    protected final Map<Integer, List<Item>> animations = new HashMap<>();
    protected RepeatingTask animator;
    protected int nextAnimationPosition = 0;
    protected boolean mainEventCalled = false;

    public InventoryRenderer(PlayerWrapper player, SubInventory subInventory, int page) {
        this.player = player;
        this.subInventory = subInventory;
        this.page = page;
    }

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

    public boolean jump(InventorySet inventorySet) {
        if (inventorySet == null) {
            return false;
        }
        this.subInventory = inventorySet.getMainSubInventory();
        this.mainEventCalled = false;
        render();
        return true;
    }

    public boolean jump(InventorySet inventorySet, int page) {
        if (inventorySet == null) {
            return false;
        }
        this.subInventory = inventorySet.getMainSubInventory();
        this.mainEventCalled = false;
        this.page = 0;
        if (subInventory.getHighestPage() > page && page >= 0) {
            this.page = page;
        }
        render();
        return true;
    }

    public boolean jump(InventoryLink inventoryLink) {
        if (inventoryLink == null) {
            return false;
        }
        var link = inventoryLink.resolve();
        if (link.isEmpty()) {
            return false;
        }
        return jump(link.get());
    }

    public void render() {
        clear();

        if (!mainEventCalled) {
            var openInventoryEvent = new SubInventoryOpenEvent(player, subInventory, page);
            subInventory.getInventorySet().getEventManager().fireEvent(openInventoryEvent);

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
        player.closeInventory();
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

    protected Component getTitle() {
        var options = subInventory.getLocalOptions();
        var prefix = options.getPrefix();
        if (options.isShowPageNumber()) {
            prefix = prefix
                    .append(Component.text(" - "))
                    .append(Component.text(page + 1));
        }
        return prefix;
    }

    protected void generateNewData() {
        var options = subInventory.getLocalOptions();
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
                if (subInventory.getInventorySet().isAnimationsEnabled() && playerItemInfo.hasAnimation()) {
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
        var options = subInventory.getLocalOptions();
        return item.isWritten() && page == item.getPosition() / options.getItemsOnPage();
    }

    protected abstract void renderOnPlatform();

    public abstract boolean isOpened();
}
