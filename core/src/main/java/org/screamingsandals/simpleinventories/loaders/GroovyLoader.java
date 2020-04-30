package org.screamingsandals.simpleinventories.loaders;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.bukkit.Bukkit;
import org.screamingsandals.simpleinventories.groovy.CantObtainGroovyException;
import org.screamingsandals.simpleinventories.groovy.GroovyClassLoader;
import org.screamingsandals.simpleinventories.groovy.MainGroovyBuilder;
import org.screamingsandals.simpleinventories.inventory.Origin;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class GroovyLoader implements Loader {
    @Override
    public Origin readData(File file, String configPath) throws Exception {
        try {
            Class.forName("groovy.util.GroovyScriptEngine");
        } catch (ClassNotFoundException exception) {
            // Obtaining Groovy
            File lib = new File("lib/");
            if (!lib.exists()) {
                lib.mkdir();
            }

            File groovy = new File(lib, "groovy.jar");
            if (!groovy.exists()) {
                Bukkit.getLogger().info("Obtaining groovy.jar for your server");

                Files.copy(new URL("https://repo1.maven.org/maven2/org/codehaus/groovy/groovy/3.0.3/groovy-3.0.3.jar").openStream(), Paths.get(groovy.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            }
            if (!groovy.exists()) {
                throw new CantObtainGroovyException();
            }

            GroovyClassLoader load = new GroovyClassLoader(this.getClass().getClassLoader());
            load.addJarToClasspath(groovy);
        }

        Binding binding = new Binding();
        MainGroovyBuilder builder = new MainGroovyBuilder();
        binding.setVariable("builder", builder);
        GroovyScriptEngine engine = new GroovyScriptEngine(new URL[] {file.getParentFile().toURI().toURL()});

        engine.run(file.getName(), binding);

        return new Origin(file, builder.getList());
    }
}
