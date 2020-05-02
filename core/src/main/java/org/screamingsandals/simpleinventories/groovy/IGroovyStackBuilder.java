package org.screamingsandals.simpleinventories.groovy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

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
}
