package org.screamingsandals.simpleinventories.groovy;

import groovy.lang.Closure;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public abstract class GroovyBuilder {

    public void category(String material) {
        item(material);
    }

    public void category(Material material) {
        item(material);
    }

    public void category(String material, Closure<GroovyItemBuilder> closure) {
        item(material, closure);
    }

    public void category(Material material, Closure<GroovyItemBuilder> closure) {
        item(material, closure);
    }

    public void item(String material) {
        internalAddItem(material);
    }

    public void item(Material material) {
        internalAddItem(material);
    }

    public void item(String material, Closure<GroovyItemBuilder> closure) {
        internalCallClosure(closure, new GroovyItemBuilder(internalAddItem(material)));
    }

    public void item(Material material, Closure<GroovyItemBuilder> closure) {
        internalCallClosure(closure, new GroovyItemBuilder(internalAddItem(material)));
    }

    protected abstract void putItem(Object object);

    private Map<String, Object> internalAddItem(Object material) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> stack = new HashMap<>();
        stack.put("type", material);
        map.put("stack", stack);
        putItem(map);
        return map;
    }

    protected void internalCallClosure(Closure<?> closure, Object delegate) {
        closure.setDelegate(delegate);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call(delegate);
    }

    /* Just for groovy script purpose */

    public void call(Closure<GroovyBuilder> closure) {
        internalCallClosure(closure, this);
    }
}
