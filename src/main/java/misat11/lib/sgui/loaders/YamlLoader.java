package misat11.lib.sgui.loaders;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class YamlLoader implements Loader {
	@Override
	public List<Object> readData(File file, String configPath) throws Exception {
		YamlConfiguration config = new YamlConfiguration();
		config.load(file);
		return (List<Object>) config.getList(configPath);
	}
}
