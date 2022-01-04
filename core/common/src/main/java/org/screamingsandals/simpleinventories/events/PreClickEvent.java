/*
 * Copyright 2022 ScreamingSandals
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
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
@RequiredArgsConstructor
public class PreClickEvent implements SCancellableEvent {
    private final PlayerWrapper player;
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
}
