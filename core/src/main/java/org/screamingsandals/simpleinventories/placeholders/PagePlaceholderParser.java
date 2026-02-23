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

public class PagePlaceholderParser implements AdvancedPlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, PlayerItemInfo item, String[] arguments) {
		if (arguments.length > 0) {
			if (arguments.length == 1) {
				switch(arguments[0]) {
					case "main":
						return item.getParent() == null ? "true" : "false";
					case "id":
						return item.getParent() != null ? item.getParent().getId() : "";
					case "number":
						return String.valueOf(item.getPosition() / (item.getParent() != null ? item.getParent().getLocalOptions().getItemsOnPage() : item.getFormat().getLocalOptions().getItemsOnPage()));
				}
			}
		}
		return "%" + key + "%"; // ignore this placeholder
	}

}
