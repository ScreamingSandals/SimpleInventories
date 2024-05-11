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

package org.screamingsandals.simpleinventories.action;

import org.screamingsandals.simpleinventories.events.SubInventoryCloseEvent;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;

public abstract class CloseInventoryActionHandler {
    protected boolean handleAction(InventoryRenderer inventoryRenderer) {
        var player = inventoryRenderer.getPlayer();
        var subInventory = inventoryRenderer.getSubInventory();
        var page = inventoryRenderer.getPage();

        var closeEvent = new SubInventoryCloseEvent(player, subInventory, page);
        subInventory.getInventorySet().getEventManager().fireEvent(closeEvent);

        if (closeEvent.cancelled()) {
            inventoryRenderer.jump(subInventory, page);
            return false;
        }
        return true;
    }
}
