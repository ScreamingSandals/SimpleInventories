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
