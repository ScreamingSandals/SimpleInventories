package org.screamingsandals.simpleinventories.groovy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MainGroovyBuilder extends GroovyBuilder {
    private final List<Object> list = new ArrayList<>();

    @Override
    protected void putItem(Object object) {
        if (!list.contains(object)) {
            list.add(object);
        }
    }
}
