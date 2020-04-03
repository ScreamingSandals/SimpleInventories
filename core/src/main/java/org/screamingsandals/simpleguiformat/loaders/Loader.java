package org.screamingsandals.simpleguiformat.loaders;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.screamingsandals.simpleguiformat.inventory.Origin;

public interface Loader {
	public Origin readData(File file, String configPath) throws Exception;
}
