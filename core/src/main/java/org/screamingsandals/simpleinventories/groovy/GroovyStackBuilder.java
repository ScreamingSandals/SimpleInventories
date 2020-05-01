package org.screamingsandals.simpleinventories.groovy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class GroovyStackBuilder {
    private final Map<String, Object> stack;

    public void amount(int amount) {
        stack.put("amount", amount);
    }

    public void lore(List<String> lore) {
        stack.put("lore", lore);
    }

    public void name(String name) {
        stack.put("display-name", name);
    }

    public void type(String type) {
        stack.put("type", type);
    }

    public void type(Material type) {
        stack.put("type", type);
    }

    public void damage(short damage) {
        durability(damage);
    }

    public void durability(short durability) {
        stack.put("durability", durability);
    }

    public void customModelData(int data) {
        stack.put("custom-model-data", data);
    }

    public void repair(int repair) {
        stack.put("repair-cost", repair);
    }

    public void flags(List<Object> flags) {
        stack.put("ItemFlags", flags);
    }

    public void unbreakable(boolean unbreakable) {
        stack.put("Unbreakable", unbreakable);
    }

    public void enchant(String enchant) {
        enchant(enchant, 1);
    }

    public void enchant(Enchantment enchant) {
        enchant(enchant, 1);
    }

    public void enchant(String enchant, int level) {
        if (!stack.containsKey("enchants")) {
            stack.put("enchants", new HashMap<>());
        }

        Map<Object, Object> map = (Map<Object, Object>) stack.get("enchants");
        map.put(enchant, level);
    }

    public void enchant(Enchantment enchant, int level) {
        if (!stack.containsKey("enchants")) {
            stack.put("enchants", new HashMap<>());
        }

        Map<Object, Object> map = (Map<Object, Object>) stack.get("enchants");
        map.put(enchant, level);
    }

    public void enchant(Map<Object, Integer> map) {
        map.forEach((key, value) -> {
            if (key instanceof Enchantment) {
                enchant((Enchantment) key, value);
            } else {
                enchant(key.toString(), value);
            }
        });
    }

    public void enchant(List<Object> list) {
        list.forEach(enchant -> {
            if (enchant instanceof Enchantment) {
                enchant((Enchantment) enchant);
            } else {
                enchant(enchant.toString());
            }
        });
    }
}
