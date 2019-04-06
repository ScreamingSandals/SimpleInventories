package misat11.lib.sgui.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlUtils {
	public static final Map<String, Object> readYamlFile(File file) {
		Yaml yaml = new Yaml();

		try {
			InputStream stream = new FileInputStream(file);
			Map<String, Object> obj = yaml.load(stream);
			return obj;
		} catch (Exception e) {
		}

		return null; // invalid file
	}
}
