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
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;
import org.screamingsandals.simpleinventories.utils.MapReader;

public class ThisPlaceholderParser implements AdvancedPlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, PlayerItemInfo item, String[] arguments) {
		if (arguments.length > 0) {
			if (arguments.length == 1) {
				switch(arguments[0]) {
					case "stack":
						return item.getStack().getType().name();
					case "visible":
						return String.valueOf(item.isVisible());
					case "disabled":
						return String.valueOf(item.isDisabled());
				}
			}
			try {
				MapReader reader = item.getReader();
				for (int i = 0; i < arguments.length; i++) {
					if (i == (arguments.length - 1)) {
						return reader.getString(arguments[i]);
					} else {
						reader = reader.getMap(arguments[i]);
					}
				}
			} catch (Throwable t) {
				// Placeholder is not valid
			}
		}
		return "%" + key + "%"; // ignore this placeholder
	}

}
