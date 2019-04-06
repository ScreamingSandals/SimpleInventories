package misat11.lib.sgui.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleGuiFormat {

	public static final int ITEMS_ON_PAGE = 36;

	public static final int ITEMS_ON_ROW = 9;

	private final List<Map<String, Object>> data;
	private final List<ItemInfo> generatedData = new ArrayList<ItemInfo>();

	private int lastpos = 0;

	private ItemInfo previous = null;
	private Map<String, ItemInfo> ids = new HashMap<String, ItemInfo>();

	public SimpleGuiFormat(List<Map<String, Object>> data) {
		this.data = data;
	}

	public List<Map<String, Object>> getData() {
		return this.data;
	}

	public List<ItemInfo> getPreparedData() {
		return this.generatedData;
	}

	public void generateData() {
		for (Map<String, Object> object : data) {
			lastpos = generateItem(null, object, lastpos);
		}
	}

	private int generateItem(ItemInfo parent, Map<String, Object> object, int lastpos) {
		if (object.containsKey("insert")) {
			Object obj = object.get("insert");
			if (obj instanceof String && obj != null) {
				String insert = (String) obj;
				if ("main".equalsIgnoreCase(insert)) {
					if (object.containsKey("items")) {
						List<Map<String, Object>> items = (List<Map<String, Object>>) object.get("items");
						for (Map<String, Object> itemObject : items) {
							this.lastpos = generateItem(null, itemObject, this.lastpos);
						}
					}
					return parent == null ? this.lastpos : lastpos;
				} else if (insert.startsWith("§")) {
					ItemInfo inserted = ids.get(insert.substring(1));
					if (inserted != null) {
						if (object.containsKey("items")) {
							List<Map<String, Object>> items = (List<Map<String, Object>>) object.get("items");
							for (Map<String, Object> itemObject : items) {
								inserted.getData().lastpos = generateItem(inserted, itemObject, inserted.getData().lastpos);
							}
						}
						return parent == inserted ? inserted.getData().lastpos : lastpos;
					}
				}
			}
		}
		
		if (object.containsKey("clone")) {
			Object obj = object.get("clone");
			if (obj instanceof String && obj != null) {
				
				boolean cloneOverride = false;
				boolean cloneListIncrement = false;
				if (object.containsKey("clone-method")) {
					Object obj2 = object.get("clone-method");
					if (obj2 instanceof String && obj != null) {
						String cloneMethod = (String) obj2;
						if ("default".equalsIgnoreCase(cloneMethod) || "missing".equalsIgnoreCase(cloneMethod)) {
							// no changes
						} else if ("override".equalsIgnoreCase(cloneMethod)) {
							cloneOverride = true;
						} else if ("increment".equalsIgnoreCase(cloneMethod) || "increment-default".equalsIgnoreCase(cloneMethod) || "increment-missing".equalsIgnoreCase(cloneMethod)) {
							cloneListIncrement = true;
						} else if ("increment-override".equalsIgnoreCase(cloneMethod)) {
							cloneOverride = true;
							cloneListIncrement = true;
						}
					}
				}
				
				String clone = (String) obj;
				if ("previous".equalsIgnoreCase(clone)) {
					if (previous != null) {
						for (Map.Entry<String, Object> entry : previous.getData().getData().entrySet()) {
							if (!isPositionProperty(entry.getKey())) { 
								// Clone just non exists keys and without position
								Object val = entry.getValue();
								if (val instanceof List) {
									boolean containsObjectList = object.containsKey(entry.getKey());
									if (containsObjectList) {
										if (cloneListIncrement) {
											Object originalList = object.get(entry.getKey());
											List<?> newList = new ArrayList<>();
											newList.addAll((List) val);
											if (originalList instanceof List) {
												newList.addAll((List) originalList);
											}
											object.put(entry.getKey(), newList);
										} else if (cloneOverride) {
											object.put(entry.getKey(), val);
										}
									} else {
										object.put(entry.getKey(), val);
									}
								} else if (!object.containsKey(entry.getKey()) || cloneOverride) {
									object.put(entry.getKey(), val);
								}
							}
						}
					}
				} else if (clone.startsWith("§")) {
					ItemInfo cloned = ids.get(clone.substring(1));
					if (cloned != null) {
						for (Map.Entry<String, Object> entry : cloned.getData().getData().entrySet()) {
							if (!isPositionProperty(entry.getKey())) { 
								// Clone just non exists keys and without position
								Object val = entry.getValue();
								if (val instanceof List) {
									boolean containsObjectList = object.containsKey(entry.getKey());
									if (containsObjectList) {
										if (cloneListIncrement) {
											Object originalList = object.get(entry.getKey());
											List<?> newList = new ArrayList<>();
											newList.addAll((List) val);
											if (originalList instanceof List) {
												newList.addAll((List) originalList);
											}
											object.put(entry.getKey(), newList);
										} else if (cloneOverride) {
											object.put(entry.getKey(), val);
										}
									} else {
										object.put(entry.getKey(), val);
									}
								} else if (!object.containsKey(entry.getKey()) || cloneOverride) {
									object.put(entry.getKey(), val);
								}
							}
						}
					}
				}
			}
		}
		ItemStack stack = object.containsKey("stack") ? new ItemStack((Map<String, Object>)object.get("stack")) : new ItemStack(new HashMap<>());
		int positionC = lastpos;
		int linebreakC = 0;
		int pagebreakC = 0;
		if (object.containsKey("linebreak")) {
			String lnBreak = (String) object.get("linebreak");
			if ("before".equalsIgnoreCase(lnBreak)) {
				linebreakC = 1;
			} else if ("after".equalsIgnoreCase(lnBreak)) {
				linebreakC = 2;
			} else if ("both".equalsIgnoreCase(lnBreak)) {
				linebreakC = 3;
			}
		}
		if (object.containsKey("pagebreak")) {
			String pgBreak = (String) object.get("pagebreak");
			if ("before".equalsIgnoreCase(pgBreak)) {
				pagebreakC = 1;
			} else if ("after".equalsIgnoreCase(pgBreak)) {
				pagebreakC = 2;
			} else if ("both".equalsIgnoreCase(pgBreak)) {
				pagebreakC = 3;
			}
		}
		if (pagebreakC == 1 || pagebreakC == 3) {
			positionC += (ITEMS_ON_PAGE - (positionC % ITEMS_ON_PAGE));
		}
		if (object.containsKey("row")) {
			positionC = positionC - (positionC % ITEMS_ON_PAGE) + (((int) object.get("row") - 1) * 9) + (positionC % 9);
		}
		if (object.containsKey("column")) {
			Object cl = object.get("column");
			int column = 0;
			if ("left".equals(cl) || "first".equals(cl)) {
				column = 0;
			} else if ("middle".equals(cl) || "center".equals(cl)) {
				column = 4;
			} else if ("right".equals(cl) || "last".equals(cl)) {
				column = 8;
			} else {
				column = (int) cl;
			}

			positionC = (positionC - (positionC % 9)) + column;
		}
		if (linebreakC == 1 || linebreakC == 3) {
			positionC += (9 - (positionC % 9));
		}
		if (object.containsKey("skip")) {
			positionC += (int) object.get("skip");
		}
		if (object.containsKey("absolute")) {
			positionC = (int) object.get("absolute");
		}
		String id = object.containsKey("id") ? (String) object.get("id") : null;
		List<Property> properties = new ArrayList<Property>();
		if (object.containsKey("properties")) {
			Object prop = object.get("properties");
			if (properties instanceof List) {
				List<Object> propertiesList = (List<Object>) prop;
				for (Object obj : propertiesList) {
					if (obj instanceof Map) {
						Map<String, Object> propertyMap = (Map<String, Object>) obj;
						Property pr = new Property(
								propertyMap.containsKey("name") ? (String) propertyMap.get("name") : null, propertyMap);
						properties.add(pr);
					}
				}
			}
		}
		ItemData iData = new ItemData(id, properties, object);
		ItemInfo info = new ItemInfo(parent, stack.clone(), positionC, iData);
		if (object.containsKey("items")) {
			info.setHasChilds(true);
			List<Map<String, Object>> items = (List<Map<String, Object>>) object.get("items");
			for (Map<String, Object> itemObject : items) {
				iData.lastpos = generateItem(info, itemObject, iData.lastpos);
			}
		}
		generatedData.add(info);
		previous = info;
		if (id != null) {
			ids.put(id, info);
		}
		int nextPosition = positionC;
		if (pagebreakC >= 2) {
			nextPosition += (ITEMS_ON_PAGE - (nextPosition % ITEMS_ON_PAGE));
		}
		if (linebreakC >= 2) {
			nextPosition += (9 - (nextPosition % 9));
		}
		if (pagebreakC < 2 && linebreakC < 2) {
			nextPosition++;
		}
		lastpos = nextPosition;
		return lastpos;
	}

	private boolean isPositionProperty(String key) {
		return key.equals("row") || key.equals("column") || key.equals("skip") || key.equals("linebreak")
				|| key.equals("pagebreak");
	}
}
