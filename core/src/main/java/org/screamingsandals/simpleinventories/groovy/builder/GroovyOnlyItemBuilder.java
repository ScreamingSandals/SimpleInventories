/*
 * Copyright (C) 2026 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.simpleinventories.groovy.builder;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.*;

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

    public void cosmetic() {
        internalAddItem(new HashMap<String, Object>() {
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
        internalAddItem(map);
        internalCallClosure(closure, new GroovyItemBuilder(map));
    }

    public void itemClone(String clone) {
        internalAddItem(new HashMap<String, Object>() {
            {
                put("clone", clone);
            }
        });
    }

    public void itemClone(String clone, Closure<GroovyItemBuilder> closure) {
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("clone", clone);
            }
        };
        internalAddItem(map);
        internalCallClosure(closure, new GroovyItemBuilder(map));
    }

    public void include(String include) {
        internalAddItem(new HashMap<String, Object>() {
            {
                put("include", include);
            }
        });
    }

    public void hidden(String id, Closure<GroovyOnlyItemBuilder> closure) {
        List<Object> items = new ArrayList<>();
        internalAddItem(new HashMap<String, Object>() {
            {
                put("id", id);
                put("write", false);
                put("items", items);
            }
        });
        internalCallClosure(closure, new GroovyOnlyItemBuilder(items));
    }

    public void hidden(String id) {
        internalAddItem(new HashMap<String, Object>() {
            {
                put("id", id);
                put("write", false);
            }
        });
    }

    public void insert(String insert, Closure<GroovyOnlyItemBuilder> closure) {
        insert(Collections.singletonList(insert), closure);
    }

    public void insert(List<String> insert, Closure<GroovyOnlyItemBuilder> closure) {
        List<Object> items = new ArrayList<>();
        internalAddItem(new HashMap<String, Object>() {
            {
                put("insert", insert);
                put("items", items);
            }
        });
        internalCallClosure(closure, new GroovyOnlyItemBuilder(items));
    }

    private Map<String, Object> internalAddItem(Object material) {
        Map<String, Object> map = material instanceof Map ? (Map<String, Object>) material : new HashMap<>();
        if (!(material instanceof Map)) {
            Map<String, Object> stack = new HashMap<>();
            stack.put("type", material);
            map.put("stack", stack);
        }
        list.add(map);
        return map;
    }
}
