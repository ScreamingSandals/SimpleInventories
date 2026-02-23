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

import org.bukkit.entity.Player;

public class PermissionPlaceholderParser implements PlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, String[] arguments) {
		if (arguments.length >= 2) {
			String[] permission = new String[arguments.length - 1];
			for (int i = 1; i < arguments.length; i++) {
				permission[i - 1] = arguments[i];
			}
			String perms = String.join(".", permission);
			if (arguments[0].equalsIgnoreCase("has")) {
				return Boolean.toString(player.hasPermission(perms));
			}
		}
		return "";
	}

}
