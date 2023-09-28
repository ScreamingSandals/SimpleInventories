package org.screamingsandals.simpleinventories.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EnchantmentSearchEngine {

    /* Legacy enchantment mapping for <= 1.12.2, on 1.13+ NamespacedKeys are used instead */
    private static final Map<String, String> LEGACY_MAPPING = new HashMap<String, String>() {
        {
            put("power", "ARROW_DAMAGE");
            put("flame", "ARROW_FIRE");
            put("infinity", "ARROW_INFINITE");
            put("punch", "ARROW_KNOCKBACK");
            put("sharpness", "DAMAGE_ALL");
            put("bane_of_arthropods", "DAMAGE_ARTHROPODS");
            put("smite", "DAMAGE_UNDEAD");
            put("efficiency", "DIG_SPEED");
            put("unbreaking", "DURABILITY");
            put("fortune", "LOOT_BONUS_BLOCKS");
            put("looting", "LOOT_BONUS_MOBS");
            put("luck_of_the_sea", "LUCK");
            put("respiration", "OXYGEN");
            put("protection", "PROTECTION_ENVIRONMENTAL");
            put("blast_protection", "PROTECTION_EXPLOSIONS");
            put("feather_falling", "PROTECTION_FALL");
            put("fire_protection", "PROTECTION_FIRE");
            put("projectile_protection", "PROTECTION_PROJECTILE");
            put("sweeping", "SWEEPING_EDGE");
            put("aqua_affinity", "WATER_WORKER");
        }
    };

    public static Enchantment searchEnchantment(Object enchantObj) {
        if (enchantObj instanceof Enchantment) {
            return (Enchantment) enchantObj;
        }

        String enchant = enchantObj.toString();

        Enchantment en = Enchantment.getByName(enchant.toUpperCase());
        if (en != null) {
            return en;
        }

        try {
            String[] split = enchant.toLowerCase().split(":", 2);
            if (split.length == 2) {
                return Enchantment.getByKey(new NamespacedKey(split[0], split[1]));
            } else {
                return Enchantment.getByKey(NamespacedKey.minecraft(enchant.toLowerCase()));
            }
        } catch (Throwable ignored) {}

        /* NamespacedKeys are not available on old versions, use hardcoded map */

        enchant = enchant.toLowerCase(Locale.ROOT);

        // allow prefixing vanilla stuff with minecraft:
        if (enchant.startsWith("minecraft:")) {
            enchant = enchant.substring(10);
        }

        return Enchantment.getByName(LEGACY_MAPPING.get(enchant));
    }
}
