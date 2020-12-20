package org.screamingsandals.simpleinventories.builder;

import lombok.NonNull;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.utils.ConsumerExecutor;

import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public abstract class AbstractSubInventoryBuilder {

    private final Pattern REPEAT_MATCH_PATTERN = Pattern.compile("^(?<remain>.*)\\s+repeat\\s+(?<times>\\d+)$");
    private final Pattern FOR_PRICE_MATCH_PATTERN = Pattern.compile("(?<remain>.*)\\s+for\\s+(?<price>[^\"]*)$");

    public AbstractSubInventoryBuilder category(Object material) {
        item(material);
        return this;
    }

    public AbstractSubInventoryBuilder category(Object material, Consumer<ItemInfoBuilder> consumer) {
        item(material, consumer);
        return this;
    }

    public AbstractSubInventoryBuilder item(Object material) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        return this;
    }

    public AbstractSubInventoryBuilder item(Object material, Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = build(material);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, new ItemInfoBuilder(itemInfo));
        return this;
    }

    public AbstractSubInventoryBuilder cosmetic() {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink("cosmetic");
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        return this;
    }

    public AbstractSubInventoryBuilder cosmetic(Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink("cosmetic");
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, new ItemInfoBuilder(itemInfo));
        return this;
    }

    public AbstractSubInventoryBuilder itemClone(String link) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink(link);
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        return this;
    }

    public AbstractSubInventoryBuilder itemClone(String link, Consumer<ItemInfoBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        var clone = new Clone();
        clone.setCloneLink(link);
        itemInfo.setRequestedClone(clone);
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, new ItemInfoBuilder(itemInfo));
        return this;
    }

    public AbstractSubInventoryBuilder include(String include) {
        putObjectToQueue(new Include(include));
        return this;
    }

    public AbstractSubInventoryBuilder hidden(String id, Consumer<CategoryBuilder> consumer) {
        var itemInfo = new GenericItemInfo(getFormat());
        itemInfo.setWritten(false);
        itemInfo.setId(id);
        itemInfo.setChildInventory(new SubInventory(false, itemInfo, getFormat()));
        putObjectToQueue(itemInfo);
        ConsumerExecutor.execute(consumer, new CategoryBuilder(itemInfo.getChildInventory()));
        return this;
    }

    public AbstractSubInventoryBuilder hidden(String id) {
        var itemInfo = new GenericItemInfo(getFormat());
        itemInfo.setWritten(false);
        itemInfo.setId(id);
        putObjectToQueue(itemInfo);
        return this;
    }

    public AbstractSubInventoryBuilder insert(String link, Consumer<CategoryBuilder> consumer) {
        var insert = new Insert(link, new SubInventory(false, null, getFormat()));
        putObjectToQueue(insert);
        ConsumerExecutor.execute(consumer, new CategoryBuilder(insert.getSubInventory()));
        return this;
    }

    public AbstractSubInventoryBuilder insert(List<String> links, Consumer<CategoryBuilder> consumer) {
        links.forEach(s -> insert(s, consumer));
        return this;
    }

    protected abstract Inventory getFormat();
    protected abstract void putObjectToQueue(@NonNull Object object);

    protected GenericItemInfo build(Object stack) {
        if (stack instanceof GenericItemInfo) {
            return (GenericItemInfo) stack;
        } else if (stack instanceof String) {
            var itemInfo = new GenericItemInfo(getFormat());

            var enhancedShortStack = stack.toString();

            var matcher_repeat = REPEAT_MATCH_PATTERN.matcher(enhancedShortStack);

            if (matcher_repeat.find()) {
                var times = new Times();
                times.setRepeat(Integer.parseInt(matcher_repeat.group("times")));
                itemInfo.setRequestedTimes(times);
                enhancedShortStack = matcher_repeat.group("remain");
            }

            var matcher_price = FOR_PRICE_MATCH_PATTERN.matcher(enhancedShortStack);

            if (matcher_price.find()) {
                var matcher_one_price = ItemInfoBuilder.PRICE_PATTERN.matcher(matcher_price.group("price"));
                while (matcher_one_price.find()) {
                    var price1 = new Price();
                    price1.setAmount(Integer.parseInt(matcher_one_price.group("price")));
                    price1.setCurrency(matcher_one_price.group("currency"));
                    itemInfo.getPrices().add(price1);
                }
                enhancedShortStack = matcher_price.group("remain");
            }

            itemInfo.setItem(ItemFactory.build(stack).orElse(ItemFactory.getAir()));

            return itemInfo;
        } else {
            var itemInfo = new GenericItemInfo(getFormat());
            itemInfo.setItem(ItemFactory.build(stack).orElse(ItemFactory.getAir()));
            return itemInfo;
        }
    }

    /**
     * Groovy only
     *
     * @param consumer Consumer that consumes AbstractSubInventoryBuilder
     */
    public void call(Consumer<AbstractSubInventoryBuilder> consumer) {
        ConsumerExecutor.execute(consumer, this);
    }
}
