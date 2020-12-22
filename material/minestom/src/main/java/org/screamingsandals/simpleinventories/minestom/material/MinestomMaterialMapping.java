package org.screamingsandals.simpleinventories.minestom.material;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.screamingsandals.simpleinventories.material.MaterialHolder;
import org.screamingsandals.simpleinventories.material.MaterialMapping;
import org.screamingsandals.simpleinventories.utils.Platform;

import java.util.Arrays;

public class MinestomMaterialMapping extends MaterialMapping {

    public static void init() {
        MaterialMapping.init(MinestomMaterialMapping::new);
    }

    public MinestomMaterialMapping() {
        platform = Platform.JAVA_FLATTENING;

        materialConverter
                .registerW2P(Material.class, holder -> Material.valueOf(holder.getPlatformName()))
                .registerW2P(ItemStack.class, holder -> new ItemStack(Material.valueOf(holder.getPlatformName()), (byte) 1, holder.getDurability()))
                .registerP2W(Material.class, material -> new MaterialHolder(material.name()))
                .registerP2W(ItemStack.class, stack -> new MaterialHolder(stack.getMaterial().name()));

        Arrays.stream(Material.values()).forEach(material -> materialMapping.put(material.name().toUpperCase(), new MaterialHolder(material.name())));
    }
}
