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
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.screamingsandals.simpleinventories.inventory.LocalOptions;
import org.screamingsandals.simpleinventories.inventory.Origin;

public class YamlLoader implements Loader {
	@Override
	public Origin readData(File file, String configPath, LocalOptions options) throws Exception {
		YamlConfiguration config = new YamlConfiguration();
		config.load(file);

		String optionNodeKey;
		if ("data".equals(configPath)) {
			optionNodeKey = "options";
		} else {
			optionNodeKey = configPath + "_options";
		}

		ConfigurationSection optionsSection;
		if (config.isSet(optionNodeKey)) {
			optionsSection = config.getConfigurationSection(optionNodeKey);
		} else {
			optionsSection = null;
		}

		return new Origin(file, (List<Object>) config.getList(configPath), optionsSection);
	}
}
