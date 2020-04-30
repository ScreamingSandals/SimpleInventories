package org.screamingsandals.simpleinventories.groovy;

import lombok.AllArgsConstructor;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class GroovyStackBuilder {
    private final Map<String, Object> stack;

    public void amount(int amount) {
        stack.put("amount", amount);
    }

    public void lore(List<String> lore) {
        stack.put("lore", lore);
    }

    public void name(String name) {
        stack.put("name", name);
    }

    public void type(String type) {
        stack.put("type", type);
    }

    public void type(Material type) {
        stack.put("type", type);
    }
}
