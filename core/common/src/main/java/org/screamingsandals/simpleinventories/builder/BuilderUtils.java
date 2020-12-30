package org.screamingsandals.simpleinventories.builder;

import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.placeholders.RuntimeDefinedPlaceholder;

import java.util.regex.Pattern;

public class BuilderUtils {

    private static final Pattern REPEAT_MATCH_PATTERN = Pattern.compile("^(?<remain>.*)\\s+repeat\\s+(?<times>\\d+)$");
    private static final Pattern FOR_PRICE_MATCH_PATTERN = Pattern.compile("(?<remain>.*)\\s+for\\s+(?<price>[^\"]*)$");

    public static GenericItemInfo buildItem(InventorySet format, Object stack) {
        if (stack instanceof GenericItemInfo) {
            return (GenericItemInfo) stack;
        } else if (stack instanceof String) {
            var itemInfo = new GenericItemInfo(format);

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

            if (enhancedShortStack.equalsIgnoreCase("cosmetic")) {
                var clone = new Clone();
                clone.setCloneLink("cosmetic");
                itemInfo.setRequestedClone(clone);
            } else {
                itemInfo.setItem(ItemFactory.build(enhancedShortStack).orElse(ItemFactory.getAir()));
            }

            return itemInfo;
        } else {
            var itemInfo = new GenericItemInfo(format);
            itemInfo.setItem(ItemFactory.build(stack).orElse(ItemFactory.getAir()));
            return itemInfo;
        }
    }

    public static void buildDefinition(InventorySet inventorySet, String definition) {
        var defsplit = definition.split(" as ", 2);
        var key = defsplit[0].trim();
        if (key.startsWith("%")) {
            key = key.substring(1);
        }
        if (key.endsWith("%")) {
            key = key.substring(0, key.length() - 1);
        }
        var placeholderFormat = key.split("(?<!\\.)\\.(?!\\.)", 2);
        key = placeholderFormat[0];
        var placeholderArguments = placeholderFormat.length == 1 ? "" : placeholderFormat[1];
        if (inventorySet.getPlaceholders().containsKey(key) && !(inventorySet.getPlaceholders().get(key) instanceof RuntimeDefinedPlaceholder)) {
            SimpleInventoriesCore.getLogger().severe("Placeholder " + key + " is already defined as non-dynamic placeholder!");
            return;
        }
        if (!inventorySet.getPlaceholders().containsKey(key)) {
            inventorySet.registerPlaceholder(key, new RuntimeDefinedPlaceholder());
        }
        var parser = (RuntimeDefinedPlaceholder) inventorySet.getPlaceholders().get(key);
        if (defsplit.length == 2) {
            if (!placeholderArguments.isEmpty()) {
                parser.register(placeholderArguments, defsplit[1].trim());
            } else {
                parser.putDefault(defsplit[1].trim());
            }
        }
    }
}
