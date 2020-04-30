package org.screamingsandals.simpleinventories.groovy;

import com.google.common.base.Suppliers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.function.Supplier;

public class GroovyClassLoader {
    private final URLClassLoader classLoader;

    private final Supplier<Method> addUrlMethod;

    public GroovyClassLoader(ClassLoader classLoader) throws IllegalStateException {
        if (classLoader instanceof URLClassLoader) {
            this.classLoader = (URLClassLoader) classLoader;
        } else {
            throw new IllegalStateException("ClassLoader is not instance of URLClassLoader");
        }

        this.addUrlMethod = Suppliers.memoize(() -> {
            try {
                Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addUrlMethod.setAccessible(true);
                return addUrlMethod;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addJarToClasspath(File file) {
        try {
            this.addUrlMethod.get().invoke(this.classLoader, file.toURI().toURL());
        } catch (IllegalAccessException | InvocationTargetException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}