/*
 * Copyright (C) 2026 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.simpleinventories.utils;

import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PotionTypeEffectSearchEngine {
    private static final Map<String, String> minecraft2bukkit = new HashMap<String, String>() {
        {
            put("slowness", "slow");
            put("haste", "fast_digging");
            put("mining_fatigue", "slow_digging");
            put("strength", "increase_damage");
            put("instant_health", "heal");
            put("instant_damage", "harm");
            put("jump_boost", "jump");
            put("nausea", "confusion");
            put("resistance", "damage_resistance");
        }
    };

    public static PotionEffectType find(String potionType) {
        String potion = potionType.toLowerCase(Locale.ROOT);

        // allow prefixing vanilla stuff with minecraft:
        if (potion.startsWith("minecraft:")) {
            potion = potion.substring(10);
        }

        if (!MaterialSearchEngine.isV1_20_5() && MaterialSearchEngine.getVersionNumber() <= 120 && minecraft2bukkit.containsKey(potion)) {
            potion = minecraft2bukkit.get(potion);
        }

        return PotionEffectType.getByName(potion);
    }
}
