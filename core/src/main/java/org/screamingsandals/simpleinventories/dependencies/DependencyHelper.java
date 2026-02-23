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

package org.screamingsandals.simpleinventories.dependencies;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DependencyHelper {
    GROOVY("groovy.util.GroovyScriptEngine", "Groovy", "3.0.16"),
    UNIVOCITY("com.univocity.parsers.csv.CsvParser", "Univocity", "2.8.3");

    private final String checkClass;
    private final String dependencyName;
    private final String dependencyVersion;

    public void load() {
        try {
            Class.forName(checkClass);
        } catch (ClassNotFoundException exception) {
            new DependencyLoader(checkClass, dependencyName, dependencyVersion).load();
        }
    }
}
