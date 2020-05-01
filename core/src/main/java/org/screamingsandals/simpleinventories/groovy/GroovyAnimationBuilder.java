package org.screamingsandals.simpleinventories.groovy;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.screamingsandals.simpleinventories.groovy.GroovyUtils.internalCallClosure;

@AllArgsConstructor
@Getter
public class GroovyAnimationBuilder {
    private final List<Object> stacks;

    public void stack(String material) {
        putStack(material);
    }

    public void stack(String material, Closure<GroovyStackBuilder> closure) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", material);
        putStack(map);
        internalCallClosure(closure, new GroovyStackBuilder(map));
    }

    public void stack(Closure<GroovyStackBuilder> closure) {
        Map<String, Object> map = new HashMap<>();
        putStack(map);
        internalCallClosure(closure, new GroovyStackBuilder(map));
    }

    private void putStack(Object stack) {
        if (!stacks.contains(stack)) {
            stacks.add(stack);
        }
    }
}
