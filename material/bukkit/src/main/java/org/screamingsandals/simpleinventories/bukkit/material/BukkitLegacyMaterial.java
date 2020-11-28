package org.screamingsandals.simpleinventories.bukkit.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleinventories.material.MaterialHolder;

public class BukkitLegacyMaterial extends MaterialHolder {

    public BukkitLegacyMaterial(String platformName) {
        this(platformName, (short) 0);
    }

    public BukkitLegacyMaterial(String platformName, short data) {
        super(platformName, data);
    }

    @Override
    public BukkitLegacyMaterial newDurability(short data) {
        return new BukkitLegacyMaterial(getPlatformName(), data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> theClass) {
        if (Material.class == theClass) {
            return (T) Material.valueOf(getPlatformName());
        } else if (ItemStack.class == theClass) {
            return (T) new ItemStack(Material.valueOf(getPlatformName()), 1, getDurability());
        } else if (String.class == theClass) {
            return (T) (getPlatformName() + ":" + getDurability());
        }
        return null;
    }
}
