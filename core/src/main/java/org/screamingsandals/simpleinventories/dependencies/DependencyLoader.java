package org.screamingsandals.simpleinventories.dependencies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;

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

    private final String dependencyName;
    private final String dependencyURL;
    private final String dependencyURLVersion;

    public void load() {
        try {
            File lib = new File("lib/");
            if (!lib.exists()) {
                lib.mkdir();
            }

            File library = new File(lib, dependencyName.toLowerCase() + ".jar");
            if (!library.exists()) {
                Bukkit.getLogger().info("[ScreamingDependencyHelper] Obtaining " + dependencyName.toLowerCase() +".jar version " + dependencyURLVersion + " for your server");

                Files.copy(new URL(dependencyURL).openStream(), Paths.get(library.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);

                Files.write(Paths.get(new File("lib/" + dependencyName.toLowerCase() + ".version").getAbsolutePath()), Collections.singletonList(dependencyURLVersion), StandardCharsets.UTF_8);
            }
            if (!library.exists()) {
                throw new Exception("[ScreamingDependencyHelper] Can't obtain dependency: " + dependencyName);
            }

            Bukkit.getLogger().info("[ScreamingDependencyHelper] Loading " + dependencyName.toLowerCase() + ".jar");
            try {
                Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addUrlMethod.setAccessible(true);
                /* TODO: stop using plugin class loader to load dependencies and loads it to server (but how?) */
                addUrlMethod.invoke(this.getClass().getClassLoader(), library.toURI().toURL());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | MalformedURLException exception) {
                throw new Exception("[ScreamingDependencyHelper] Can't load dependency: " + dependencyName, exception);
            }
            Bukkit.getLogger().info("[ScreamingDependencyHelper] " + dependencyName + " is loaded! Don't disable or reload this automatically generated plugin!");
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}
