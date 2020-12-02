package org.screamingsandals.simpleinventories.material.builder;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.material.MaterialHolder;
import org.screamingsandals.simpleinventories.material.MaterialMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemFactory {

    private static final Pattern SHORT_STACK_PATTERN = Pattern.compile("^(?<material>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(\\\\*)?(;(?<amount>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)?(\\\\*)?(;(?<name>(\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+|(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+))?(\\\\*)?(;(?<lore>.*))?)?)?$");
    private static final Pattern LORE_SPLIT = Pattern.compile("((\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+\")|((?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(?=($|;))");

    public static Optional<Item> build(String shortStack) {
        shortStack = shortStack.trim();
        if (shortStack.startsWith("(cast to ItemStack)")) {
            shortStack = shortStack.substring(19).trim();
        }

        return readShortStack(shortStack);
    }

    public static Optional<Item> build(Consumer<ItemBuilder> builder) {
        return Optional.empty();
    }

    public static Optional<Item> build(Map<String, Object> map) {
        return Optional.empty();
    }

    public static Optional<Item> build(String shortStack, Consumer<ItemBuilder> builder) {
        shortStack = shortStack.trim();
        if (shortStack.startsWith("(cast to ItemStack)")) {
            shortStack = shortStack.substring(19).trim();
        }

        Optional<Item> item = readShortStack(shortStack);
        if (!item.isPresent()) {
            return Optional.empty();
        }

        return item;
    }

    private static Optional<Item> readShortStack(String shortStack) {
        Matcher matcher = SHORT_STACK_PATTERN.matcher(shortStack);

        if (!matcher.matches() || matcher.group("material") == null) {
            return Optional.empty();
        }

        String material = matcher.group("material");
        String amount = matcher.group("amount");
        String name = matcher.group("name");
        if (name != null && name.startsWith("\"") && name.endsWith("\"")) {
            name = name.substring(1, name.length() - 1);
        }
        String lore_string = matcher.group("lore");
        List<String> lore = new ArrayList<>();
        if (lore_string != null) {
            Matcher loreMatcher = LORE_SPLIT.matcher(lore_string);
            while (loreMatcher.find()) {
                lore.add(loreMatcher.group());
            }
        }

        Optional<MaterialHolder> materialHolder = MaterialMapping.resolve(material);
        if (!materialHolder.isPresent()) {
            return Optional.empty();
        }
        Item item = new Item();
        item.setMaterial(materialHolder.get());
        try {
            if (amount != null && !amount.trim().isEmpty()) {
                item.setAmount(Integer.parseInt(amount.trim()));
            }
        } catch (NumberFormatException ignored) {}
        if (name != null && !name.trim().isEmpty()) {
            item.setDisplayName(name.trim());
        }
        item.setLore(lore);

        return Optional.of(item);
    }
}
