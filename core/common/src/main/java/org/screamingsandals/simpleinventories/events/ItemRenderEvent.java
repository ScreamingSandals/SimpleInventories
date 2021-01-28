package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.simpleinventories.builder.AnimationBuilder;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemBuilder;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.utils.ConsumerExecutor;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class ItemRenderEvent extends AbstractEvent {
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
        item.setStack(ItemFactory.getAir());
        ConsumerExecutor.execute(consumer, new ItemBuilder(item.getStack()));
        return this;
    }

    public ItemRenderEvent clearStack(Consumer<ItemBuilder> consumer) {
        ConsumerExecutor.execute(consumer, new ItemBuilder(item.getStack()));
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
