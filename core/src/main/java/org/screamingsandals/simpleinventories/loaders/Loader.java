package org.screamingsandals.simpleinventories.loaders;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.screamingsandals.simpleinventories.inventory.Origin;

public interface Loader {
	public Origin readData(File file, String configPath) throws Exception;
}
