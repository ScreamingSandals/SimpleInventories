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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;
import org.screamingsandals.simpleinventories.operations.Operation;

public class BooleanCondition implements Condition {
	protected SimpleInventories format;
	protected Object obj;

	public BooleanCondition(SimpleInventories format, Object obj) {
		this.format = format;
		this.obj = obj;
	}

	@Override
	public boolean process(Player player, PlayerItemInfo info) {
		Object ob = obj;
		if (ob instanceof Operation) {
			ob = ((Operation) ob).resolveFor(player, info);
		}
		if (ob instanceof Boolean) {
			return (Boolean) ob;
		}
		if (ob instanceof Number) {
			return ((Number) ob).doubleValue() != 0;
		}
		if (ob instanceof String) {
			try {
				double number = Double.parseDouble((String) ob);
				return number != 0;
			} catch (Throwable t) {
			}
			ob = format.processPlaceholders(player, (String) ob, info);
			return !((String) ob).isEmpty() && !"false".equalsIgnoreCase((String) ob)
					&& !"null".equalsIgnoreCase((String) ob);
		}
		return ob != null;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "[format=" + format + ";obj=" + obj + "]";
	}
}
