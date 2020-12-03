package org.screamingsandals.simpleinventories.minestom.material.builder;

import net.minestom.server.chat.ColoredText;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.EnchantedBookMeta;
import net.minestom.server.item.metadata.ItemMeta;
import net.minestom.server.item.metadata.PotionMeta;
import net.minestom.server.potion.PotionType;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.minestom.material.MinestomMaterialMapping;
import org.screamingsandals.simpleinventories.minestom.material.meta.MinestomEnchantmentMapping;
import org.screamingsandals.simpleinventories.minestom.material.meta.MinestomPotionMapping;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MinestomItemFactory extends ItemFactory {
    public static void init() {
        ItemFactory.init(MinestomItemFactory.class);
    }

    public MinestomItemFactory() {
        if (!MinestomMaterialMapping.isInitialized()) {
            MinestomMaterialMapping.init();
        }
        if (!MinestomEnchantmentMapping.isInitialized()) {
            MinestomEnchantmentMapping.init();
        }
        if (!MinestomPotionMapping.isInitialized()) {
            MinestomPotionMapping.init();
        }

        resultConverter
                .register(ItemStack.class, item -> {
                    var stack = item.getMaterial().as(ItemStack.class);
                    stack.setAmount((byte) item.getAmount());
                    if (item.getPlatformMeta() != null) {
                        if (item.getPlatformMeta() instanceof ItemMeta) {
                            stack.setItemMeta((ItemMeta) item.getPlatformMeta());
                        }
                    }

                    if (item.getDisplayName() != null) {
                        stack.setDisplayName(ColoredText.of(item.getDisplayName()));
                    }
                    if (item.getLocalizedName() != null) {
                        // where is that?
                    }
                    stack.setCustomModelData(item.getCustomModelData());
                    // repair
                    stack.setUnbreakable(item.isUnbreakable());
                    if (item.getLore() != null) {
                        stack.setLore(item.getLore().stream().map(ColoredText::of).collect(Collectors.toCollection(ArrayList::new)));
                    }
                    item.getEnchantments().forEach(e -> {
                        if (stack.getItemMeta() instanceof EnchantedBookMeta) {
                            ((EnchantedBookMeta) stack.getItemMeta()).setStoredEnchantment(e.as(Enchantment.class), (short) e.getLevel());
                        } else {
                            stack.setEnchantment(e.as(Enchantment.class), (short) e.getLevel());
                        }
                    });
                    if (item.getItemFlags() != null) {
                        try {
                            stack.addItemFlags(item.getItemFlags().stream().map(ItemFlag::valueOf).toArray(ItemFlag[]::new));
                        } catch (IllegalArgumentException ignored) {}
                    }
                    if (item.getPotion() != null && stack.getItemMeta() instanceof PotionMeta) {
                        ((PotionMeta) stack.getItemMeta()).setPotionType(item.getPotion().as(PotionType.class));
                    }

                    return stack;
                });
    }
}
