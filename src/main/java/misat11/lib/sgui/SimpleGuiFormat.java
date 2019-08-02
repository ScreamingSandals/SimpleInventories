package misat11.lib.sgui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;

import misat11.lib.sgui.operations.OperationParser;
import misat11.lib.sgui.operations.conditions.Condition;
import misat11.lib.sgui.placeholders.PAPIPlaceholderParser;
import misat11.lib.sgui.placeholders.PermissionPlaceholderParser;
import misat11.lib.sgui.placeholders.PlaceholderConstantParser;
import misat11.lib.sgui.placeholders.PlaceholderParser;
import misat11.lib.sgui.placeholders.PlayerPlaceholderParser;
import misat11.lib.sgui.placeholders.WorldPlaceholderParser;

public class SimpleGuiFormat {
	
	private int items_on_row = 9; // TODO
	private int items_on_page = items_on_row * 4;

	private final List<List<Map<String, Object>>> data = new ArrayList<List<Map<String, Object>>>();
	private final List<ItemInfo> generatedData = new ArrayList<ItemInfo>();

	private int lastpos = 0;

	private ItemInfo previous = null;
	private Map<String, ItemInfo> ids = new HashMap<String, ItemInfo>();

	private boolean animationsEnabled = false;
	private Plugin pluginForRunnables = null;

	private boolean genericShopEnabled = false;
	private boolean genericShopPriceTypeRequired = false;

	private Map<String, PlaceholderParser> placeholders = new HashMap<>();

	// FROM CREATOR
	private final Map<ItemInfo, Map<Integer, List<ItemInfo>>> infoByAbsolutePosition = new HashMap<ItemInfo, Map<Integer, List<ItemInfo>>>();
	private final Map<ItemInfo, Integer> lastPageNumbers = new HashMap<ItemInfo, Integer>();
	private final String prefix;
	private ItemStack backItem, pageBackItem, pageForwardItem, cosmeticItem;
	
	public SimpleGuiFormat(Options options) {
		this(options.getPrefix(), options.getBackItem(), options.getPageBackItem(), options.getPageForwardItem(), options.getCosmeticItem());
		
		this.animationsEnabled = options.isAnimationsEnabled();
		this.pluginForRunnables = options.getAnimationPlugin();
		this.genericShopEnabled = options.isGenericShop();
		this.genericShopPriceTypeRequired = options.isGenericShopPriceTypeRequired();
		this.items_on_page = options.getRows() * 4;
	}

	public SimpleGuiFormat(String prefix, ItemStack backItem, ItemStack pageBackItem, ItemStack pageForwardItem,
			ItemStack cosmeticItem) {
		this.prefix = prefix;
		this.backItem = backItem.clone();
		this.pageBackItem = pageBackItem.clone();
		this.pageForwardItem = pageForwardItem.clone();
		this.cosmeticItem = cosmeticItem.clone();

		registerPlaceholder("player", new PlayerPlaceholderParser());
		registerPlaceholder("permission", new PermissionPlaceholderParser());
		registerPlaceholder("world", new WorldPlaceholderParser());
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			registerPlaceholder("papi", new PAPIPlaceholderParser());
		}
	}

	public void load(List<Map<String, Object>> data) {
		this.data.add(data);
	}

	public void load(String fileName) throws IOException, InvalidConfigurationException {
		load(new File(fileName), "data");
	}

	public void load(String fileName, String configPath) throws IOException, InvalidConfigurationException {
		load(new File(fileName), configPath);
	}

	public void loadFromDataFolder(File pluginDataFolder, String fileName)
			throws IOException, InvalidConfigurationException {
		load(new File(pluginDataFolder, fileName), "data");
	}

	public void loadFromDataFolder(File pluginDataFolder, String fileName, String configPath)
			throws IOException, InvalidConfigurationException {
		load(new File(pluginDataFolder, fileName), configPath);
	}

	public void load(File file) throws IOException, InvalidConfigurationException {
		load(file, "data");
	}

	public void load(File file, String configPath) throws IOException, InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		config = new YamlConfiguration();
		config.load(file);
		List<Map<String, Object>> data = (List<Map<String, Object>>) config.getList(configPath);
		this.data.add(data);
	}

	public void enableAnimations(Plugin plugin) {
		this.animationsEnabled = true;
		this.pluginForRunnables = plugin;
	}

	public void disableAnimations() {
		this.animationsEnabled = false;
	}

	public boolean isAnimationsEnabled() {
		return this.animationsEnabled && this.pluginForRunnables != null;
	}

	public void enableGenericShop(boolean priceTypeRequired) {
		this.genericShopEnabled = true;
		this.genericShopPriceTypeRequired = priceTypeRequired;
	}

	public void disableGenericShop() {
		this.genericShopEnabled = false;
	}

	public boolean isGenericShopEnabled() {
		return this.genericShopEnabled;
	}

	public boolean isPriceTypeRequired() {
		return this.genericShopPriceTypeRequired;
	}

	public Plugin getPluginForRunnables() {
		return this.pluginForRunnables;
	}

	public List<List<Map<String, Object>>> getData() {
		return this.data;
	}

	public boolean registerPlaceholder(String name, String value) {
		return registerPlaceholder(name, new PlaceholderConstantParser(value));
	}

	public boolean registerPlaceholder(String name, PlaceholderParser parser) {
		if (name.contains(".") || name.contains(":") || name.contains("%") || name.contains(" ")) {
			return false;
		}
		placeholders.put(name, parser);
		return true;
	}

	public String processPlaceholders(Player player, String text) {
		Pattern pat = Pattern.compile("%[^%]+%");
		Matcher matcher = pat.matcher(text);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String matched = matcher.group();
			matched = matcher.group().substring(1, matched.length() - 1);
			String[] args = matched.split("(?<!\\.)\\.(?!\\.)");
			String[] gargs = new String[args.length - 1];
			for (int i = 0; i < args.length; i++) {
				args[i] = args[i].replaceAll("\\.+", ".");
				if (i > 0) {
					gargs[i - 1] = args[i];
				}
			}
			String key = args[0];
			if (placeholders.containsKey(key)) {
				matcher.appendReplacement(sb, placeholders.get(key).processPlaceholder(key, player, gargs));
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public void generateData() {
		for (List<Map<String, Object>> list : data) {
			for (Map<String, Object> object : list) {
				lastpos = generateItem(null, object, lastpos);
			}
		}

		for (ItemInfo info : this.generatedData) {
			if (!infoByAbsolutePosition.containsKey(info.getParent())) {
				infoByAbsolutePosition.put(info.getParent(), new HashMap<Integer, List<ItemInfo>>());
			}
			int page = (info.getPosition() / items_on_page);
			Map<Integer, List<ItemInfo>> map = infoByAbsolutePosition.get(info.getParent());
			if (!map.containsKey(page)) {
				map.put(page, new ArrayList<>());
			}
			map.get(page).add(info);
			if (!lastPageNumbers.containsKey(info.getParent())) {
				lastPageNumbers.put(info.getParent(), page);
			} else {
				int lastPage = lastPageNumbers.get(info.getParent());
				if (page > lastPage) {
					lastPageNumbers.put(info.getParent(), page);
				}
			}
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
								inserted.lastpos = generateItem(inserted, itemObject, inserted.lastpos);
							}
						}
						return parent == inserted ? inserted.lastpos : lastpos;
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
						} else if ("increment".equalsIgnoreCase(cloneMethod)
								|| "increment-default".equalsIgnoreCase(cloneMethod)
								|| "increment-missing".equalsIgnoreCase(cloneMethod)) {
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
						for (Map.Entry<String, Object> entry : previous.getData().entrySet()) {
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
									if (val instanceof ItemStack) {
										val = ((ItemStack) val).clone();
									}
									object.put(entry.getKey(), val);
								}
							}
						}
					}
				} else if ("cosmetic".equalsIgnoreCase(clone)) {
					object.put("stack", new ItemStack(Material.AIR)); // Apply correct ItemStack in StaticGuiCreator
				} else if (clone.startsWith("§")) {
					ItemInfo cloned = ids.get(clone.substring(1));
					if (cloned != null) {
						for (Map.Entry<String, Object> entry : cloned.getData().entrySet()) {
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
									if (val instanceof ItemStack) {
										val = ((ItemStack) val).clone();
									}
									object.put(entry.getKey(), val);
								}
							}
						}
					}
				}
			}
		}
		ItemStack stack = new ItemStack(Material.AIR);
		if (object.containsKey("stack")) {
			Object st = object.get("stack");
			if (st instanceof ItemStack) {
				stack = (ItemStack) st;
			} else if (st instanceof String) {
				stack = ShortStackParser.parseShortStack((String) st);
			}
		}
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
			positionC += (items_on_page - (positionC % items_on_page));
		}
		if (object.containsKey("row")) {
			positionC = positionC - (positionC % items_on_page) + (((int) object.get("row") - 1) * items_on_row) + (positionC % items_on_row);
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

			positionC = (positionC - (positionC % items_on_row)) + column;
		}
		if (linebreakC == 1 || linebreakC == 3) {
			positionC += (items_on_row - (positionC % items_on_row));
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
						Property pr = new Property(this,
								propertyMap.containsKey("name") ? (String) propertyMap.get("name") : null, propertyMap);
						properties.add(pr);
					}
				}
			}
		}
		List<ItemStack> animation = null;
		if (object.containsKey("animation")) {
			List<Object> anim = (List<Object>) object.get("animation");
			animation = new ArrayList<>();
			for (Object ani : anim) {
				if (ani instanceof ItemStack) {
					animation.add((ItemStack) ani);
				} else if (ani instanceof String) {
					animation.add(ShortStackParser.parseShortStack((String) ani));
				}
			}
		}
		Map<Condition, Map<String, Object>> conditions = new HashMap<>();
		if (object.containsKey("conditions")) {
			List<Map<String, Object>> configuredConditions = (List<Map<String, Object>>) object.get("conditions");
			for (Map<String, Object> entry : configuredConditions) {
				String f_if = (String) entry.get("if");
				if (entry.containsKey("then")) {
					Condition f_cond_then = OperationParser.getFinalCondition(this, f_if);
					Map<String, Object> f_then = (Map<String, Object>) entry.get("then");
					conditions.put(f_cond_then, f_then);
				}
				if (entry.containsKey("else")) {
					Condition f_cond_else = OperationParser.getFinalNegation(this, f_if);
					Map<String, Object> f_else = (Map<String, Object>) entry.get("else");
					conditions.put(f_cond_else, f_else);

				}
			}
		}

		Object f_visible = object.getOrDefault("visible", true);
		boolean visible = true;
		if (f_visible instanceof Boolean) {
			visible = (Boolean) f_visible;
		} else if (f_visible instanceof String) {
			Condition f_visible_cond = OperationParser.getFinalNegation(this, (String) f_visible);
			Map<String, Object> f_visible_map = new HashMap<String, Object>();
			f_visible_map.put("visible", false);
			conditions.put(f_visible_cond, f_visible_map);
		}

		Object f_disabled = object.getOrDefault("disabled", false);
		boolean disabled = false;
		if (f_disabled instanceof Boolean) {
			disabled = (Boolean) f_disabled;
		} else if (f_disabled instanceof String) {
			Condition f_disabled_cond = OperationParser.getFinalNegation(this, (String) f_disabled);
			Map<String, Object> f_disabled_map = new HashMap<String, Object>();
			f_disabled_map.put("disabled", true);
			conditions.put(f_disabled_cond, f_disabled_map);
		}

		ItemInfo info = new ItemInfo(this, parent, stack.clone(), positionC, visible, disabled, id, properties, object,
				animation, conditions);
		if (object.containsKey("items")) {
			List<Map<String, Object>> items = (List<Map<String, Object>>) object.get("items");
			for (Map<String, Object> itemObject : items) {
				info.lastpos = generateItem(info, itemObject, info.lastpos);
			}
		} else if (object.containsKey("book")) {
			List<Map<String, Object>> pages = (List<Map<String, Object>>) object.get("book");
			ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
			BookMeta meta = (BookMeta) book.getItemMeta();
			for (Map<String, Object> page : pages) {
				String text = (String) page.get("text");
				/*
				 * TODO we should just save text and generate book when player clicked (and will
				 * be able to use placeholders here)
				 */
				meta.addPage(text);
			}
			book.setItemMeta(meta);
			info.setBook(book);
		}
		generatedData.add(info);
		previous = info;
		if (id != null) {
			ids.put(id, info);
		}
		int nextPosition = positionC;
		if (pagebreakC >= 2) {
			nextPosition += (items_on_page - (nextPosition % items_on_page));
		}
		if (linebreakC >= 2) {
			nextPosition += (items_on_row - (nextPosition % items_on_row));
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

	public Map<ItemInfo, Map<Integer, List<ItemInfo>>> getDynamicInfo() {
		return this.infoByAbsolutePosition;
	}

	public Map<ItemInfo, Integer> getLastPageNumbers() {
		return this.lastPageNumbers;
	}

	public String getPrefix() {
		return prefix;
	}

	public ItemStack getBackItem() {
		return backItem;
	}

	public ItemStack getPageBackItem() {
		return pageBackItem;
	}

	public ItemStack getPageForwardItem() {
		return pageForwardItem;
	}

	public ItemStack getCosmeticItem() {
		return cosmeticItem;
	}
	
	public int getItemsOnPage() {
		return items_on_page;
	}
	
	public int getItemsOnRow() {
		return items_on_row;
	}

	public void openForPlayer(Player player) {
		new GuiHolder(player, this, null, 0);
	}
}
