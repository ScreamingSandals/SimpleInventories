package org.screamingsandals.simpleinventories.groovy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class MainGroovyBuilder extends GroovyBuilder {
    private final List<Object> list = new ArrayList<>();

    public void define(String definition) {
        putItem(new HashMap<String, Object>() {
            {
                put("define", definition);
            }
        });
    }

    @Override
    protected void putItem(Object object) {
        if (!list.contains(object)) {
            list.add(object);
        }
    }
}
