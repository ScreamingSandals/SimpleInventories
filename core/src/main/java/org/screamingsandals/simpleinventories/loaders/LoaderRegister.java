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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class LoaderRegister {
	private static final Map<String, Loader> REGISTER = new HashMap<>();
	private static final Loader FALLBACK;
	
	static {
		Loader yaml = FALLBACK = new YamlLoader();
		
		REGISTER.put(".yml", yaml);
		REGISTER.put(".yaml", yaml);
		REGISTER.put(".json", yaml); // SnakeYAML can read json files

		Loader groovy = new GroovyLoader();

		REGISTER.put(".groovy", groovy);
	}

	public static Loader getLoader(String extension) {
		extension = extension.toLowerCase();
		if (!extension.startsWith(".")) {
			extension = "." + extension;
		}
		if (REGISTER.containsKey(extension)) {
			return REGISTER.get(extension);
		}
		
		return FALLBACK;
	}
	
	public static Loader getLoader(File file) {
		String extension = "";

		int i = file.getAbsolutePath().lastIndexOf('.');
		if (i > -1) {
		    extension = file.getAbsolutePath().substring(i+1);
		}
		return getLoader(extension);
	}
}
