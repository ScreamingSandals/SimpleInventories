package org.screamingsandals.simpleinventories.loaders;

import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;

public class HoconLoader implements ILoader {
    @Override
    public void loadFileInto(SubInventory subInventory, File file, String configPath) throws Exception {
        var loader = HoconConfigurationLoader.builder()
                .path(file.toPath())
                .build();

        final ConfigurationNode root = loader.load();

        var configKeys = configPath.split("\\.");
        var node = root.node((Object[]) configKeys);

        ConfigurateLoader.loadConfigurationNodeInto(subInventory, node);
    }
}
