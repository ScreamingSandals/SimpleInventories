package org.screamingsandals.simpleinventories.loaders;

import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

public class HoconLoader extends ConfigurateLoader {
    public HoconLoader() {
        super(HoconConfigurationLoader::builder);
    }
}
