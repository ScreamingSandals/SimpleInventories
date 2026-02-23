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

package org.screamingsandals.simpleinventories.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleinventories.utils.StackParser;

@AllArgsConstructor
public class FormatBuilder {
	private final List<Object> result;
	
	public FormatBuilder() {
		this(new ArrayList<>());
	}
	
	public List<Object> getResult() {
		return this.result;
	}
	
	public Item add(String shortFormat) {
		return add(StackParser.parseShortStack(shortFormat));
	}
	
	public Item add(Material mat) {
		return add(new ItemStack(mat));
	}
	
	public Item add(Material mat, int amount) {
		return add(new ItemStack(mat, amount));
	}
	
	@Deprecated
	public Item add(Material mat, int amount, short damage) {
		return add(new ItemStack(mat, amount, damage));
	}
	
	public Item add(ItemStack stack) {
		Map<String, Object> map = new HashMap<>();
		map.put("stack", stack);
		result.add(map);
		return new Item(map);
	}

	@AllArgsConstructor
	public static class Item {
		private Map<String, Object> map;
		
		public Item set(String key, Object value) {
			this.map.put(key, value);
			return this;
		}
		
		public Item setIf(String condition, String key, Object value) {
			return setIf(condition, key, value, null);
		}
		
		@SuppressWarnings({ "unchecked", "serial" })
		public Item setIf(String condition, String key, Object value, Object elseValue) {
			if (!this.map.containsKey("conditions")) {
				this.map.put("conditions", new ArrayList<>());
			}
			Map<String, Object> nmap = new HashMap<>();
			nmap.put("if", condition);
			nmap.put("then", new HashMap<String, Object>() {
				{
					put(key, value);
				}
			});
			if (elseValue != null) {
				nmap.put("else", new HashMap<String, Object>() {
					{
						put(key, elseValue);
					}
				});
			}
			((List<Object>) this.map.get("conditions")).add(nmap);
			return this;
		}
		
		public Item setVisible(boolean visible) {
			return set("visible", visible);
		}
		
		public Item setVisible(String condition) {
			return setIf(condition, "visible", true, false);
		}
		
		public Item setDisabled(boolean disabled) {
			return set("disabled", disabled);
		}
		
		public Item setDisabled(String condition) {
			return setIf(condition, "disabled", true, false);
		}
		
		public Item setId(String id) {
			return set("id", id);
		}
		
		public Item add(String shortFormat) {
			return add(StackParser.parseShortStack(shortFormat));
		}
		
		public Item add(Material mat) {
			return add(new ItemStack(mat));
		}
		
		public Item add(Material mat, int amount) {
			return add(new ItemStack(mat, amount));
		}
		
		@Deprecated
		public Item add(Material mat, int amount, short damage) {
			return add(new ItemStack(mat, amount, damage));
		}
		
		@SuppressWarnings("unchecked")
		public Item add(ItemStack stack) {
			if (!this.map.containsKey("items")) {
				this.map.put("items", new ArrayList<>());
			}
			Map<String, Object> nmap = new HashMap<>();
			nmap.put("stack", stack);
			((List<Object>) this.map.get("items")).add(nmap);
			return new Item(nmap);
		}
	}
}
