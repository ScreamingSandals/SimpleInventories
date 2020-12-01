package org.screamingsandals.simpleinventories.minestom.material;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.screamingsandals.simpleinventories.material.MaterialHolder;
import org.screamingsandals.simpleinventories.material.MaterialMapping;
import org.screamingsandals.simpleinventories.utils.OneWayTypeConverter;
import org.screamingsandals.simpleinventories.utils.Platform;

import java.util.Arrays;

public class MinestomMaterialMapping extends MaterialMapping {

    public static void init() {
        MaterialMapping.init(MinestomMaterialMapping.class);
    }

    public MinestomMaterialMapping() {
        platform = Platform.JAVA_FLATTENING;

        materialHolderConverter = OneWayTypeConverter.<MaterialHolder>builder()
                .convertor(Material.class, holder -> Material.valueOf(holder.getPlatformName()))
                .convertor(String.class, MaterialHolder::getPlatformName)
                .convertor(ItemStack.class, holder -> new ItemStack(Material.valueOf(holder.getPlatformName()), (byte) 1, holder.getDurability()))
                .construct();

        Arrays.stream(Material.values()).forEach(material -> materialMapping.put(material.name().toUpperCase(), new MaterialHolder(material.name())));
    }
}
