package misat11.lib.sgui.loaders;

import java.io.File;
import java.util.List;
import java.util.Map;

import misat11.lib.sgui.Origin;

public interface Loader {
	public Origin readData(File file, String configPath) throws Exception;
}
