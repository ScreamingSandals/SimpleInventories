package org.screamingsandals.simpleinventories.groovy.builder;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.screamingsandals.simpleinventories.groovy.utils.GroovyUtils.internalCallClosure;

@Getter
@AllArgsConstructor
public class GroovyOnlyItemBuilder {
    private final List<Object> list;

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

    private Map<String, Object> internalAddItem(Object material) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> stack = new HashMap<>();
        stack.put("type", material);
        map.put("stack", stack);
        list.add(map);
        return map;
    }
}
