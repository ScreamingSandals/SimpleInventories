package org.screamingsandals.simpleinventories.loaders;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.screamingsandals.simpleinventories.dependencies.DependencyHelper;
import org.screamingsandals.simpleinventories.groovy.MainGroovyBuilder;
import org.screamingsandals.simpleinventories.inventory.Origin;

import java.io.File;
import java.net.URL;

public class GroovyLoader implements Loader {
    @Override
    public Origin readData(File file, String configPath) throws Exception {
        DependencyHelper.GROOVY.load();

        Binding binding = new Binding();
        MainGroovyBuilder builder = new MainGroovyBuilder();
        binding.setVariable("inventory", builder);
        binding.setVariable("section", configPath);
        GroovyScriptEngine engine = new GroovyScriptEngine(new URL[]{file.getParentFile().toURI().toURL()});

        engine.run(file.getName(), binding);

        return new Origin(file, builder.getList());
    }
}
