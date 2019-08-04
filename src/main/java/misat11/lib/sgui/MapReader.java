package misat11.lib.sgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import misat11.lib.sgui.operations.OperationParser;

public class MapReader {

	private Map<String, Object> map;
	private SimpleGuiFormat format;
	private Player player;

	public MapReader(SimpleGuiFormat format, Map<String, Object> map, Player player) {
		this.map = map;
		this.format = format;
		this.player = player;
	}
	
	public SimpleGuiFormat getFormat() {
		return format;
	}
	
	public Player getOwnerOfReader() {
		return player;
	}

	public MapReader getMap(String key) {
		Object obj = map.get(key);
		if (obj instanceof Map) {
			return new MapReader(format, (Map<String, Object>) obj, player);
		}
		return null;
	}

	public String getString(String key) {
		return getString(key, null);
	}
	
	public String getString(String key, String def) {
		Object obj = get(key);
		if (obj instanceof String) {
			return (String) obj;
		}
		return def;
	}
	
	private Number getNumber(String key, Number def) {
		Object obj = get(key);
		if (obj instanceof String) {
			// Object is string, but maybe can be converted to number
			String s = (String) obj;
			try {
				Number number = Double.parseDouble(s);
				return number;
			} catch (NumberFormatException e) {
				// Object isn't number, try to use aritmetic
				try {
					Object ob = OperationParser.getFinalOperation(format, s).resolveFor(player);
					if (ob instanceof Number) {
						return (Number) ob;
					} else if (ob instanceof String) {
						Number number = Double.parseDouble((String) ob);
						return number;
					}
				} catch (NumberFormatException ex) {
					
				}
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
		Object obj = get(key);
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
			return Boolean.parseBoolean((String) obj);
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
		Object obj = get(key);
		if (!(obj instanceof List)) {
			return def;
		}
		List<String> newStrList = new ArrayList<>((List) obj);
		return newStrList.isEmpty() && def != null ? def : newStrList;
	}
	
	public List<ItemStack> getStackList(String key) {
		return getStackList(key, null);
	}
	
	public List<ItemStack> getStackList(String key, List<ItemStack> def){
		Object obj = get(key);
		if (!(obj instanceof List)) {
			return def;
		}
		List<ItemStack> newStrList = new ArrayList<>();
		for (Object ob : (List) obj) {
			if (ob instanceof ItemStack) {
				newStrList.add((ItemStack) ob);
			} else if (ob instanceof String) {
				newStrList.add(ShortStackParser.parseShortStack((String) ob));
			}
		}
		return newStrList.isEmpty() && def != null ? def : newStrList;
	}
	
	public ItemStack getItemStack(String key) {
		return getItemStack(key, null);
	}
	
	public ItemStack getItemStack(String key, ItemStack def) {
		Object obj = get(key);
		if (obj instanceof ItemStack) {
			return (ItemStack) obj;
		} else if (obj instanceof String) {
			return ShortStackParser.parseShortStack((String) obj);
		}
		return def;
	}
	
	public List<MapReader> getMapList(String key) {
		Object obj = map.get(key);
		List<MapReader> newMapList = new ArrayList<>();
		if (obj instanceof List) {
			List<Map<String, Object>> mapList = (List<Map<String, Object>>) obj;
			for (Map<String, Object> map : mapList) {
				newMapList.add(new MapReader(format, map, player));
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
			obj = format.processPlaceholders(player, (String) obj);
			if (((String) obj).startsWith("(cast to ItemStack)")) {
				obj = ShortStackParser.parseShortStack((String) obj);
			}
		}
		if (obj instanceof List) {
			List list = (List) obj;
			List<Object> nlist = new ArrayList<>();
			for (Object ob : list) {
				nlist.add(convert(ob));
			}
			obj = nlist;
		}
		if (obj instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) obj;
			Map<String, Object> nmap = new HashMap<>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				nmap.put(entry.getKey(), convert(entry.getValue()));
			}
			obj = nmap;
		}
		if (obj instanceof ItemStack) {
			ItemStack stack = (ItemStack) obj;
			if (stack.hasItemMeta()) {
				ItemMeta meta = stack.getItemMeta();
				if (meta.hasDisplayName()) {
					meta.setDisplayName(format.processPlaceholders(player, meta.getDisplayName()));
				}
				if (meta.hasLore()) {
					List<String> lore = new ArrayList<String>();
					for (String str : meta.getLore()) {
						lore.add(format.processPlaceholders(player, str));
					}
					meta.setLore(lore);
				}
				stack.setItemMeta(meta);
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
