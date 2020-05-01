package org.screamingsandals.simpleinventories.loaders;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.PluginClassLoader;
import org.screamingsandals.simpleinventories.groovy.CantObtainGroovyException;
import org.screamingsandals.simpleinventories.groovy.GroovyClassLoader;
import org.screamingsandals.simpleinventories.groovy.MainGroovyBuilder;
import org.screamingsandals.simpleinventories.inventory.Origin;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

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
                Bukkit.getLogger().info("[SimpleInventories] Obtaining groovy.jar for your server");

                Files.copy(new URL("https://repo1.maven.org/maven2/org/codehaus/groovy/groovy/3.0.3/groovy-3.0.3.jar").openStream(), Paths.get(groovy.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);

                Files.write(Paths.get(new File("lib/groovy.version").getAbsolutePath()), Collections.singletonList("3.0.3"), StandardCharsets.UTF_8);
            }
            if (!groovy.exists()) {
                throw new CantObtainGroovyException();
            }

            Bukkit.getLogger().info("[SimpleInventories] Loading groovy.jar");
            GroovyClassLoader load = new GroovyClassLoader(this.getClass().getClassLoader());
            load.addJarToClasspath(groovy);
            Bukkit.getLogger().info("[SimpleInventories] Groovy is loaded! Don't use /reload command or some of your groovy scripts will be corrupted!");
        }

        Binding binding = new Binding();
        MainGroovyBuilder builder = new MainGroovyBuilder();
        binding.setVariable("inventory", builder);
        binding.setVariable("section", configPath);
        GroovyScriptEngine engine = new GroovyScriptEngine(new URL[]{file.getParentFile().toURI().toURL()});

        engine.run(file.getName(), binding);

        return new Origin(file, builder.getList());
    }
}
