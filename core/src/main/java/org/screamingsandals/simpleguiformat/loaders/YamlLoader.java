package org.screamingsandals.simpleguiformat.loaders;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.screamingsandals.simpleguiformat.inventory.Origin;

public class YamlLoader implements Loader {
	@Override
	public Origin readData(File file, String configPath) throws Exception {
		YamlConfiguration config = new YamlConfiguration();
		config.load(file);
		return new Origin(file, (List<Object>) config.getList(configPath));
	}
}
