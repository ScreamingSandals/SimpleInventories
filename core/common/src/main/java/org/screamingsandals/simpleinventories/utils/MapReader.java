package org.screamingsandals.simpleinventories.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.operations.OperationParser;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

@SuppressWarnings("unchecked")
@Data
@RequiredArgsConstructor
public class MapReader {
	private final Inventory format;
	@Getter(AccessLevel.PRIVATE)
	private final Map<String, Object> map;
	private final PlayerWrapper player;
	private final PlayerItemInfo info;

	public MapReader getMap(String key) {
		var obj = map.get(key);
		if (obj instanceof Map) {
			return new MapReader(format, (Map<String, Object>) obj, player, info);
		}
		return null;
	}

	public String getString(String key) {
		return getString(key, null);
	}
	
	public String getString(String key, String def) {
		var obj = get(key);
		return obj != null ? obj.toString() : def;
	}
	
	private Number getNumber(String key, Number def) {
		var obj = get(key);
		if (obj instanceof String) {
			// Object is string, but maybe can be converted to number
			var s = (String) obj;
			try {
				return Double.parseDouble(s);
			} catch (NumberFormatException e) {
				// Object isn't number, try to use arithmetic
				try {
					var ob = OperationParser.getFinalOperation(format, s).resolveFor(player, info);
					if (ob instanceof Number) {
						return (Number) ob;
					} else if (ob instanceof String) {
						return Double.parseDouble((String) ob);
					}
				} catch (NumberFormatException ignored) {}
			}
		}
		if (obj instanceof Number) {
			return (Number) obj;
		}
		return def;
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int def) {
		return getNumber(key, def).intValue();
	}

	public float getFloat(String key) {
		return getFloat(key, 0);
	}

	public float getFloat(String key, float def) {
		return getNumber(key, def).floatValue();
	}

	public double getDouble(String key) {
		return getDouble(key, 0);
	}

	public double getDouble(String key, double def) {
		return getNumber(key, def).doubleValue();
	}

	public byte getByte(String key) {
		return getByte(key, (byte) 0);
	}

	public byte getByte(String key, byte def) {
		return getNumber(key, def).byteValue();
	}

	public short getShort(String key) {
		return getShort(key, (short) 0);
	}

	public short getShort(String key, short def) {
		return getNumber(key, def).shortValue();
	}

	public long getLong(String key) {
		return getLong(key, 0);
	}

	public long getLong(String key, long def) {
		return getNumber(key, def).longValue();
	}

	public char getChar(String key) {
		return getChar(key, (char) 0);
	}

	public char getChar(String key, char def) {
		var obj = get(key);
		if (obj instanceof String) {
			return ((String) obj).charAt(0);
		}
		if (obj instanceof Character) {
			return (Character) obj;
		}
		return def;
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean def) {
		Object obj = get(key);
		if (obj instanceof String) {
			return OperationParser.getFinalCondition(format, (String) obj).process(player, info);
		}
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		}
		return def;
	}
	
	public List<String> getStringList(String key) {
		return getStringList(key, null);
	}
	
	public List<String> getStringList(String key, List<String> def){
		var obj = get(key);
		if (!(obj instanceof List)) {
			return def;
		}
		var newStrList = new ArrayList<String>((List) obj);
		return newStrList.isEmpty() && def != null ? def : newStrList;
	}
	
	public List<Item> getStackList(String key) {
		return getStackList(key, null);
	}
	
	public List<Item> getStackList(String key, List<Item> def){
		var obj = get(key);
		if (!(obj instanceof List)) {
			return def;
		}
		var newStrList = ItemFactory.buildAll((List<Object>) obj);
		return newStrList.isEmpty() && def != null ? def : newStrList;
	}
	
	public Item getItemStack(String key) {
		return getItemStack(key, null);
	}
	
	public Item getItemStack(String key, Item def) {
		return ItemFactory.build(get(key)).orElse(def);
	}
	
	public List<MapReader> getMapList(String key) {
		var obj = map.get(key);
		var newMapList = new ArrayList<MapReader>();
		if (obj instanceof List) {
			var mapList = (List<Map<String, Object>>) obj;
			for (var map : mapList) {
				newMapList.add(new MapReader(format, map, player, info));
			}
		}
		return newMapList;
	}
	
	public boolean containsKey(String key) {
		return map.containsKey(key);
	}
	
	public Object get(String key) {
		return convert(map.get(key));
	}
	
	private Object convert(Object obj) {
		if (obj instanceof String) {
			obj = format.processPlaceholders(player, (String) obj, info);
			if (((String) obj).startsWith("(cast to ItemStack)")) {
				obj = ItemFactory.build(obj).orElse(ItemFactory.getAir());
			}
		}
		if (obj instanceof List) {
			var list = (List) obj;
			var nlist = new ArrayList<>();
			for (var ob : list) {
				nlist.add(convert(ob));
			}
			obj = nlist;
		}
		if (obj instanceof Map) {
			var map = (Map<String, Object>) obj;
			var casted = false;
			if (map.containsKey("cast")) {
				var cast = map.get("cast").toString().toLowerCase();
				if (cast.endsWith("itemstack")) {
					obj = ItemFactory.build(map).orElse(ItemFactory.getAir());
					casted = true;
				}
			}
			if (!casted) {
				var nmap = new HashMap<>();
				for (var entry : map.entrySet()) {
					nmap.put(entry.getKey(), convert(entry.getValue()));
				}
				obj = nmap;
				if (nmap.containsKey("==")) {
					// TODO: on Bukkit convert to such class
					//obj = ConfigurationSerialization.deserializeObject(nmap);
				}
			}
		}
		if (obj instanceof Item) {
			var stack = (Item) obj;
			if (stack.getDisplayName() != null) {
				stack.setDisplayName(format.processPlaceholders(player, stack.getDisplayName(), info));
			}
			if (stack.getLore() != null) {
				stack.setLore(stack.getLore().stream().map(str -> format.processPlaceholders(player, str, info)).collect(Collectors.toList()));
			}
		}
		return obj;
	}
	
	public Map<String, Object> convertToMap() {
		return (Map<String, Object>) convert(map);
	}
	
	@Deprecated
	public Map<String, Object> getOriginalData() {
		return map;
	}
}
