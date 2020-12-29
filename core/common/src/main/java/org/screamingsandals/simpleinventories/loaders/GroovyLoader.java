package org.screamingsandals.simpleinventories.loaders;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.screamingsandals.simpleinventories.builder.CategoryBuilder;
import org.screamingsandals.simpleinventories.builder.InventorySetBuilder;
import org.screamingsandals.simpleinventories.dependencies.DependencyHelper;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

import java.net.URL;
import java.nio.file.Path;

public class GroovyLoader implements ILoader {
    @Override
    public void loadPathInto(SubInventory subInventory, Path path, String configPath) throws Exception {
        DependencyHelper.GROOVY.load();

        Binding binding = new Binding();
        CategoryBuilder builder;
        if (subInventory.isMain()) {
            builder = InventorySetBuilder.of(subInventory.getInventorySet()); // TODO: change this logic
        } else {
            builder = CategoryBuilder.of(subInventory);
        }

        binding.setVariable("inventory", builder);
        binding.setVariable("section", configPath);
        GroovyScriptEngine engine = new GroovyScriptEngine(new URL[]{path.getParent().toUri().toURL()});

        engine.run(path.getFileName().toString(), binding);
    }
}
