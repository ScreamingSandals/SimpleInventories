package org.screamingsandals.simpleinventories.loaders;

import java.io.File;

import org.screamingsandals.simpleinventories.inventory.LocalOptions;
import org.screamingsandals.simpleinventories.inventory.Origin;

public interface Loader {
	Origin readData(File file, String configPath, LocalOptions options) throws Exception;
}
