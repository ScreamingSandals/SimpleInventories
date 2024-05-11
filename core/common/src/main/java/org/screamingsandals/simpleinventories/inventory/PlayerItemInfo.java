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

package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.Getter;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.simpleinventories.events.ItemRenderEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class PlayerItemInfo {
    private GenericItemInfo original;
    private ItemStack stack;
    private boolean visible;
    private boolean disabled;
    private final List<ItemStack> animation = new ArrayList<>();
    @Getter
    private Player player;

    public PlayerItemInfo(Player player, GenericItemInfo original) {
        this.player = player;
        this.original = original;
        this.stack = original.getItem() != null ? original.getItem().clone() : ItemStackFactory.getAir();
        this.visible = original.isVisible();
        this.disabled = original.isDisabled();
        original.getAnimation().stream().map(ItemStack::clone).forEach(animation::add);

        if (stack.getDisplayName() != null) {
            stack = stack.withDisplayName(original.getFormat().processPlaceholders(player, stack.getDisplayName(), this));
        }
        if (!stack.getLore().isEmpty()) {
            stack = stack.withItemLore(stack.getLore().stream().map(e -> original.getFormat().processPlaceholders(player, e, this)).collect(Collectors.toList()));
        }

        original.getEventManager().fireEvent(new ItemRenderEvent(this));
    }

    public String getId() {
        return original.getId();
    }

    public int getPosition() {
        return original.getPosition();
    }

    public List<Property> getProperties() {
        return original.getProperties();
    }

    public boolean hasId() {
        return original.hasId();
    }

    public boolean hasProperties() {
        return original.hasProperties();
    }

    public boolean hasAnimation() {
        return !animation.isEmpty();
    }

    public boolean hasChildInventory() {
        return original.hasChildInventory();
    }

    public SubInventory getChildInventory() {
        return original.getChildInventory();
    }

    public InventorySet getFormat() {
        return original.getFormat();
    }

    public SubInventory getParent() {
        return original.getParent();
    }

    public Stream<Property> getPropertiesByName(String name) {
        return original.getPropertiesByName(name);
    }

    public Optional<Property> getFirstPropertyByName(String name) {
        return original.getFirstPropertyByName(name);
    }
}
