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

package org.screamingsandals.simpleinventories.placeholders;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;

public class PAPIPlaceholderParser implements PlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, String[] arguments) {
		String format = String.join(".", arguments);
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			Map<String, PlaceholderHook> hooks = PlaceholderAPI.getPlaceholders();
			int index = format.indexOf("_");
			if (index <= 0 || index >= format.length()) {
				return "%" + format + "%"; // ignore this placeholder
			}
			String identifier = format.substring(0, index).toLowerCase();
			String params = format.substring(index + 1);
			if (hooks.containsKey(identifier)) {
				String value = hooks.get(identifier).onPlaceholderRequest(player, params);
				if (value != null) {
					return value;
				}
			}
		}
		return "%" + format + "%"; // ignore this placeholder
	}

}
