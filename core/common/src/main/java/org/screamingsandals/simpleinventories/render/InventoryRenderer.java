/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.simpleinventories.render;

import lombok.*;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.tasker.task.Task;
import org.screamingsandals.simpleinventories.events.SubInventoryOpenEvent;
import org.screamingsandals.simpleinventories.inventory.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class InventoryRenderer {
    protected final Player player;
    protected SubInventory subInventory;
    protected int page;

    protected final Map<Integer, PlayerItemInfo> itemInfoMap = new HashMap<>();
    protected final Map<Integer, ItemStack> itemStacksInInventory = new HashMap<>();
    protected final Map<Integer, List<ItemStack>> animations = new HashMap<>();
    protected Task animator;
    protected int nextAnimationPosition = 0;
    protected boolean mainEventCalled = false;

    public InventoryRenderer(Player player, SubInventory subInventory, int page) {
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
                    .withAppendix(Component.text(" - "))
                    .withAppendix(Component.text(page + 1));
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

    protected void safePutStackToInventory(int max, int position, ItemStack stack) {
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
