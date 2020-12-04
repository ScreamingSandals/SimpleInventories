package org.screamingsandals.simpleinventories.material.meta;

import lombok.SneakyThrows;
import org.screamingsandals.simpleinventories.material.MaterialHolder;
import org.screamingsandals.simpleinventories.utils.ArgumentConverter;
import org.screamingsandals.simpleinventories.utils.ResultConverter;
import org.screamingsandals.simpleinventories.utils.RomanToDecimal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class EnchantmentMapping {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?:(?<namespace>[A-Za-z][A-Za-z0-9_.\\-]*):)?(?<enchantment>[A-Za-z][A-Za-z0-9_.\\-/]*)(\\s+(?<level>(\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)))?)?$");
    private static EnchantmentMapping mapping = null;
    protected final Map<String, EnchantmentHolder> enchantmentMapping = new HashMap<>();

    protected ResultConverter<EnchantmentHolder> resultConverter = ResultConverter.<EnchantmentHolder>build()
            .register(String.class, EnchantmentHolder::getPlatformName);
    protected ArgumentConverter<EnchantmentHolder> argumentConverter = ArgumentConverter.<EnchantmentHolder>build()
            .register(EnchantmentHolder.class, e -> e)
            .register(Map.Entry.class, entry -> {
                Optional<EnchantmentHolder> holder = resolve(entry.getKey());
                if (holder.isPresent()) {
                    int level;
                    if (entry.getValue() instanceof Number) {
                        level = ((Number) entry.getValue()).intValue();
                    } else {
                        try {
                            level = Integer.parseInt(entry.getValue().toString());
                        } catch (Throwable t) {
                            level = RomanToDecimal.romanToDecimal(entry.getValue().toString());
                        }
                    }
                    return holder.get().newLevel(level);
                }
                return null;
            });

    public static Optional<EnchantmentHolder> resolve(Object enchantmentObject) {
        if (mapping == null) {
            throw new UnsupportedOperationException("Enchantment mapping is not initialized yet.");
        }
        Optional<EnchantmentHolder> opt = mapping.argumentConverter.convertOptional(enchantmentObject);
        if (opt.isPresent()) {
            return opt;
        }
        String enchantment = enchantmentObject.toString().trim();

        Matcher matcher = RESOLUTION_PATTERN.matcher(enchantment);

        if (!matcher.matches()) {
            return Optional.empty();
        }

        if (matcher.group("enchantment") != null) {

            /*String namespace = matcher.group("namespace");*/ // namespace is currently useless on vanilla

            String name = matcher.group("enchantment").toUpperCase();
            String level_str = matcher.group("level");

            if (mapping.enchantmentMapping.containsKey(name)) {
                if (level_str != null && !level_str.isEmpty()) {
                    int level;
                    try {
                        level = Integer.parseInt(level_str);
                    } catch (Throwable t) {
                        level = RomanToDecimal.romanToDecimal(level_str);
                    }
                    return Optional.of(mapping.enchantmentMapping.get(name).newLevel(level));
                } else {
                    return Optional.of(mapping.enchantmentMapping.get(name));
                }
            }
        }

        return Optional.empty();
    }

    @SneakyThrows
    public static void init(Class<? extends EnchantmentMapping> mappingClass) {
        if (mapping != null) {
            throw new UnsupportedOperationException("Enchantment mapping is already initialized.");
        }

        mapping = mappingClass.getConstructor().newInstance();
        mapping.resultConverter.finish();
        mapping.argumentConverter.finish();

        mapping.legacyMapping();
    }

    private void legacyMapping() {
        f2l("POWER", "ARROW_DAMAGE");
        f2l("FLAME", "ARROW_FIRE");
        f2l("INFINITY", "ARROW_INFINITE");
        f2l("PUNCH", "ARROW_KNOCKBACK");
        f2l("SHARPNESS", "DAMAGE_ALL");
        f2l("BANE_OF_ARTHROPODS", "DAMAGE_ARTHROPODS");
        f2l("SMITE", "DAMAGE_UNDEAD");
        f2l("EFFICIENCY", "DIG_SPEED");
        f2l("UNBREAKING", "DURABILITY");
        f2l("FORTUNE", "LOOT_BONUS_BLOCKS");
        f2l("LOOTING", "LOOT_BONUS_MOBS");
        f2l("LUCK_OF_THE_SEA", "LUCK");
        f2l("RESPIRATION", "OXYGEN");
        f2l("PROTECTION", "PROTECTION_ENVIRONMENTAL");
        f2l("BLAST_PROTECTION", "PROTECTION_EXPLOSIONS");
        f2l("FEATHER_FALLING", "PROTECTION_FALL");
        f2l("FIRE_PROTECTION", "PROTECTION_FIRE");
        f2l("PROJECTILE_PROTECTION", "PROTECTION_PROJECTILE");
        f2l("SWEEPING", "SWEEPING_EDGE");
        f2l("AQUA_AFFINITY", "WATER_WORKER");
    }

    private void f2l(String enchantment, String legacyEnchantments) {
        if (enchantment == null || legacyEnchantments == null) {
            throw new IllegalArgumentException("Both enchantments mustn't be null!");
        }
        if (enchantmentMapping.containsKey(enchantment.toUpperCase()) && !enchantmentMapping.containsKey(legacyEnchantments.toUpperCase())) {
            enchantmentMapping.put(legacyEnchantments.toUpperCase(), enchantmentMapping.get(enchantment.toUpperCase()));
        } else if (enchantmentMapping.containsKey(legacyEnchantments.toUpperCase()) && !enchantmentMapping.containsKey(enchantment.toUpperCase())) {
            enchantmentMapping.put(enchantment.toUpperCase(), enchantmentMapping.get(legacyEnchantments.toUpperCase()));
        }
    }

    public static <T> T convertEnchantmentHolder(EnchantmentHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("Enchantment mapping is not initialized yet.");
        }
        return mapping.resultConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return mapping != null;
    }

}
