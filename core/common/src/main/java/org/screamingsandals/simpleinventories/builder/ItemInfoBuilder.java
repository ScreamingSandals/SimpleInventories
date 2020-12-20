package org.screamingsandals.simpleinventories.builder;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.events.ItemRenderEvent;
import org.screamingsandals.simpleinventories.events.OnTradeEvent;
import org.screamingsandals.simpleinventories.events.PostClickEvent;
import org.screamingsandals.simpleinventories.events.PreClickEvent;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.simpleinventories.material.builder.ItemBuilder;
import org.screamingsandals.simpleinventories.operations.OperationParser;
import org.screamingsandals.simpleinventories.operations.conditions.Condition;
import org.screamingsandals.simpleinventories.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
public class ItemInfoBuilder extends ParametrizedCategoryBuilder {
    private final GenericItemInfo itemInfo;

    public static final Pattern PRICE_PATTERN = Pattern.compile("(?<price>\\d+)(\\s+of\\s+|\\s+)(?<currency>[a-zA-Z0-9]+)?");

    @Override
    public LocalOptionsBuilder getCategoryOptions() {
        if (!itemInfo.hasChildInventory()) {
            itemInfo.setChildInventory(new SubInventory(false, itemInfo, itemInfo.getFormat()));
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
        var matcher = PRICE_PATTERN.matcher(price);
        while (matcher.find()) {
            var price1 = new Price();
            price1.setAmount(Integer.parseInt(matcher.group("price")));
            price1.setCurrency(matcher.group("currency"));
            itemInfo.getPrices().add(price1);
        }
        return this;
    }

    public ItemInfoBuilder price(Price price) {
        itemInfo.getPrices().add(price);
        return this;
    }

    public ItemInfoBuilder price(List<Object> prices) {
        prices.forEach(o -> {
            if (o instanceof Price) {
                price((Price) o);
            } else {
                price(o.toString());
            }
        });
        return this;
    }

    @Deprecated
    public ItemInfoBuilder priceType(String priceType) {
        itemInfo.setDefaultCurrency(priceType);
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
        itemInfo.getRequestedPosition().setAbsolute(() -> absolute);
        return this;
    }

    public ItemInfoBuilder clone(String link) {
        if (itemInfo.getRequestedClone() == null) {
            itemInfo.setRequestedClone(new Clone());
        }
        itemInfo.getRequestedClone().setCloneLink(link);
        return this;
    }

    public ItemInfoBuilder cloneMethod(String cloneMethod) {
        try {
            cloneMethod(CloneMethod.valueOf(cloneMethod.toUpperCase()));
        } catch (IllegalArgumentException ignored) {}
        return this;
    }

    public ItemInfoBuilder cloneMethod(CloneMethod cloneMethod) {
        if (itemInfo.getRequestedClone() == null) {
            itemInfo.setRequestedClone(new Clone());
        }
        itemInfo.getRequestedClone().setCloneMethod(cloneMethod);
        return this;
    }

    public ItemInfoBuilder column(int number) {
        itemInfo.getRequestedPosition().setColumn(integer -> number);
        return this;
    }

    public ItemInfoBuilder column(String colName) {
        try {
            column(Column.valueOf(colName.toUpperCase()));
        } catch (IllegalArgumentException ignored) {}
        return this;
    }

    public ItemInfoBuilder column(Column column) {
        itemInfo.getRequestedPosition().setColumn(column::convert);
        return this;
    }

    public ItemInfoBuilder linebreak(String linebreak) {
        try {
            linebreak(BreakType.valueOf(linebreak.toUpperCase()));
        } catch (IllegalArgumentException ignored) {}
        return this;
    }

    public ItemInfoBuilder linebreak(BreakType linebreak) {
        itemInfo.getRequestedPosition().setLinebreak(() -> linebreak);
        return this;
    }

    public ItemInfoBuilder pagebreak(String pagebreak) {
        try {
            pagebreak(BreakType.valueOf(pagebreak.toUpperCase()));
        } catch (IllegalArgumentException ignored) {}
        return this;
    }

    public ItemInfoBuilder pagebreak(BreakType pagebreak) {
        itemInfo.getRequestedPosition().setPagebreak(() -> pagebreak);
        return this;
    }

    public ItemInfoBuilder row(int row) {
        itemInfo.getRequestedPosition().setRow(() -> row);
        return this;
    }

    public ItemInfoBuilder skip(int skip) {
        itemInfo.getRequestedPosition().setSkip(() -> skip);
        return this;
    }

    public ItemInfoBuilder times(int times) {
        if (itemInfo.getRequestedTimes() == null) {
            itemInfo.setRequestedTimes(new Times());
        }
        itemInfo.getRequestedTimes().setRepeat(times);
        return this;
    }

    public ItemInfoBuilder timesMethods(String method) {
        try {
            timesMethods(TimesFlags.valueOf(method.toUpperCase()));
        } catch (IllegalArgumentException ignored) {}
        return this;
    }

    public ItemInfoBuilder timesMethods(TimesFlags method) {
        if (itemInfo.getRequestedTimes() == null) {
            itemInfo.setRequestedTimes(new Times());
        }
        itemInfo.getRequestedTimes().getFlags().add(method);
        return this;
    }

    public ItemInfoBuilder timesMethods(List<Object> methods) {
        methods.forEach(o -> {
            if (o instanceof TimesFlags) {
                timesMethods((TimesFlags) o);
            } else {
                timesMethods(o.toString());
            }
        });
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

    @Override
    protected void putObjectToQueue(@NonNull Object object) {
        if (subInventory == null) {
            if (!itemInfo.hasChildInventory()) {
                itemInfo.setChildInventory(new SubInventory(false, itemInfo, itemInfo.getFormat()));
            }
            subInventory = itemInfo.getChildInventory();
        }
        super.putObjectToQueue(object);
    }
}