package org.screamingsandals.simpleinventories.material.builder;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.material.MaterialMapping;
import org.screamingsandals.simpleinventories.material.meta.EnchantmentMapping;
import org.screamingsandals.simpleinventories.material.meta.PotionMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ItemBuilder {
    @NotNull
    private final Item item;

    public void type(@NotNull Object type) {
        // TODO: custom object resolving
        String typ = type.toString();
        MaterialMapping.resolve(typ).ifPresent(item::setMaterial);
    }

    public void amount(int amount) {
        item.setAmount(amount);
    }

    public void name(@Nullable String name) {
        item.setDisplayName(name);
    }

    public void localizedName(@Nullable String name) {
        item.setLocalizedName(name);
    }

    public void customModelData(int data) {
        item.setCustomModelData(data);
    }

    public void repair(int repair) {
        item.setRepair(repair);
    }

    public void flags(@Nullable List<Object> flags) {
        if (flags == null) {
            item.setItemFlags(null);
        } else {
            List<String> stringList = flags.stream().map(Object::toString).collect(Collectors.toList());
            item.setItemFlags(stringList);
        }
    }
    public void unbreakable(boolean unbreakable) {
        item.setUnbreakable(unbreakable);
    }

    public void lore(@Nullable List<String> lore) {
        item.setLore(lore);
    }

    public void enchant(@NotNull Object enchant) {
        EnchantmentMapping.resolve(enchant.toString()).ifPresent(item.getEnchantments()::add); // TODO: custom object resolving
    }

    public void enchant(@NotNull Object enchant, int level) {
        enchant(enchant + " " + level);
    }

    public void enchant(@NotNull Map<Object, Integer> enchants) {
        enchants.forEach(this::enchant);
    }

    public void enchant(@NotNull List<Object> enchants) {
        enchants.forEach(this::enchant);
    }

    public void potion(@NotNull Object potion) {
        PotionMapping.resolve(potion.toString()).ifPresent(item::setPotion); // TODO: custom object resolving
    }

    // For legacy versions
    @Deprecated
    public void damage(int damage) {
        durability(damage);
    }
    // Or (durability is just alias for damage)
    public void durability(int durability) {}
}
