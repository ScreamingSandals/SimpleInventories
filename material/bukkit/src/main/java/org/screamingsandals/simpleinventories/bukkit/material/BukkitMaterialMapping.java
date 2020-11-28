package org.screamingsandals.simpleinventories.bukkit.material;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.screamingsandals.simpleinventories.material.MaterialMapping;
import org.screamingsandals.simpleinventories.material.Platform;

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

        Arrays.stream(Material.values()).filter(t -> !t.name().startsWith("LEGACY")).forEach(material -> {
            if (platform == Platform.JAVA_FLATTENING) {
                materialMapping.put(material.name().toUpperCase(), new BukkitFlatteningMaterial(material.name()));
            } else {
                materialMapping.put(material.name().toUpperCase(), new BukkitLegacyMaterial(material.name()));
            }
        });
    }
}
