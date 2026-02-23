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

package org.screamingsandals.simpleinventories.operations.conditions;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

public class EqualsCondition extends AbstractCondition {

	public EqualsCondition(SimpleInventories format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(Player player, Object obj1, Object obj2, PlayerItemInfo info) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return ((Number) obj1).doubleValue() == ((Number) obj2).doubleValue();
		}
		if (obj1 instanceof String && obj2 instanceof String) {
			return ((String) obj1).equals((String) obj2);
		}
		if ((obj1 instanceof Number || obj2 instanceof Number) && (obj1 instanceof String || obj2 instanceof String)) {
			try {
				if (obj1 instanceof Number) {
					obj2 = Double.parseDouble((String) obj2);
				} else {
					obj1 = Double.parseDouble((String) obj1);
				}
				if (((Number) obj1).doubleValue() == ((Number) obj2).doubleValue()) {
					return true;
				}
			} catch (NumberFormatException ex) {
			}
		}
		return false;
	}

}
