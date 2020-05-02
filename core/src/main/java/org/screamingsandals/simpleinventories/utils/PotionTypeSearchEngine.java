package org.screamingsandals.simpleinventories.utils;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionTypeSearchEngine {


    private static final Map<String, String> regular = new HashMap<String, String>() {
        {
            put("empty", "UNCRAFTABLE");
            put("water", "WATER");
            put("mundane", "MUNDANE");
            put("thick", "THICK");
            put("awkward", "AWKWARD");
            put("night_vision", "NIGHT_VISION");
            put("invisibility", "INVISIBILITY");
            put("leaping", "JUMP");
            put("fire_resistance", "FIRE_RESISTANCE");
            put("swiftness", "SPEED");
            put("slowness", "SLOWNESS");
            put("water_breathing", "WATER_BREATHING");
            put("healing", "INSTANT_HEAL");
            put("harming", "INSTANT_DAMAGE");
            put("poison", "POISON");
            put("regeneration", "REGEN");
            put("strength", "STRENGTH");
            put("weakness", "WEAKNESS");
            put("luck", "LUCK");
            put("turtle_master", "TURTLE_MASTER");
            put("slow_falling", "SLOW_FALLING");
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
            put("strong_turtle_master", "TURTLE_MASTER");
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
            put("long_turtle_master", "TURTLE_MASTER");
            put("long_slow_falling", "SLOW_FALLING");
        }
    };

    public static PotionData find(String potionType) {
        String potion = potionType.toLowerCase();
        return Attemptor.start()
                .attempt(extendable.containsKey(potion), type -> new PotionData(PotionType.valueOf(extendable.get(type)), true, false))
                .attempt(upgradeable.containsKey(potion), type -> new PotionData(PotionType.valueOf(upgradeable.get(type)), false, true))
                .attempt(regular.containsKey(potion), type -> new PotionData(PotionType.valueOf(regular.get(type)), false, false))
                // if something is not in our maps yet
                .attempt(potion.startsWith("long_"), type -> new PotionData(PotionType.valueOf(type.substring(5).toUpperCase()), true, false))
                .attempt(potion.startsWith("strong_"), type -> new PotionData(PotionType.valueOf(type.substring(7).toUpperCase()), false, true))
                .attempt(type -> new PotionData(PotionType.valueOf(type.toUpperCase()), false, false))
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

        public PotionData end(String potionType) {
            for (Attempt attempt : attempts) {
                try {
                    PotionData data = attempt.attempt(potionType);
                    if (data != null) {
                        return data;
                    }
                } catch (Throwable t) {
                }
            }

            Bukkit.getLogger().warning("[SimpleInventories] Cannot find potion type for " + potionType + "! Your configuration is invalid!");
            return new PotionData(PotionType.UNCRAFTABLE, false, false);
        }
    }

    private static interface Attempt {
        public PotionData attempt(String potionType) throws Throwable;
    }
}
