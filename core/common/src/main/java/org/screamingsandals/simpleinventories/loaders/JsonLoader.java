package org.screamingsandals.simpleinventories.loaders;

import org.spongepowered.configurate.gson.GsonConfigurationLoader;

public class JsonLoader extends ConfigurateLoader {
    public JsonLoader() {
        super(GsonConfigurationLoader::builder);
    }
}
