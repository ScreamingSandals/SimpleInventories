package org.screamingsandals.simpleinventories.loaders;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.screamingsandals.simpleinventories.dependencies.DependencyHelper;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class CsvLoader implements ILoader {
    @Override
    public void loadFileInto(SubInventory subInventory, File file, String configPath) throws Exception {
        DependencyHelper.UNIVOCITY.load();

        var list = new ArrayList<>();

        CsvParserSettings settings = new CsvParserSettings();
        settings.detectFormatAutomatically();

        CsvParser parser = new CsvParser(settings);
        var rows = parser.parseAll(file);

        String[] header = null;
        for (String[] row : rows) {
            if (header == null) {
                header = row;
            } else {
                var map = new HashMap<>();
                for (int i = 0; i < row.length && i < header.length; i++) {
                    map.put(header[i], row[i]);
                }
                list.add(map);
            }
        }

        // TODO: process as map
    }
}
