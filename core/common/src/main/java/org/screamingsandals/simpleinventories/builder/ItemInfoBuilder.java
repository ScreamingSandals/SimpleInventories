package org.screamingsandals.simpleinventories.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.*;
import org.screamingsandals.simpleinventories.events.ItemRenderEvent;
import org.screamingsandals.simpleinventories.events.OnTradeEvent;
import org.screamingsandals.simpleinventories.events.PostClickEvent;
import org.screamingsandals.simpleinventories.events.PreClickEvent;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.lib.material.builder.ItemBuilder;
import org.screamingsandals.simpleinventories.operations.OperationParser;
import org.screamingsandals.simpleinventories.operations.conditions.Condition;
import org.screamingsandals.simpleinventories.utils.BreakType;
import org.screamingsandals.simpleinventories.utils.CloneMethod;
import org.screamingsandals.simpleinventories.utils.Column;
import org.screamingsandals.simpleinventories.utils.TimesFlags;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ItemInfoBuilder extends CategoryBuilder {
    private final GenericItemInfo itemInfo;

    public static final Pattern PRICE_PATTERN = Pattern.compile("(?<price>\\d+)(\\s+of\\s+|\\s+)(?<currency>[a-zA-Z0-9]+)?");

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
        itemInfo.getProperties().add(new Property(itemInfo.getFormat(), name));
        return this;
    }

    public ItemInfoBuilder property(String name, Map<String, Object> map) {
        try {
            return property(name, BasicConfigurationNode.root().set(map));
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ItemInfoBuilder property(String name, ConfigurationNode configurationNode) {
        itemInfo.getProperties().add(new Property(itemInfo.getFormat(), name, configurationNode));
        return this;
    }

    public ItemInfoBuilder property(Map<String, Object> map) {
        try {
            return property(BasicConfigurationNode.root().set(map));
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ItemInfoBuilder property(ConfigurationNode configurationNode) {
        return property(configurationNode.node("name").getString(), configurationNode);
    }

    public ItemInfoBuilder disabled(boolean disabled) {
        itemInfo.setDisabled(() -> disabled);
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
        itemInfo.setVisible(() -> visible);
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

    /**
     * Turns on or off the rendering for this item. Shouldn't be used, use hidden instead
     * 
     * @see CategoryBuilder#hidden(String)
     * @param write If item should be rendered
     * @return itself
     */
    @Deprecated
    public ItemInfoBuilder write(boolean write) {
        itemInfo.setWritten(() -> write);
        return this;
    }

    public AnimationBuilder getAnimation() {
        return AnimationBuilder.of(itemInfo.getAnimation());
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
        itemInfo.setLocate(InventoryLink.of(getFormat(), locate));
        return this;
    }

    public ItemInfoBuilder locate(InventorySet inventorySet) {
        itemInfo.setLocate(InventoryLink.of(inventorySet));
        return this;
    }

    public ItemInfoBuilder locate(SubInventory subInventory) {
        itemInfo.setLocate(InventoryLink.of(subInventory));
        return this;
    }

    public ItemInfoBuilder locate(InventorySet inventorySet, String locate) {
        itemInfo.setLocate(InventoryLink.of(inventorySet, locate));
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
        if (getFormat().getVariableToPropertyMap().containsKey("upgrade")) {
            var propertyName = getFormat().getVariableToPropertyMap().get("upgrade");
            var property = itemInfo.getProperties().stream()
                    .filter(property1 -> property1.getPropertyName().equals(propertyName))
                    .findFirst()
                    .orElseGet(() -> {
                        var newProperty = new Property(getFormat(), propertyName, BasicConfigurationNode.root());
                        itemInfo.getProperties().add(newProperty);
                        return newProperty;
                    });

            var upgrades = property.getPropertyData();
            var entities = upgrades.node("entities");
            try {
                entities.appendListNode().set(map);
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public SubInventory getSubInventory() {
        if (subInventory == null) {
            if (!itemInfo.hasChildInventory()) {
                itemInfo.setChildInventory(new SubInventory(false, itemInfo, itemInfo.getFormat()));
            }
            subInventory = itemInfo.getChildInventory();
        }
        return subInventory;
    }

    @Override
    protected InventorySet getFormat() {
        return itemInfo.getFormat();
    }



    /* TODO: Create annotation processor that will generate overrides of self-returning methods you can see below */

    @Override
    public ItemInfoBuilder categoryOptions(Consumer<LocalOptionsBuilder> consumer) {
        return (ItemInfoBuilder) super.categoryOptions(consumer);
    }

    @Override
    public ItemInfoBuilder category(Object material) {
        return (ItemInfoBuilder) super.category(material);
    }

    @Override
    public ItemInfoBuilder category(Object material, Consumer<ItemInfoBuilder> consumer) {
        return (ItemInfoBuilder) super.category(material, consumer);
    }

    @Override
    public ItemInfoBuilder item(Object material) {
        return (ItemInfoBuilder) super.item(material);
    }

    @Override
    public ItemInfoBuilder item(Object material, Consumer<ItemInfoBuilder> consumer) {
        return (ItemInfoBuilder) super.item(material, consumer);
    }

    @Override
    public ItemInfoBuilder cosmetic() {
        return (ItemInfoBuilder) super.cosmetic();
    }

    @Override
    public ItemInfoBuilder cosmetic(Consumer<ItemInfoBuilder> consumer) {
        return (ItemInfoBuilder) super.cosmetic(consumer);
    }

    @Override
    public ItemInfoBuilder itemClone(String link) {
        return (ItemInfoBuilder) super.itemClone(link);
    }

    @Override
    public ItemInfoBuilder itemClone(String link, Consumer<ItemInfoBuilder> consumer) {
        return (ItemInfoBuilder) super.itemClone(link, consumer);
    }

    @Override
    public ItemInfoBuilder include(String include) {
        return (ItemInfoBuilder) super.include(include);
    }

    @Override
    public ItemInfoBuilder include(Include include) {
        return (ItemInfoBuilder) super.include(include);
    }

    @Override
    public ItemInfoBuilder hidden(String id, Consumer<CategoryBuilder> consumer) {
        return (ItemInfoBuilder) super.hidden(id, consumer);
    }

    @Override
    public ItemInfoBuilder hidden(String id) {
        return (ItemInfoBuilder) super.hidden(id);
    }

    @Override
    public ItemInfoBuilder insert(String link, Consumer<QueueBuilder> consumer) {
        return (ItemInfoBuilder) super.insert(link, consumer);
    }

    @Override
    public ItemInfoBuilder insert(String link, SubInventoryLike<?> prebuiltInventory) {
        return (ItemInfoBuilder) super.insert(link, prebuiltInventory);
    }

    @Override
    public ItemInfoBuilder insert(List<String> links, Consumer<QueueBuilder> consumer) {
        return (ItemInfoBuilder) super.insert(links, consumer);
    }

    @Override
    public ItemInfoBuilder insert(List<String> links, SubInventoryLike<?> prebuiltInventory) {
        return (ItemInfoBuilder) super.insert(links, prebuiltInventory);
    }
}