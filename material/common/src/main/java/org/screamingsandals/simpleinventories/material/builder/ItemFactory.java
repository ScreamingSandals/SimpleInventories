package org.screamingsandals.simpleinventories.material.builder;

import lombok.SneakyThrows;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.material.MaterialHolder;
import org.screamingsandals.simpleinventories.material.MaterialMapping;
import org.screamingsandals.simpleinventories.material.meta.EnchantmentMapping;
import org.screamingsandals.simpleinventories.material.meta.PotionMapping;
import org.screamingsandals.simpleinventories.utils.BidirectionalConverter;
import org.screamingsandals.simpleinventories.utils.ConsumerExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class ItemFactory {

    private static ItemFactory factory;
    protected BidirectionalConverter<Item> itemConverter = BidirectionalConverter.<Item>build()
            .registerW2P(String.class, item -> item.getMaterial().getPlatformName())
            .registerW2P(MaterialHolder.class, Item::getMaterial)
            .registerP2W(Map.class, map -> {
                Object type = map.get("type");
                if (type == null) {
                    return null;
                }

                Optional<Item> optionalItem = readStack(type.toString());
                if (!optionalItem.isPresent()) {
                    return null;
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
                                EnchantmentMapping.resolve(en).ifPresent(item.getEnchantments()::add)
                        );
                    } else if (map.get("enchants") instanceof Map) {
                        ((Map<Object, Object>) map.get("enchants")).entrySet().forEach(entry ->
                                EnchantmentMapping.resolve(entry).ifPresent(item.getEnchantments()::add)
                        );
                    } else {
                        EnchantmentMapping.resolve(map.get("enchants")).ifPresent(item.getEnchantments()::add);
                    }
                }

                if (map.containsKey("potion-type")) {
                    PotionMapping.resolve(map.get("potion-type")).ifPresent(item::setPotion);
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

                return item;
            });

    private static final Pattern SHORT_STACK_PATTERN = Pattern.compile("^(?<material>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(\\\\*)?(;(?<amount>(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+)?(\\\\*)?(;(?<name>(\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+|(?:(?!(?<!\\\\)(?:\\\\\\\\)*;).)+))?(\\\\*)?(;(?<lore>.*))?)?)?$");
    private static final Pattern LORE_SPLIT = Pattern.compile("((\"((?!(?<!\\\\)(?:\\\\\\\\)*\").)+\")|((?!(?<!\\\\)(?:\\\\\\\\)*;).)+)(?=($|;))");

    @SneakyThrows
    public static void init(Supplier<ItemFactory> factoryClass) {
        if (factory != null) {
            throw new UnsupportedOperationException("ItemFactory is already initialized.");
        }

        factory = factoryClass.get();

        assert MaterialMapping.isInitialized();
        assert PotionMapping.isInitialized();
        assert EnchantmentMapping.isInitialized();

        factory.itemConverter.finish();
    }

    public static ItemBuilder builder() {
        return new ItemBuilder(new Item());
    }

    public static Optional<Item> build(Object stack) {
        return readStack(stack);
    }

    public static Optional<Item> build(Consumer<ItemBuilder> builder) {
        var item = new Item();

        if (builder != null) {
            ConsumerExecutor.execute(builder, new ItemBuilder(item));
        }

        if (item.getMaterial() != null) {
            return Optional.of(item);
        }

        return Optional.empty();
    }

    public static Optional<Item> build(Object shortStack, Consumer<ItemBuilder> builder) {
        var item = readStack(shortStack);
        if (item.isEmpty()) {
            return Optional.empty();
        }

        if (builder != null) {
            ConsumerExecutor.execute(builder, new ItemBuilder(item.get()));
        }

        return item;
    }

    public static Optional<Item> readStack(Object stackObject) {
        Optional<Item> it = factory.itemConverter.convertOptional(stackObject);
        if (it.isPresent()) {
            return it;
        }
        return readShortStack(new Item(), stackObject);
    }

    public static Optional<Item> readShortStack(Item item, Object shortStackObject) {
        if (!(shortStackObject instanceof String)) {
            Optional<MaterialHolder> opt = MaterialMapping.resolve(shortStackObject);
            if (opt.isPresent()) {
                item.setMaterial(opt.get());
                return Optional.of(item);
            }
        }

        String shortStack = shortStackObject.toString().trim();
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

    public static List<Item> buildAll(List<Object> objects) {
        return objects.stream().map(o -> build(o).orElse(ItemFactory.getAir())).collect(Collectors.toList());
    }

    private static Item cachedAir;

    public static Item getAir() {
        if (cachedAir == null) {
            cachedAir = build("AIR").orElseThrow();
        }
        return cachedAir.clone();
    }

    public static <T> T convertItem(Item item, Class<T> newType) {
        if (factory == null) {
            throw new UnsupportedOperationException("ItemFactory is not initialized yet.");
        }
        return factory.itemConverter.convert(item, newType);
    }

    public static boolean isInitialized() {
        return factory != null;
    }
}
