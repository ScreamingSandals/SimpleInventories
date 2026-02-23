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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.screamingsandals.simpleinventories.groovy.utils.GroovyUtils.internalCallClosure;

@AllArgsConstructor
@Getter
public class GroovyAnimationBuilder {
    private final List<Object> stacks;

    public void stack(String material) {
        putStack(material);
    }

    public void stack(String material, Closure<IGroovyStackBuilder> closure) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", material);
        putStack(map);
        internalCallClosure(closure, new GroovyLongStackBuilder(map));
    }

    public void stack(Closure<IGroovyStackBuilder> closure) {
        Map<String, Object> map = new HashMap<>();
        putStack(map);
        internalCallClosure(closure, new GroovyLongStackBuilder(map));
    }

    private void putStack(Object stack) {
        stacks.add(stack);
    }
}
