package misat11.lib.sgui.loaders;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

public class YamlLoader implements Loader {
	@Override
	public List<Map<String, Object>> readData(File file, String configPath) throws Exception {
		YamlConfiguration config = new YamlConfiguration();
		config.load(file);
		return (List<Map<String, Object>>) config.getList(configPath);
	}
}
