package org.screamingsandals.simpleinventories.bukkit.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.simpleinventories.material.MaterialHolder;

public class BukkitFlatteningMaterial extends MaterialHolder {
    public BukkitFlatteningMaterial(String platformName) {
        super(platformName, (short) 0);
    }

    public BukkitFlatteningMaterial(String platformName, short durability) {
        super(platformName, durability);
    }

    @Override
    public MaterialHolder newDurability(short durability) {
        return new BukkitFlatteningMaterial(getPlatformName(), durability);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> theClass) {
        if (Material.class == theClass) {
            return (T) Material.valueOf(getPlatformName());
        } else if (ItemStack.class == theClass) {
            ItemStack stack = new ItemStack(Material.valueOf(getPlatformName()));
            ItemMeta meta = stack.getItemMeta();
            if (meta instanceof Damageable) {
                ((Damageable) meta).setDamage(getDurability());
                stack.setItemMeta(meta);
            }
            return (T) stack;
        } else if (String.class == theClass) {
            return (T) getPlatformName();
        }
        return null;
    }
}
