package org.screamingsandals.simpleinventories.loaders;

import org.spongepowered.configurate.xml.XmlConfigurationLoader;

public class XmlLoader extends ConfigurateLoader {
    public XmlLoader() {
        super(XmlConfigurationLoader::builder);
    }
}
