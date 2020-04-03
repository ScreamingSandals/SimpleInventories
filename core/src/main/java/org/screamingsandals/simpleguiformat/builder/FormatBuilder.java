package org.screamingsandals.simpleguiformat.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleguiformat.utils.StackParser;

public class FormatBuilder {
	private final List<Object> result;
	
	public FormatBuilder() {
		this(new ArrayList<>());
	}	
	
	public FormatBuilder(List<Object> result) {
		this.result = result;
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
	
	public static class Item {
		private Map<String, Object> map;
		
		private Item(Map<String, Object> map) {
			this.map = map;
		}
		
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
