package org.screamingsandals.simpleinventories.bukkit.material.builder;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.potion.PotionData;
import org.screamingsandals.simpleinventories.bukkit.material.BukkitMaterialMapping;
import org.screamingsandals.simpleinventories.bukkit.material.meta.BukkitEnchantmentMapping;
import org.screamingsandals.simpleinventories.bukkit.material.meta.BukkitPotionMapping;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;

import java.util.Map;

public class BukkitItemFactory extends ItemFactory {
    public static void init() {
        ItemFactory.init(BukkitItemFactory.class);
    }

    public BukkitItemFactory() {
        if (!BukkitMaterialMapping.isInitialized()) {
            BukkitMaterialMapping.init();
        }
        if (!BukkitEnchantmentMapping.isInitialized()) {
            BukkitEnchantmentMapping.init();
        }
        if (!BukkitPotionMapping.isInitialized()) {
            BukkitPotionMapping.init();
        }

        resultConverter
                .register(ItemStack.class, item -> {
                    ItemStack stack = item.getMaterial().as(ItemStack.class);
                    stack.setAmount(item.getAmount());
                    if (item.getPlatformMeta() != null) {
                        if (item.getPlatformMeta() instanceof ItemMeta) {
                            try {
                                stack.setItemMeta((ItemMeta) item.getPlatformMeta());
                            } catch (Throwable t) {
                            }
                        } else if (item.getPlatformMeta() instanceof Map) {
                            try {
                                stack.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject((Map<String,?>) item.getPlatformMeta()));
                            } catch (Throwable t) {
                            }
                        }
                    }

                    ItemMeta meta = stack.getItemMeta();

                    if (item.getDisplayName() != null) {
                        meta.setDisplayName(item.getDisplayName());
                    }
                    if (item.getLocalizedName() != null) {
                        try {
                            meta.setLocalizedName(item.getLocalizedName());
                        } catch (Throwable ignored) {}
                    }
                    try {
                        meta.setCustomModelData(item.getCustomModelData());
                    } catch (Throwable ignored) {}
                    if (meta instanceof Repairable) {
                        ((Repairable) meta).setRepairCost(item.getRepair());
                    }
                    meta.setUnbreakable(item.isUnbreakable());
                    if (item.getLore() != null) {
                        meta.setLore(item.getLore());
                    }
                    item.getEnchantments().forEach(e -> {
                        if (meta instanceof EnchantmentStorageMeta) {
                            ((EnchantmentStorageMeta) meta).addStoredEnchant(e.as(Enchantment.class), e.getLevel(), true);
                        } else {
                            meta.addEnchant(e.as(Enchantment.class), e.getLevel(), true);
                        }
                    });
                    if (item.getItemFlags() != null) {
                        try {
                            meta.addItemFlags(item.getItemFlags().stream().map(ItemFlag::valueOf).toArray(ItemFlag[]::new));
                        } catch (IllegalArgumentException ignored) {}
                    }
                    if (item.getPotion() != null && meta instanceof PotionMeta) {
                        try {
                            ((PotionMeta) stack.getItemMeta()).setBasePotionData(item.getPotion().as(PotionData.class));
                        } catch (Throwable ignored) {}
                    }

                    stack.setItemMeta(meta);

                    return stack;
                });
    }
}