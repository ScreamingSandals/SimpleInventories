package org.screamingsandals.simpleinventories.loaders;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.screamingsandals.simpleinventories.builder.CategoryBuilder;
import org.screamingsandals.simpleinventories.builder.InventoryBuilder;
import org.screamingsandals.simpleinventories.dependencies.DependencyHelper;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

import java.io.File;
import java.net.URL;

public class GroovyLoader implements ILoader {
    @Override
    public void loadFileInto(SubInventory subInventory, File file, String configPath) throws Exception {
        DependencyHelper.GROOVY.load();

        Binding binding = new Binding();
        CategoryBuilder builder;
        if (subInventory.isMain()) {
            builder = new InventoryBuilder(subInventory.getFormat()); // TODO: change this logic
        } else {
            builder = new CategoryBuilder(subInventory);
        }

        binding.setVariable("inventory", builder);
        binding.setVariable("section", configPath);
        GroovyScriptEngine engine = new GroovyScriptEngine(new URL[]{file.getParentFile().toURI().toURL()});

        engine.run(file.getName(), binding);
    }
}
