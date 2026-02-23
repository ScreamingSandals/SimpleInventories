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

package org.screamingsandals.simpleinventories.groovy.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IGroovyStackBuilder {
    void amount(int amount);

    void lore(List<String> lore);

    void name(String name);

    void type(String type);

    void type(Material type);

    default void damage(short damage) {
        durability(damage);
    }

    void durability(short durability);

    void customModelData(int data);

    void repair(int repair);

    void flags(List<Object> flags);

    void unbreakable(boolean unbreakable);

    default void enchant(String enchant) {
        enchant(enchant, 1);
    }

    default void enchant(Enchantment enchant) {
        enchant(enchant, 1);
    }

    void enchant(String enchant, int level);

    void enchant(Enchantment enchant, int level);

    default void enchant(Map<Object, Integer> map) {
        map.forEach((key, value) -> {
            if (key instanceof Enchantment) {
                enchant((Enchantment) key, value);
            } else {
                enchant(key.toString(), value);
            }
        });
    }

    default void enchant(List<Object> list) {
        list.forEach(enchant -> {
            if (enchant instanceof Enchantment) {
                enchant((Enchantment) enchant);
            } else {
                enchant(enchant.toString());
            }
        });
    }

    void potion(String potion);

    void potion(PotionType potion);
}
