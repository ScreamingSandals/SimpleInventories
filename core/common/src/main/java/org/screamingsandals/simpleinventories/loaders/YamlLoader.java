package org.screamingsandals.simpleinventories.loaders;

import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class YamlLoader extends ConfigurateLoader {
    public YamlLoader() {
        super(YamlConfigurationLoader::builder);
    }
}
