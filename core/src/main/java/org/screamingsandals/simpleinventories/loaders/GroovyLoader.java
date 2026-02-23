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

package org.screamingsandals.simpleinventories.loaders;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.screamingsandals.simpleinventories.dependencies.DependencyHelper;
import org.screamingsandals.simpleinventories.groovy.builder.MainGroovyBuilder;
import org.screamingsandals.simpleinventories.inventory.LocalOptions;
import org.screamingsandals.simpleinventories.inventory.Origin;

import java.io.File;
import java.net.URL;

public class GroovyLoader implements Loader {
    @Override
    public Origin readData(File file, String configPath, LocalOptions options) throws Exception {
        DependencyHelper.GROOVY.load();

        Binding binding = new Binding();
        MainGroovyBuilder builder = new MainGroovyBuilder(options);

        binding.setVariable("inventory", builder);
        binding.setVariable("section", configPath);
        GroovyScriptEngine engine = new GroovyScriptEngine(new URL[]{file.getParentFile().toURI().toURL()});

        engine.run(file.getName(), binding);

        Origin origin = new Origin(file, builder.getList());

        origin.setOpenCallbacks(builder.getOpenCallbacks());
        origin.setRenderCallbacks(builder.getRenderCallbacks());
        origin.setPreClickCallbacks(builder.getPreClickCallbacks());
        origin.setBuyCallbacks(builder.getBuyCallbacks());
        origin.setPostClickCallbacks(builder.getPostClickCallbacks());
        origin.setCloseCallbacks(builder.getCloseCallbacks());

        return origin;
    }
}
