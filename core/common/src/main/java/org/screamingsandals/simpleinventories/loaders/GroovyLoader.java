/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.simpleinventories.loaders;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.screamingsandals.simpleinventories.builder.CategoryBuilder;
import org.screamingsandals.simpleinventories.builder.InventorySetBuilder;
import org.screamingsandals.simpleinventories.dependencies.DependencyHelper;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

import java.net.URL;
import java.nio.file.Path;

public class GroovyLoader implements ILoader {
    @Override
    public void loadPathInto(SubInventory subInventory, Path path, String configPath) throws Exception {
        DependencyHelper.GROOVY.load();

        Binding binding = new Binding();
        CategoryBuilder builder;
        if (subInventory.isMain()) {
            builder = InventorySetBuilder.of(subInventory.getInventorySet()); // TODO: change this logic
        } else {
            builder = CategoryBuilder.of(subInventory);
        }

        binding.setVariable("inventory", builder);
        binding.setVariable("section", configPath);
        GroovyScriptEngine engine = new GroovyScriptEngine(new URL[]{path.getParent().toUri().toURL()});

        engine.run(path.getFileName().toString(), binding);
    }
}
