package org.screamingsandals.simpleinventories.loaders;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.screamingsandals.simpleinventories.dependencies.DependencyHelper;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class CsvLoader implements ILoader {
    @Override
    public void loadPathInto(SubInventory subInventory, Path path, String configPath) throws Exception {
        DependencyHelper.UNIVOCITY.load();

        var list = new ArrayList<HashMap<String,?>>();

        var settings = new CsvParserSettings();
        settings.detectFormatAutomatically();

        var parser = new CsvParser(settings);
        var rows = parser.parseAll(path.toFile());

        String[] header = null;
        for (var row : rows) {
            if (header == null) {
                header = row;
            } else {
                var map = new HashMap<String,Object>();
                for (int i = 0; i < row.length && i < header.length; i++) {
                    map.put(header[i], row[i]);
                }
                list.add(map);
            }
        }

        var node = BasicConfigurationNode.root();
        list.forEach(map -> {
            var child = node.appendListNode();
            map.forEach((s, o) -> {
                try {
                    child.node((Object[]) s.split("\\.")).set(o);
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            });
        });

        ConfigurateLoader.loadConfigurationNodeInto(subInventory, node);
    }
}
