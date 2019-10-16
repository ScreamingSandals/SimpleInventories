package misat11.lib.sgui.loaders;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class LoaderRegister {
	private static final Map<String, Loader> REGISTER = new HashMap<>();
	private static final Loader FALLBACK;
	
	static {
		Loader yaml = FALLBACK = new YamlLoader();
		
		REGISTER.put(".yml", yaml);
		REGISTER.put(".yaml", yaml);
	}
	
	public static Loader getLoader(File file) {
		String extension = "";

		int i = file.getAbsolutePath().lastIndexOf('.');
		if (i > -1) {
		    extension = file.getAbsolutePath().substring(i+1);
		}
		extension = extension.toLowerCase();
		if (REGISTER.containsKey(extension)) {
			return REGISTER.get(extension);
		}
		
		return FALLBACK;
	}
}
