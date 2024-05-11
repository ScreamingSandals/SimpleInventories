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

package org.screamingsandals.simpleinventories.builder;

import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
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
                var item = ItemStackFactory.build(enhancedShortStack);
                itemInfo.setItem(item != null ? item : ItemStackFactory.getAir());
            }

            return itemInfo;
        } else {
            var itemInfo = new GenericItemInfo(format);
            var item = ItemStackFactory.build(stack);
            itemInfo.setItem(item != null ? item : ItemStackFactory.getAir());
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
