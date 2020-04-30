package org.screamingsandals.simpleinventories.groovy;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class GroovyItemBuilder extends GroovyBuilder {
    private final Map<String, Object> itemMap;

    @Override
    protected void putItem(Object object) {
        if (!itemMap.containsKey("items")) {
            itemMap.put("items", new ArrayList<>());
        }

        List<Object> list = (List<Object>) itemMap.get("items");

        if (!list.contains(object)) {
            list.add(object);
        }
    }

    public void stack(Closure<GroovyStackBuilder> closure) {
        internalCallClosure(closure, new GroovyStackBuilder((Map<String, Object>) itemMap.get("stack")));
    }

    public GroovyStackBuilder getStack() {
        return new GroovyStackBuilder((Map<String, Object>) itemMap.get("stack"));
    }

    public void price(String price) {
        itemMap.put("price", price);
    }

    public void property(String name) {
        property(new HashMap<String, Object>() {
            {
                put("name", name);
            }
        });
    }

    public void property(String name, Map<String, Object> map) {
        map.put("name", name);
        property(map);
    }

    public void property(Map<String, Object> map) {
        if (!itemMap.containsKey("properties")) {
            itemMap.put("properties", new ArrayList<>());
        }

        List<Object> list = (List<Object>) itemMap.get("properties");

        if (!list.contains(map)) {
            list.add(map);
        }
    }
}
