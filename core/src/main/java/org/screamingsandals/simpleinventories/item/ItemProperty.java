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

package org.screamingsandals.simpleinventories.item;

import java.util.Map;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.utils.MapReader;

public class ItemProperty {
	private SimpleInventories format;
	private String propertyName;
	private Map<String, Object> propertyData;
	
	public ItemProperty(SimpleInventories format, String propertyName, Map<String, Object> propertyData) {
		this.format = format;
		this.propertyName = propertyName;
		this.propertyData = propertyData;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	@Deprecated
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	@Deprecated
	public MapReader getReader(Player player) {
		return new MapReader(format, propertyData, player, null);
	}
	
	public MapReader getReader(Player player, PlayerItemInfo info) {
		return new MapReader(format, propertyData, player, info);
	}
	
	@Deprecated
	public Map<String, Object> getPropertyData() {
		return propertyData;
	}
	
	@Deprecated
	public void setPropertyData(Map<String, Object> propertyData) {
		this.propertyData = propertyData;
	}
	
	public boolean hasName() {
		return propertyName != null;
	}
	
	public boolean hasData() {
		return propertyData != null && !propertyData.isEmpty();
	}
	
	public SimpleInventories getFormat() {
		return format;
	}
	
}
