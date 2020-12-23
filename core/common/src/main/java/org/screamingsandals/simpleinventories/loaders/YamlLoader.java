package org.screamingsandals.simpleinventories.loaders;

import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;

public class YamlLoader implements ILoader {
    @Override
    public void loadFileInto(SubInventory subInventory, File file, String configPath) throws Exception {
        var loader = YamlConfigurationLoader.builder()
                .path(file.toPath())
                .build();

        final ConfigurationNode root = loader.load();

        var configKeys = configPath.split("\\.");
        var node = root.node((Object[]) configKeys);

        ConfigurateLoader.loadConfigurationNodeInto(subInventory, node);
    }
}
