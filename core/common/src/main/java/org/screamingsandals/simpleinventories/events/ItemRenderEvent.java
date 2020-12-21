package org.screamingsandals.simpleinventories.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.screamingsandals.simpleinventories.builder.AnimationBuilder;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.material.builder.ItemBuilder;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.utils.ConsumerExecutor;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.function.Consumer;

@Data
@RequiredArgsConstructor
public class ItemRenderEvent {
    private final Inventory format;
    private final PlayerItemInfo item;
    private final PlayerWrapper player;

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
        return new AnimationBuilder(item.getAnimation());
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
        return format.processPlaceholders(item.getPlayer(), raw, item);
    }

    public PlayerWrapper getPlayer() {
        return item.getPlayer();
    }

    public ItemRenderEvent player(Consumer<PlayerWrapper> consumer) {
        ConsumerExecutor.execute(consumer, getPlayer());
        return this;
    }
}
