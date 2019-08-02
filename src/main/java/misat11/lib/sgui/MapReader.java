package misat11.lib.sgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

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
		Object obj = get(key);
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
					// Object isn't number
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
			return Boolean.parseBoolean(format.processPlaceholders(player, (String) obj));
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
		List<String> newStrList = new ArrayList<>();
		if (obj instanceof List) {
			List<String> stringList = (List<String>) obj;
			for (String s : stringList){
				newStrList.add(format.processPlaceholders(player, s));
			}
		}
		return newStrList.isEmpty() && def != null ? def : newStrList;
	}
	
	public List<MapReader> getMapList(String key) {
		Object obj = get(key);
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
		Object obj = map.get(key);
		if (obj instanceof String) {
			obj = format.processPlaceholders(player, (String) obj);
		}
		return obj;
	}
	
	@Deprecated
	public Map<String, Object> getOriginalData() {
		return map;
	}
}
