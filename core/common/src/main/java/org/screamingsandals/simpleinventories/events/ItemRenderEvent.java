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

import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.simpleinventories.builder.AnimationBuilder;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.utils.ConsumerExecutor;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.function.Consumer;

@Data
@RequiredArgsConstructor
public class ItemRenderEvent implements SEvent {
    private final PlayerItemInfo item;

    public InventorySet getFormat() {
        return item.getFormat();
    }

    public PlayerItemInfo getInfo() {
        return item;
    }

    public GenericItemInfo getOriginalInfo() {
        return item.getOriginal();
    }

    public Item getStack() {
        return item.getStack();
    }

    public void setStack(Item stack) {
        item.setStack(stack);
    }

    public boolean isVisible() {
        return item.isVisible();
    }

    public void setVisible(boolean visible) {
        item.setVisible(visible);
    }

    public boolean isDisabled() {
        return item.isDisabled();
    }

    public void setDisabled(boolean disabled) {
        item.setDisabled(disabled);
    }

    public ItemRenderEvent disabled(boolean disabled) {
        setDisabled(disabled);
        return this;
    }

    public ItemRenderEvent visible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public AnimationBuilder getAnimation() {
        return AnimationBuilder.of(item.getAnimation());
    }

    public ItemRenderEvent animation(Consumer<AnimationBuilder> consumer) {
        ConsumerExecutor.execute(consumer, getAnimation());
        return this;
    }

    public ItemRenderEvent stack(Consumer<ItemBuilder> consumer) {
        var builder = item.getStack().builder();
        ConsumerExecutor.execute(consumer, builder);
        item.setStack(builder.build().orElse(ItemFactory.getAir()));
        return this;
    }

    public ItemRenderEvent clearStack(Consumer<ItemBuilder> consumer) {
        var builder = ItemFactory.builder();
        ConsumerExecutor.execute(consumer, builder);
        item.setStack(builder.build().orElse(ItemFactory.getAir()));
        return this;
    }

    public String process(String raw) {
        return item.getFormat().processPlaceholders(item.getPlayer(), raw, item);
    }

    public PlayerWrapper getPlayer() {
        return item.getPlayer();
    }

    public ItemRenderEvent player(Consumer<PlayerWrapper> consumer) {
        ConsumerExecutor.execute(consumer, getPlayer());
        return this;
    }
}
