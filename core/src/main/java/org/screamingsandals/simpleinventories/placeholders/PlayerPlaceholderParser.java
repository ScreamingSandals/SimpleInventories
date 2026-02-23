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

public class PlayerPlaceholderParser implements PlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player player, String[] arguments) {
		if (arguments.length >= 1) {
			switch (arguments[0]) {
				case "exp":
				case "xp":
					return Float.toString(player.getExp());
				case "level":
					return Integer.toString(player.getLevel());
				case "displayName":
					return player.getDisplayName();
				case "tabName":
					return player.getPlayerListName();
				case "health":
					return Double.toString(player.getHealth());
				case "maxhealth":
					return Double.toString(player.getMaxHealth());
				case "isdead":
				case "dead":
					return Boolean.toString(player.isDead());
				case "isalive":
				case "alive":
					return Boolean.toString(!player.isDead());
				case "food":
					return Double.toString(player.getFoodLevel());
				case "firsttime":
					return Long.toString(player.getFirstPlayed());
				case "lasttime":
					return Long.toString(player.getLastPlayed());
				case "playertime":
					return Long.toString(player.getPlayerTime());
				case "gamemode":
					return player.getGameMode().name();
			}
		}
		return player.getName();
	}

}
