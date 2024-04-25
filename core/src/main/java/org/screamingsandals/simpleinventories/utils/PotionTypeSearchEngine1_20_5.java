package org.screamingsandals.simpleinventories.utils;

import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class PotionTypeSearchEngine1_20_5 {
    public static PotionType find(String potionType) {
        String potion = potionType.toUpperCase(Locale.ROOT);

        // allow prefixing vanilla stuff with minecraft:
        if (potion.startsWith("MINECRAFT:")) {
            potion = potion.substring(10);
        }

        return PotionType.valueOf(potion);
    }

    public static void setPotionType(PotionMeta meta, PotionType potionType) {
        try {
            PotionMeta.class.getMethod("setBasePotionType", PotionType.class).invoke(meta, potionType);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
