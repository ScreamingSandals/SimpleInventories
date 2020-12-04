package org.screamingsandals.simpleinventories.minestom.material;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.screamingsandals.simpleinventories.material.MaterialHolder;
import org.screamingsandals.simpleinventories.material.MaterialMapping;
import org.screamingsandals.simpleinventories.utils.Platform;

import java.util.Arrays;

public class MinestomMaterialMapping extends MaterialMapping {

    public static void init() {
        MaterialMapping.init(MinestomMaterialMapping.class);
    }

    public MinestomMaterialMapping() {
        platform = Platform.JAVA_FLATTENING;

        resultConverter
                .register(Material.class, holder -> Material.valueOf(holder.getPlatformName()))
                .register(ItemStack.class, holder -> new ItemStack(Material.valueOf(holder.getPlatformName()), (byte) 1, holder.getDurability()));

        argumentConverter
                .register(Material.class, material -> new MaterialHolder(material.name()))
                .register(ItemStack.class, stack -> new MaterialHolder(stack.getMaterial().name()));

        Arrays.stream(Material.values()).forEach(material -> materialMapping.put(material.name().toUpperCase(), new MaterialHolder(material.name())));
    }
}
