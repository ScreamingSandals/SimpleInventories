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

package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.event.CancellableEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.simpleinventories.inventory.IdentifiableEntry;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

@Data
@RequiredArgsConstructor
public class PreClickEvent implements CancellableEvent {
    private final Player player;
    private final PlayerItemInfo item;
    private final ClickType clickType;
    private final SubInventory subInventory;
    private boolean cancelled;

    public boolean hasItem() {
        return item != null;
    }

    public InventorySet getFormat() {
        return item.getFormat();
    }

    public IdentifiableEntry getParent() {
        return subInventory.getItemOwner();
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public void cancelled(boolean cancel) {
        cancelled = cancel;
    }
}
