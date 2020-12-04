package org.screamingsandals.simpleinventories.minestom.material.meta;

import net.minestom.server.potion.PotionType;
import org.screamingsandals.simpleinventories.material.meta.PotionHolder;
import org.screamingsandals.simpleinventories.material.meta.PotionMapping;

import java.util.Arrays;

public class MinestomPotionMapping extends PotionMapping {

    public static void init() {
        PotionMapping.init(MinestomPotionMapping.class);
    }

    public MinestomPotionMapping() {
        resultConverter
                .register(PotionType.class, e -> PotionType.valueOf(e.getPlatformName().toUpperCase()));

        argumentConverter
                .register(PotionType.class, e -> new PotionHolder(e.name()));

        Arrays.stream(PotionType.values()).forEach(potion -> potionMapping.put(potion.name().toUpperCase(), new PotionHolder(potion.name())));
    }
}
