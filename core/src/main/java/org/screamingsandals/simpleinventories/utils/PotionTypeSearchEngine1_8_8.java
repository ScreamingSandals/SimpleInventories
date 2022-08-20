package org.screamingsandals.simpleinventories.utils;

import org.bukkit.Bukkit;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionTypeSearchEngine1_8_8 {


    private static final Map<String, String> regular = new HashMap<String, String>() {
        {
            put("water", "WATER");
            put("regeneration", "REGEN");
            put("swiftness", "SPEED");
            put("fire_resistance", "FIRE_RESISTANCE");
            put("poison", "POISON");
            put("healing", "INSTANT_HEAL");
            put("night_vision", "NIGHT_VISION");
            put("weakness", "WEAKNESS");
            put("strength", "STRENGTH");
            put("slowness", "SLOWNESS");
            put("leaping", "JUMP");
            put("harming", "INSTANT_DAMAGE");
            put("water_breathing", "WATER_BREATHING");
            put("invisibility", "INVISIBILITY");
        }
    };

    private static final Map<String, String> upgradeable = new HashMap<String, String>() {
        {
            put("strong_leaping", "JUMP");
            put("strong_swiftness", "SPEED");
            put("strong_healing", "INSTANT_HEAL");
            put("strong_harming", "INSTANT_DAMAGE");
            put("strong_poison", "POISON");
            put("strong_regeneration", "REGEN");
            put("strong_strength", "STRENGTH");
            put("strong_slowness", "SLOWNESS");
        }
    };
    private static final Map<String, String> extendable = new HashMap<String, String>() {
        {
            put("long_night_vision", "NIGHT_VISION");
            put("long_invisibility", "INVISIBILITY");
            put("long_leaping", "JUMP");
            put("long_fire_resistance", "FIRE_RESISTANCE");
            put("long_swiftness", "SPEED");
            put("long_slowness", "SLOWNESS");
            put("long_water_breathing", "WATER_BREATHING");
            put("long_poison", "POISON");
            put("long_regeneration", "REGEN");
            put("long_strength", "STRENGTH");
            put("long_weakness", "WEAKNESS");
        }
    };

    public static Potion find(String potionType) {
        String potion = potionType.toLowerCase();
        return Attemptor.start()
                .attempt(extendable.containsKey(potion), type -> new Potion(PotionType.valueOf(extendable.get(type)), 1, false, true))
                .attempt(upgradeable.containsKey(potion), type -> new Potion(PotionType.valueOf(upgradeable.get(type)), 2, false, false))
                .attempt(regular.containsKey(potion), type -> new Potion(PotionType.valueOf(regular.get(type)), 1, false, false))
                // if something is not in our maps yet
                .attempt(potion.startsWith("long_"), type -> new Potion(PotionType.valueOf(type.substring(5).toUpperCase()), 1, false, true))
                .attempt(potion.startsWith("strong_"), type -> new Potion(PotionType.valueOf(type.substring(7).toUpperCase()), 2, false, false))
                .attempt(type -> new Potion(PotionType.valueOf(type.toUpperCase()), 1, false, false))
                .end(potion);
    }

    private static class Attemptor {
        private List<Attempt> attempts = new ArrayList<>();

        public static Attemptor start() {
            return new Attemptor();
        }

        public Attemptor attempt(Attempt attempt) {
            attempts.add(attempt);
            return this;
        }

        public Attemptor attempt(boolean condition, Attempt attempt) {
            if (condition) {
                attempts.add(attempt);
            }
            return this;
        }

        public Potion end(String potionType) {
            for (Attempt attempt : attempts) {
                try {
                    Potion data = attempt.attempt(potionType);
                    if (data != null) {
                        return data;
                    }
                } catch (Throwable t) {
                }
            }

            Bukkit.getLogger().warning("[SimpleInventories] Cannot find potion type for " + potionType + "! Your configuration is invalid!");
            return new Potion(PotionType.WATER);
        }
    }

    private interface Attempt {
        Potion attempt(String potionType) throws Throwable;
    }
}
