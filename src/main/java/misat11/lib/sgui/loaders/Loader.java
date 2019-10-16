package misat11.lib.sgui.loaders;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface Loader {
	public List<Map<String, Object>> readData(File file, String configPath) throws Exception;
}
