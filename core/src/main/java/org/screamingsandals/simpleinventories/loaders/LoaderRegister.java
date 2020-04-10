package org.screamingsandals.simpleinventories.loaders;

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
		REGISTER.put(".json", yaml); // SnakeYAML can read json files
		
		Loader csv = new CsvLoader();
		
		REGISTER.put(".csv", csv);
		REGISTER.put(".txt", csv);
	}

	public static Loader getLoader(String extension) {
		extension = extension.toLowerCase();
		if (!extension.startsWith(".")) {
			extension = "." + extension;
		}
		if (REGISTER.containsKey(extension)) {
			return REGISTER.get(extension);
		}
		
		return FALLBACK;
	}
	
	public static Loader getLoader(File file) {
		String extension = "";

		int i = file.getAbsolutePath().lastIndexOf('.');
		if (i > -1) {
		    extension = file.getAbsolutePath().substring(i+1);
		}
		return getLoader(extension);
	}
}
