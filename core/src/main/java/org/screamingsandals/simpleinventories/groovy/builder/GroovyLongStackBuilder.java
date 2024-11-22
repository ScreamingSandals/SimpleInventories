package org.screamingsandals.simpleinventories.groovy.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class GroovyLongStackBuilder implements IGroovyStackBuilder {
    private final Map<String, Object> stack;

    @Override
    public void amount(int amount) {
        stack.put("amount", amount);
    }

    @Override
    public void lore(List<String> lore) {
        stack.put("lore", lore);
    }

    @Override
    public void name(String name) {
        stack.put("display-name", name);
    }

    @Override
    public void type(String type) {
        stack.put("type", type);
    }

    @Override
    public void type(Material type) {
        stack.put("type", type);
    }

    public void tag(String tag) {
        stack.put("tag", tag);
    }

    @Override
    public void durability(short durability) {
        stack.put("durability", durability);
    }

    @Override
    public void customModelData(int data) {
        stack.put("custom-model-data", data);
    }

    @Override
    public void repair(int repair) {
        stack.put("repair-cost", repair);
    }

    @Override
    public void flags(List<Object> flags) {
        stack.put("ItemFlags", flags);
    }

    @Override
    public void unbreakable(boolean unbreakable) {
        stack.put("Unbreakable", unbreakable);
    }

    @Override
    public void enchant(String enchant, int level) {
        if (!stack.containsKey("enchants")) {
            stack.put("enchants", new HashMap<>());
        }

        Map<Object, Object> map = (Map<Object, Object>) stack.get("enchants");
        map.put(enchant, level);
    }

    @Override
    public void enchant(Enchantment enchant, int level) {
        if (!stack.containsKey("enchants")) {
            stack.put("enchants", new HashMap<>());
        }

        Map<Object, Object> map = (Map<Object, Object>) stack.get("enchants");
        map.put(enchant, level);
    }

    @Override
    public void potion(String potion) {
        stack.put("potion-type", potion);
    }

    @Override
    public void potion(PotionType potion) {
        stack.put("potion-type", potion);
    }
}
