package org.screamingsandals.simpleinventories.minestom.material.meta;

import net.minestom.server.item.Enchantment;
import org.screamingsandals.simpleinventories.material.meta.EnchantmentHolder;
import org.screamingsandals.simpleinventories.material.meta.EnchantmentMapping;

import java.util.Arrays;

public class MinestomEnchantmentMapping extends EnchantmentMapping {

    public static void init() {
        EnchantmentMapping.init(MinestomEnchantmentMapping.class);
    }

    public MinestomEnchantmentMapping() {
        resultConverter
                .register(Enchantment.class, e -> Enchantment.valueOf(e.getPlatformName()));

        Arrays.stream(Enchantment.values()).forEach(enchantment -> enchantmentMapping.put(enchantment.name().toUpperCase(), new EnchantmentHolder(enchantment.name())));
    }
}
