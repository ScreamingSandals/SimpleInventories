package org.screamingsandals.simpleinventories.material.builder;

import lombok.SneakyThrows;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.material.MaterialHolder;
import org.screamingsandals.simpleinventories.material.MaterialMapping;
import org.screamingsandals.simpleinventories.material.meta.EnchantmentMapping;
import org.screamingsandals.simpleinventories.material.meta.PotionMapping;
import org.screamingsandals.simpleinventories.utils.ConsumerExecutor;
import org.screamingsandals.simpleinventories.utils.ResultConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ItemFactory {

    private static ItemFactory factory;
    protected ResultConverter<Item> resultConverter = ResultConverter.<Item>build()
            .register(String.class, item -> item.getMaterial().getPlatformName())
            .register(MaterialHolder.class, Item::getMaterial);

    private static final Pattern SHORT_STACK_PATTERN = Pattern.compile("^(?<material>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(\\\\*)?(;(?<amount>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)?(\\\\*)?(;(?<name>(\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+|(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+))?(\\\\*)?(;(?<lore>.*))?)?)?$");
    private static final Pattern LORE_SPLIT = Pattern.compile("((\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+\")|((?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(?=($|;))");

    @SneakyThrows
    public static void init(Class<? extends ItemFactory> factoryClass) {
        if (factory != null) {
            throw new UnsupportedOperationException("ItemFactory is already initialized.");
        }

        factory = factoryClass.getConstructor().newInstance();
        factory.resultConverter.finish();
    }

    public static ItemBuilder builder() {
        return new ItemBuilder(new Item());
    }

    public static Optional<Item> build(String shortStack) {
        return readShortStack(shortStack);
    }

    public static Optional<Item> build(Consumer<ItemBuilder> builder) {
        Item item = new Item();

        if (builder != null) {
            ConsumerExecutor.execute(builder, new ItemBuilder(item));
        }

        if (item.getMaterial() != null) {
            return Optional.of(item);
        }

        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public static Optional<Item> build(Map<String, Object> map) {
        Object type = map.get("type");
        if (type == null) {
            return Optional.empty();
        }

        Optional<Item> optionalItem = readShortStack(type.toString()); // TODO: custom object resolving
        if (!optionalItem.isPresent()) {
            return Optional.empty();
        }
        Item item = optionalItem.get();

        if (map.containsKey("amount")) {
            try {
                item.setAmount(Integer.parseInt(map.get("amount").toString()));
            } catch (NumberFormatException ignored) {}
        }

        if (map.containsKey("display-name")) {
            try {
                item.setDisplayName(map.get("display-name").toString());
            } catch (NumberFormatException ignored) {}
        }

        if (map.containsKey("loc-name")) {
            try {
                item.setLocalizedName(map.get("loc-name").toString());
            } catch (NumberFormatException ignored) {}
        }

        if (map.containsKey("custom-model-data")) {
            try {
                item.setCustomModelData(Integer.parseInt(map.get("custom-model-data").toString()));
            } catch (NumberFormatException ignored) {}
        }

        if (map.containsKey("repair-cost")) {
            try {
                item.setRepair(Integer.parseInt(map.get("repair-cost").toString()));
            } catch (NumberFormatException ignored) {}
        }

        if (map.containsKey("ItemFlags") && map.get("ItemFlags") instanceof List) {
            List flags = (List) map.get("ItemFlags");
            for (Object str : flags) {
                if (item.getItemFlags() == null) {
                    item.setItemFlags(new ArrayList<>());
                }
                item.getItemFlags().add(str.toString());
            }
        }

        if (map.containsKey("Unbreakable")) {
            item.setUnbreakable(Boolean.parseBoolean(map.get("Unbreakable").toString()));
        }

        if (map.containsKey("lore")) {
            if (item.getLore() == null) {
                item.setLore(new ArrayList<>());
            }
            if (map.get("lore") instanceof List) {
                ((List) map.get("lore")).forEach(str -> item.getLore().add(str.toString()));
            } else {
                item.getLore().add(map.get("lore").toString());
            }
        }

        if (map.containsKey("enchants")) {
            if (map.get("enchants") instanceof List) {
                ((List) map.get("enchants")).forEach(en ->
                    EnchantmentMapping.resolve(en.toString()).ifPresent(item.getEnchantments()::add) // TODO: custom type resolving
                );
            } else if (map.get("enchants") instanceof Map) {
                ((Map<Object, Object>) map.get("enchants")).forEach((key, value) ->
                    EnchantmentMapping.resolve(key + " " + value).ifPresent(item.getEnchantments()::add) // TODO: custom type resolving
                );
            } else {
                EnchantmentMapping.resolve(map.get("enchants").toString()).ifPresent(item.getEnchantments()::add); // TODO: custom type resolving
            }
        }

        if (map.containsKey("potion-type")) {
            PotionMapping.resolve(map.get("potion-type").toString()).ifPresent(item::setPotion); // TODO: custom type resolving
        }

        if (map.containsKey("damage")) {
            try {
                item.setMaterial(item.getMaterial().newDurability(Integer.parseInt(map.get("damage").toString())));
            } catch (NumberFormatException ignored) {}
        }

        if (map.containsKey("durability")) {
            try {
                item.setMaterial(item.getMaterial().newDurability(Integer.parseInt(map.get("durability").toString())));
            } catch (NumberFormatException ignored) {}
        }

        if (map.containsKey("meta")) {
            item.setPlatformMeta(map.get("meta"));
        }

        return optionalItem;
    }

    public static Optional<Item> build(String shortStack, Consumer<ItemBuilder> builder) {
        Optional<Item> item = readShortStack(shortStack);
        if (!item.isPresent()) {
            return Optional.empty();
        }

        if (builder != null) {
            ConsumerExecutor.execute(builder, new ItemBuilder(item.get()));
        }

        return item;
    }

    private static Optional<Item> readShortStack(String shortStack) {
        shortStack = shortStack.trim();
        if (shortStack.startsWith("(cast to ItemStack)")) {
            shortStack = shortStack.substring(19).trim();
        }

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

    public static <T> T convertItem(Item item, Class<T> newType) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.resultConverter.convert(item, newType);
    }
}
