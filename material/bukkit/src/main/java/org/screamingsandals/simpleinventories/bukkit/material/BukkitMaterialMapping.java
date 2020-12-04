package org.screamingsandals.simpleinventories.bukkit.material;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.simpleinventories.material.MappingFlags;
import org.screamingsandals.simpleinventories.material.MaterialHolder;
import org.screamingsandals.simpleinventories.material.MaterialMapping;
import org.screamingsandals.simpleinventories.utils.Platform;

import java.util.Arrays;

public class BukkitMaterialMapping extends MaterialMapping {

    @Getter
    private int versionNumber;

    public static void init() {
        MaterialMapping.init(BukkitMaterialMapping.class);
    }

    public BukkitMaterialMapping() {
        String[] bukkitVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        versionNumber = 0;

        for (int i = 0; i < 2; i++) {
            versionNumber += Integer.parseInt(bukkitVersion[i]) * (i == 0 ? 100 : 1);
        }

        platform = versionNumber < 113 ? Platform.JAVA_LEGACY : Platform.JAVA_FLATTENING;

        if (versionNumber < 112) {
            mappingFlags.add(MappingFlags.NO_COLORED_BEDS);
        }

        resultConverter
                .register(Material.class, holder -> Material.valueOf(holder.getPlatformName()))
                .register(ItemStack.class, holder -> {
                    if (platform == Platform.JAVA_FLATTENING) {
                        ItemStack stack = new ItemStack(Material.valueOf(holder.getPlatformName()));
                        ItemMeta meta = stack.getItemMeta();
                        if (meta instanceof Damageable) {
                            ((Damageable) meta).setDamage(holder.getDurability());
                            stack.setItemMeta(meta);
                        }
                        return stack;
                    } else if (platform == Platform.JAVA_LEGACY) {
                        return new ItemStack(Material.valueOf(holder.getPlatformName()), 1, (short) holder.getDurability());
                    } else {
                        throw new UnsupportedOperationException("Unknown platform!");
                    }
                });

        argumentConverter
                .register(Material.class, material -> new MaterialHolder(material.name()))
                .register(ItemStack.class, stack -> new MaterialHolder(stack.getType().name(), stack.getDurability()));

        Arrays.stream(Material.values()).filter(t -> !t.name().startsWith("LEGACY")).forEach(material ->
                materialMapping.put(material.name().toUpperCase(), new MaterialHolder(material.name()))
        );
    }
}
