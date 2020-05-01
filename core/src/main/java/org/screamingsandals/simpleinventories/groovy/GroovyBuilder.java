package org.screamingsandals.simpleinventories.groovy;

import groovy.lang.Closure;
import org.bukkit.Material;

import java.util.*;

import static org.screamingsandals.simpleinventories.groovy.GroovyUtils.internalCallClosure;

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

    public void cosmetic() {
        putItem(new HashMap<String, Object>() {
            {
                put("clone", "cosmetic");
            }
        });
    }

    public void cosmetic(Closure<GroovyItemBuilder> closure) {
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("clone", "cosmetic");
            }
        };
        putItem(map);
        internalCallClosure(closure, new GroovyItemBuilder(map));
    }

    public void itemClone(String clone) {
        putItem(new HashMap<String, Object>() {
            {
                put("clone", clone);
            }
        });
    }

    public void itemClone(String clone, Closure<GroovyOnlyItemBuilder> closure) {
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("clone", clone);
            }
        };
        putItem(map);
        internalCallClosure(closure, new GroovyItemBuilder(map));
    }

    public void include(String include) {
        putItem(new HashMap<String, Object>() {
            {
                put("include", include);
            }
        });
    }

    public void insert(String insert, Closure<GroovyOnlyItemBuilder> closure) {
        insert(Collections.singletonList(insert), closure);
    }

    public void insert(List<String> insert, Closure<GroovyOnlyItemBuilder> closure) {
        List<Object> items = new ArrayList<>();
        putItem(new HashMap<String, Object>() {
            {
                put("insert", insert);
                put("items", items);
            }
        });
        internalCallClosure(closure, new GroovyOnlyItemBuilder(items));
    }

    protected abstract void putItem(Object object);

    private Map<String, Object> internalAddItem(Object material) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> stack = new HashMap<>();
        if (material instanceof Material) {
            stack.put("type", material);
        } else {
            String obj = material.toString();
            char[] characters = obj.toCharArray();
            int lastEscapeIndex = -2;
            boolean buildingString = false;
            int building = 0;
            String buf = "";
            for (int i = 0; i < characters.length; i++) {
                char c = characters[i];
                if (c == '\\' && lastEscapeIndex != (i - 1)) {
                    lastEscapeIndex = i;
                } else if (buildingString) {
                    if ((c == '"' || c == '\'') && lastEscapeIndex != (i - 1)) {
                        buildingString = false;
                    } else {
                        buf += c;
                    }
                } else if (building < 2 && buf.endsWith("repeat")) {
                    buf = buf.substring(0, buf.length() - 6).trim();
                    if (building == 0) {
                        stack.put("type", buf);
                    } else {
                        map.put("price", buf);
                    }
                    building = 2;
                    buf = "";
                } else if (building == 0 && buf.endsWith("for")) {
                    building = 1;
                    buf = buf.substring(0, buf.length() - 3).trim();
                    stack.put("type", buf);
                    buf = "";
                } else if ((c == '"' || c == '\'') && lastEscapeIndex != (i - 1)) {
                    buildingString = true;
                    lastEscapeIndex = i;
                } else {
                    buf += c;
                }
            }
            if (building == 0) {
                buf = buf.trim();
                stack.put("type", buf);
            } else if (building == 1) {
                map.put("price", buf);
            } else if (building == 2) {
                try {
                    map.put("times", Integer.parseInt(buf.trim()));
                } catch (Throwable t) {
                }
            }
        }
        map.put("stack", stack);
        putItem(map);
        return map;
    }

    /* Just for groovy script purpose */

    public void call(Closure<GroovyBuilder> closure) {
        internalCallClosure(closure, this);
    }
}
