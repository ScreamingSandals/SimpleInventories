package org.screamingsandals.simpleinventories.material.builder;

import java.util.List;
import java.util.Map;

public class ItemBuilder {
    public void type(Object type) {}

    public void amount(int amount) {}
    public void name(String name) {}
    public void localizedName(String name) {}
    public void customModelData(int data) {}
    public void repair(int repair) {}
    public void flags(List<Object> flags) {}
    public void unbreakable(boolean unbreakable) {}
    public void lore(List<String> lore) {}

    public void enchant(Object enchant) {}
    public void enchant(Object enchant, int level) {}
    public void enchant(Map<Object, Integer> enchants) {}
    public void enchant(List<Object> enchants) {}

    public void potion(Object potion) {}

    // For legacy versions
    @Deprecated
    public void damage(int damage) {
        durability(damage);
    }
    // Or (durability is just alias for damage)
    public void durability(int durability) {}
}
