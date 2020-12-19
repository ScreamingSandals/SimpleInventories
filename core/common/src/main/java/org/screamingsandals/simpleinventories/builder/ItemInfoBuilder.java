package org.screamingsandals.simpleinventories.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.events.ItemRenderEvent;
import org.screamingsandals.simpleinventories.events.OnTradeEvent;
import org.screamingsandals.simpleinventories.events.PostClickEvent;
import org.screamingsandals.simpleinventories.events.PreClickEvent;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.simpleinventories.material.builder.ItemBuilder;
import org.screamingsandals.simpleinventories.operations.OperationParser;
import org.screamingsandals.simpleinventories.operations.conditions.Condition;
import org.screamingsandals.simpleinventories.utils.ConsumerExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public class ItemInfoBuilder extends ParametrizedCategoryBuilder {
    private final GenericItemInfo itemInfo;

    @Override
    public LocalOptionsBuilder getCategoryOptions() {
        if (!itemInfo.hasChildInventory()) {
            itemInfo.setChildInventory(new SubInventory(false, itemInfo));
        }
        return new LocalOptionsBuilder(itemInfo.getChildInventory().getLocalOptions());
    }

    public ItemInfoBuilder stack(Consumer<ItemBuilder> consumer) {
        ConsumerExecutor.execute(consumer, getStack());
        return this;
    }

    public ItemBuilder getStack() {
        return new ItemBuilder(itemInfo.getItem());
    }

    public ItemInfoBuilder price(String price) {

        return this;
    }

    public ItemInfoBuilder priceType(String priceType) {

        return this;
    }

    public ItemInfoBuilder property(String name) {
        itemInfo.getProperties().add(new Property(itemInfo.getFormat(), name, null));
        return this;
    }

    public ItemInfoBuilder property(String name, Map<String, Object> map) {
        itemInfo.getProperties().add(new Property(itemInfo.getFormat(), name, map));
        return this;
    }

    public ItemInfoBuilder property(Map<String, Object> map) {
        itemInfo.getProperties().add(new Property(itemInfo.getFormat(), map.containsKey("name") ? map.get("name").toString() : null, map));
        return this;
    }

    public ItemInfoBuilder disabled(boolean disabled) {
        itemInfo.setDisabled(disabled);
        return this;
    }

    public ItemInfoBuilder disabled(String condition) {
        Condition condition_obj = OperationParser.getFinalCondition(itemInfo.getFormat(), condition);
        itemInfo.getEventManager().register(ItemRenderEvent.class, itemRenderEvent ->
                itemRenderEvent.getItem().setDisabled(condition_obj.process(itemRenderEvent.getPlayer(), itemRenderEvent.getItem()))
        );
        return this;
    }

    public ItemInfoBuilder disabled(Predicate<PlayerItemInfo> predicate) {
        itemInfo.getEventManager().register(ItemRenderEvent.class, itemRenderEvent ->
                itemRenderEvent.getItem().setDisabled(ConsumerExecutor.execute(predicate, itemRenderEvent.getItem()))
        );
        return this;
    }

    public ItemInfoBuilder id(String id) {
        itemInfo.setId(id);
        return this;
    }

    public ItemInfoBuilder absolute(int absolute) {

        return this;
    }

    public ItemInfoBuilder clone(String link) {

        return this;
    }

    public ItemInfoBuilder cloneMethod(String cloneMethod) {

        return this;
    }

    public ItemInfoBuilder column(int number) {

        return this;
    }

    public ItemInfoBuilder column(String colName) {

        return this;
    }

    public ItemInfoBuilder linebreak(String linebreak) {

        return this;
    }

    public ItemInfoBuilder pagebreak(String pagebreak) {

        return this;
    }

    public ItemInfoBuilder row(int row) {

        return this;
    }

    public ItemInfoBuilder skip(int skip) {

        return this;
    }

    public ItemInfoBuilder times(int times) {

        return this;
    }

    public ItemInfoBuilder timesMethods(String method) {

        return this;
    }

    public ItemInfoBuilder timesMethods(List<String> methods) {

        return this;
    }

    public ItemInfoBuilder visible(boolean visible) {
        itemInfo.setVisible(visible);
        return this;
    }

    public ItemInfoBuilder visible(String condition) {
        Condition condition_obj = OperationParser.getFinalCondition(itemInfo.getFormat(), condition);
        itemInfo.getEventManager().register(ItemRenderEvent.class, itemRenderEvent ->
            itemRenderEvent.getItem().setVisible(condition_obj.process(itemRenderEvent.getPlayer(), itemRenderEvent.getItem()))
        );
        return this;
    }

    public ItemInfoBuilder visible(Predicate<PlayerItemInfo> predicate) {
        itemInfo.getEventManager().register(ItemRenderEvent.class, itemRenderEvent ->
                itemRenderEvent.getItem().setVisible(ConsumerExecutor.execute(predicate, itemRenderEvent.getItem()))
        );
        return this;
    }

    public ItemInfoBuilder write(boolean write) {
        itemInfo.setWritten(write);
        return this;
    }

    public AnimationBuilder getAnimation() {
        return new AnimationBuilder(itemInfo.getAnimation());
    }

    public ItemInfoBuilder animation(Consumer<AnimationBuilder> consumer) {
        ConsumerExecutor.execute(consumer, getAnimation());
        return this;
    }

    public ItemInfoBuilder execute(String command) {
        itemInfo.getExecutions().add(command);
        return this;
    }

    public ItemInfoBuilder execute(List<String> commands) {
        itemInfo.getExecutions().addAll(commands);
        return this;
    }

    public ItemInfoBuilder locate(String locate) {
        itemInfo.setLocate(locate);
        return this;
    }

    public ItemInfoBuilder render(Consumer<ItemRenderEvent> consumer) {
        itemInfo.getEventManager().register(ItemRenderEvent.class, consumer);
        return this;
    }

    public ItemInfoBuilder preClick(Consumer<PreClickEvent> consumer) {
        itemInfo.getEventManager().register(PreClickEvent.class, consumer);
        return this;
    }

    public ItemInfoBuilder click(Consumer<PostClickEvent> consumer) {
        itemInfo.getEventManager().register(PostClickEvent.class, consumer);
        return this;
    }

    public ItemInfoBuilder buy(Consumer<OnTradeEvent> consumer) {
        itemInfo.getEventManager().register(OnTradeEvent.class, consumer);
        return this;
    }

    /**
     * BED WARS ONLY!!!
     *
     * @param map Map with upgrades
     * @return itself
     */
    public ItemInfoBuilder upgrade(Map<String, Object> map) {
        if (!itemInfo.hasData()) {
            itemInfo.setData(new HashMap<>());
        }
        var itemMap = itemInfo.getData();

        if (!itemMap.containsKey("upgrade")) {
            itemMap.put("upgrade", new HashMap<>());
        }
        var upgrades = (Map<String, Object>) itemMap.get("upgrade");
        if (!upgrades.containsKey("entities")) {
            upgrades.put("entities", new ArrayList<>());
        }

        var list = (List<Object>) upgrades.get("entities");
        if (!list.contains(map)) {
            list.add(map);
        }
        return this;
    }
}