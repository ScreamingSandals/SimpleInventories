package org.screamingsandals.simpleinventories.dependencies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class DependencyLoader {

    private final String checkClass;
    private final String dependencyName;
    private final String dependencyVersion;

    public void load() {
        try {
            File lib = new File("lib/");
            if (!lib.exists()) {
                lib.mkdir();
            }

            File library = new File(lib, dependencyName.toLowerCase() + ".jar");
            if (!library.exists()) {
                Bukkit.getLogger().info("[ScreamingDependencyHelper] Obtaining " + dependencyName.toLowerCase() +".jar version " + dependencyVersion + " for your server");

                Files.copy(new URL("https://repo.screamingsandals.org/org/screamingsandals/misc/" + dependencyName.toLowerCase() + "/" + dependencyVersion + "/" + dependencyName.toLowerCase() + "-" + dependencyVersion.toLowerCase() + ".jar").openStream(), Paths.get(library.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);

                Files.write(Paths.get(new File("lib/" + dependencyName.toLowerCase() + ".version").getAbsolutePath()), Collections.singletonList(dependencyVersion), StandardCharsets.UTF_8);
            }
            if (!library.exists()) {
                throw new Exception("[ScreamingDependencyHelper] Can't obtain dependency: " + dependencyName);
            }

            Bukkit.getLogger().info("[ScreamingDependencyHelper] Loading " + dependencyName.toLowerCase() + ".jar");

            Plugin plugin = Bukkit.getPluginManager().loadPlugin(library);
            Bukkit.getPluginManager().enablePlugin(plugin);

            Bukkit.getLogger().info("[ScreamingDependencyHelper] " + dependencyName + " is loaded! Don't disable or reload this plugin!");
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}
