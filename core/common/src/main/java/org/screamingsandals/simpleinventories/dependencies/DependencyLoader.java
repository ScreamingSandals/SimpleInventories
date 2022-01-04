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

package org.screamingsandals.simpleinventories.dependencies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class DependencyLoader {

    private final String checkClass;
    private final String dependencyName;
    private final String dependencyVersion;

    public void load() {
        try {
            File lib = new File("libs/");
            if (!lib.exists()) {
                lib.mkdir();
            }

            File library = new File(lib, dependencyName.toLowerCase() + ".jar");
            if (!library.exists()) {
                SimpleInventoriesCore.getLogger().info("[ScreamingDependencyHelper] Obtaining " + dependencyName.toLowerCase() +".jar version " + dependencyVersion + " for your server");

                Files.copy(new URL("https://repo.screamingsandals.org/public/org/screamingsandals/misc/" + dependencyName.toLowerCase() + "/" + dependencyVersion + "/" + dependencyName.toLowerCase() + "-" + dependencyVersion.toLowerCase() + ".jar").openStream(), Paths.get(library.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);

                Files.write(Paths.get(new File(lib, dependencyName.toLowerCase() + ".version").getAbsolutePath()), Collections.singletonList(dependencyVersion), StandardCharsets.UTF_8);
            }
            if (!library.exists()) {
                throw new Exception("[ScreamingDependencyHelper] Can't obtain dependency: " + dependencyName);
            }

            SimpleInventoriesCore.getLogger().info("[ScreamingDependencyHelper] Loading " + dependencyName.toLowerCase() + ".jar");

            SimpleInventoriesCore.runJar(library);

            SimpleInventoriesCore.getLogger().info("[ScreamingDependencyHelper] " + dependencyName + " is loaded! Don't disable or reload this plugin!");
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}
