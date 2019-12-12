package misat11.lib.sgui;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;

import misat11.lib.sgui.builder.FormatBuilder;
import misat11.lib.sgui.loaders.Loader;
import misat11.lib.sgui.loaders.LoaderRegister;
import misat11.lib.sgui.operations.OperationParser;
import misat11.lib.sgui.operations.conditions.Condition;
import misat11.lib.sgui.placeholders.AdvancedPlaceholderParser;
import misat11.lib.sgui.placeholders.PAPIPlaceholderParser;
import misat11.lib.sgui.placeholders.PermissionPlaceholderParser;
import misat11.lib.sgui.placeholders.PlaceholderConstantParser;
import misat11.lib.sgui.placeholders.PlaceholderParser;
import misat11.lib.sgui.placeholders.PlayerPlaceholderParser;
import misat11.lib.sgui.placeholders.ThisPlaceholderParser;
import misat11.lib.sgui.placeholders.WorldPlaceholderParser;

public class SimpleGuiFormat {

	private int items_on_row = Options.ITEMS_ON_ROW;
	private int rows = Options.ROWS;
	private int render_start_offset = Options.RENDER_OFFSET;
	private int render_actual_rows = Options.RENDER_ACTUAL_ROWS;
	private int render_header_row_start = Options.RENDER_HEADER_START;
	private int render_footer_row_start = Options.RENDER_FOOTER_START;

	private final List<Origin> data = new ArrayList<Origin>();
	private final List<ItemInfo> generatedData = new ArrayList<ItemInfo>();

	private int lastpos = 0;

	private ItemInfo previous = null;
	private Map<String, ItemInfo> ids = new HashMap<String, ItemInfo>();

	private boolean animationsEnabled = false;
	private Plugin pluginForRunnables = null;

	private boolean genericShopEnabled = false;
	private boolean genericShopPriceTypeRequired = false;

	private boolean showPageNumber = true;
	private boolean allowAccessToConsole = false;

	private Map<String, PlaceholderParser> placeholders = new HashMap<>();
	private Map<String, AdvancedPlaceholderParser> advancedPlaceholders = new HashMap<>();

	// FROM CREATOR
	private final Map<ItemInfo, Map<Integer, List<ItemInfo>>> infoByAbsolutePosition = new HashMap<ItemInfo, Map<Integer, List<ItemInfo>>>();
	private final Map<ItemInfo, Integer> lastPageNumbers = new HashMap<ItemInfo, Integer>();
	private final String prefix;
	private ItemStack backItem, pageBackItem, pageForwardItem, cosmeticItem;
	private final List<Map.Entry<String, List<Object>>> insertingBuffer = new ArrayList<>();
	
	private static final List<String> POSITION_PROPERTIES = Arrays.asList("row", "column", "skip", "linebreak", "pagebreak", "absolute");

	public SimpleGuiFormat(Options options) {
		this.prefix = options.getPrefix();
		this.backItem = options.getBackItem().clone();
		this.pageBackItem = options.getPageBackItem().clone();
		this.pageForwardItem = options.getPageForwardItem().clone();
		this.cosmeticItem = options.getCosmeticItem().clone();
		this.animationsEnabled = options.isAnimationsEnabled();
		this.pluginForRunnables = options.getAnimationPlugin();
		this.genericShopEnabled = options.isGenericShop();
		this.genericShopPriceTypeRequired = options.isGenericShopPriceTypeRequired();
		this.rows = options.getRows();
		this.items_on_row = options.getItems_on_row();
		this.render_actual_rows = options.getRender_actual_rows();
		this.render_header_row_start = options.getRender_header_start();
		this.render_footer_row_start = options.getRender_footer_start();
		this.render_start_offset = options.getRender_offset();
		this.showPageNumber = options.isShowPageNumber();
		this.allowAccessToConsole = options.isAllowAccessToConsole();

		initPlaceholders();

		this.placeholders.putAll(options.getPlaceholders());
		this.advancedPlaceholders.putAll(options.getAdvancedPlaceholders());
	}

	/*
	 * Use new constructor!
	 * 
	 * @deprecated Old constructor
	 * 
	 * @see #SimpleGuiFormat(Options)
	 */
	@Deprecated
	public SimpleGuiFormat(String prefix, ItemStack backItem, ItemStack pageBackItem, ItemStack pageForwardItem,
		ItemStack cosmeticItem) {
		this.prefix = prefix;
		this.backItem = backItem.clone();
		this.pageBackItem = pageBackItem.clone();
		this.pageForwardItem = pageForwardItem.clone();
		this.cosmeticItem = cosmeticItem.clone();

		initPlaceholders();
	}

	private void initPlaceholders() {
		/* STANDARD PLACEHOLDERS */
		placeholders.put("player", new PlayerPlaceholderParser());
		placeholders.put("permission", new PermissionPlaceholderParser());
		placeholders.put("world", new WorldPlaceholderParser());
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			placeholders.put("papi", new PAPIPlaceholderParser());
		}

		/* ADVANCED PLACEHOLDERS */
		AdvancedPlaceholderParser thisParser = new ThisPlaceholderParser();
		advancedPlaceholders.put("this", thisParser);
		advancedPlaceholders.put("self", thisParser);
	}

	public SimpleGuiFormat carrigeReturn() {
		this.lastpos -= (lastpos - items_on_row) % items_on_row;
		return this;
	}

	public SimpleGuiFormat lineBreak() {
		this.lastpos += (items_on_row - (lastpos % items_on_row));
		return this;
	}

	public SimpleGuiFormat pageBreak() {
		this.lastpos += (getItemsOnPage() - (lastpos % getItemsOnPage()));
		return this;
	}

	public SimpleGuiFormat jump(int number) {
		this.lastpos += number;
		if (this.lastpos < 0) {
			this.lastpos = 0;
		}
		return this;
	}

	public SimpleGuiFormat absolute(int position) {
		this.lastpos = position;
		return this;
	}

	public SimpleGuiFormat column(Column column) {
		return column(column.convert(items_on_row));
	}

	public SimpleGuiFormat column(int column) {
		this.lastpos = (this.lastpos - (this.lastpos % items_on_row)) + column;
		return this;
	}

	public SimpleGuiFormat row(int row) {
		this.lastpos = this.lastpos - (this.lastpos % getItemsOnPage()) + ((row - 1) * items_on_row)
			+ (this.lastpos % items_on_row);
		return this;
	}

	public SimpleGuiFormat clearInput() {
		this.data.clear();
		this.previous = null;
		return this;
	}

	public SimpleGuiFormat purgeData() {
		this.clearInput();
		this.ids.clear();
		this.generatedData.clear();
		this.lastpos = 0;
		this.infoByAbsolutePosition.clear();
		this.lastPageNumbers.clear();
		this.insertingBuffer.clear();
		return this;
	}

	public List<Object> cloneCurrentInput() {
		return new ArrayList<>(data);
	}

	public SimpleGuiFormat load(List<Object> data) {
		this.data.add(new Origin(data));
		return this;
	}

	public SimpleGuiFormat load(Origin origin) {
		this.data.add(origin);
		return this;
	}

	public SimpleGuiFormat load(FormatBuilder builder) {
		this.data.add(new Origin(builder, builder.getResult()));
		return this;
	}

	public SimpleGuiFormat load(String fileName) throws Exception {
		return load(new File(fileName), "data");
	}

	public SimpleGuiFormat load(String fileName, String configPath) throws Exception {
		return load(new File(fileName), configPath);
	}

	public SimpleGuiFormat loadFromDataFolder(File pluginDataFolder, String fileName) throws Exception {
		return load(new File(pluginDataFolder, fileName), "data");
	}

	public SimpleGuiFormat loadFromDataFolder(File pluginDataFolder, String fileName, String configPath)
		throws Exception {
		return load(new File(pluginDataFolder, fileName), configPath);
	}

	public SimpleGuiFormat load(File file) throws Exception {
		return load(file, "data");
	}

	public SimpleGuiFormat load(File file, String configPath) throws Exception {
		return load(file, configPath, LoaderRegister.getLoader(file));
	}

	public SimpleGuiFormat load(File file, Loader loader) throws Exception {
		return load(file, "data", loader);
	}

	public SimpleGuiFormat load(File file, String configPath, Loader loader) throws Exception {
		this.data.add(loader.readData(file, configPath));
		return this;
	}

	@Deprecated
	public SimpleGuiFormat enableAnimations(Plugin plugin) {
		this.animationsEnabled = true;
		this.pluginForRunnables = plugin;
		return this;
	}

	@Deprecated
	public SimpleGuiFormat disableAnimations() {
		this.animationsEnabled = false;
		return this;
	}

	public boolean isAnimationsEnabled() {
		return this.animationsEnabled && this.pluginForRunnables != null;
	}

	@Deprecated
	public SimpleGuiFormat enableGenericShop(boolean priceTypeRequired) {
		this.genericShopEnabled = true;
		this.genericShopPriceTypeRequired = priceTypeRequired;
		return this;
	}

	@Deprecated
	public SimpleGuiFormat disableGenericShop() {
		this.genericShopEnabled = false;
		return this;
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

	public List<Origin> getData() {
		return this.data;
	}

	@Deprecated
	public boolean registerPlaceholder(String name, String value) {
		return registerPlaceholder(name, new PlaceholderConstantParser(value));
	}

	@Deprecated
	public boolean registerPlaceholder(String name, PlaceholderParser parser) {
		if (name.contains(".") || name.contains(":") || name.contains("%") || name.contains(" ")) {
			return false;
		}
		placeholders.put(name, parser);
		return true;
	}

	@Deprecated
	public boolean registerPlaceholder(String name, AdvancedPlaceholderParser parser) {
		if (name.contains(".") || name.contains(":") || name.contains("%") || name.contains(" ")) {
			return false;
		}
		advancedPlaceholders.put(name, parser);
		return true;
	}

	@Deprecated
	public String processPlaceholders(Player player, String text) {
		return processPlaceholders(player, text, null);
	}

	public String processPlaceholders(Player player, String text, PlayerItemInfo info) {
		char[] characters = text.toCharArray();
		int lastEscapeIndex = -2;
		String buf = "";
		for (int i = 0; i < characters.length; i++) {
			char c = characters[i];
			if (c == '{' && lastEscapeIndex != (i - 1)) {
				int bracketEnd = characters.length;
				int alastEscapeIndex = -2;
				String bracketBuf = "";
				for (int j = i + 1; j < characters.length; j++) {
					char cc = characters[j];
					if (cc == '\\' && alastEscapeIndex != (j - 1)) {
						alastEscapeIndex = j;
					} else if (cc == '}' && alastEscapeIndex != (j - 1)) {
						bracketEnd = j;
						break;
					} else {
						bracketBuf += cc;
					}
				}
				i = bracketEnd;
				buf += String
					.valueOf((Object) OperationParser.getFinalOperation(this, bracketBuf).resolveFor(player, info));
			} else if (c == '\\' && lastEscapeIndex != (i - 1)) {
				lastEscapeIndex = i;
			} else {
				buf += c;
			}
		}

		text = buf;

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
			if (advancedPlaceholders.containsKey(key) && info != null) {
				matcher.appendReplacement(sb,
					advancedPlaceholders.get(key).processPlaceholder(key, player, info, gargs));
			} else if (placeholders.containsKey(key)) {
				matcher.appendReplacement(sb, placeholders.get(key).processPlaceholder(key, player, gargs));
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public SimpleGuiFormat generateData() {
		for (Origin origin : data) {
			for (Object object : origin.getContent()) {
				lastpos = generateItem(null, object, lastpos, origin);
			}
		}

		for (ItemInfo info : this.generatedData) {
			if (!info.isWritten()) {
				continue;
			}
			if (!infoByAbsolutePosition.containsKey(info.getParent())) {
				infoByAbsolutePosition.put(info.getParent(), new HashMap<Integer, List<ItemInfo>>());
			}
			int page = (info.getPosition() / getItemsOnPage());
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
		return this;
	}

	private int generateItem(ItemInfo parent, Object original, int lastpos, Origin origin) {
		Map<String, Object> object = new HashMap<>();
		if (original instanceof Map) {
			object.putAll((Map<String, Object>) original);
		} else {
			String obj = original.toString();
			char[] characters = obj.toCharArray();
			int lastEscapeIndex = -2;
			boolean buildingString = false;
			int building = -1;
			String buf = "";
			for (int i = 0; i < characters.length; i++) {
				char c = characters[i];
				if (building == -1) {
					if (c == '@') {
						object.put("include", obj.substring(1));
						break;
					}
					building = 0;
				}
				if (c == '\\' && lastEscapeIndex != (i - 1)) {
					lastEscapeIndex = i;
				} else if (buildingString) {
					if ((c == '"' || c == '\'') && lastEscapeIndex != (i - 1)) {
						buildingString = false;
					} else {
						buf += c;
					}
				} else if (building < 2 && buf.endsWith("repeat")) {
					buf = buf.substring(0, buf.length() - 6).trim();
					if (building == 0) {
						if (buf.equalsIgnoreCase("cosmetic")) {
							object.put("stack", cosmeticItem.clone());
						} else {
							object.put("stack", buf);
						}
					} else {
						object.put("price", buf);
					}
					building = 2;
					buf = "";
				} else if (building == 0 && buf.endsWith("for")) {
					building = 1;
					buf = buf.substring(0, buf.length() - 3).trim();
					if (buf.equalsIgnoreCase("cosmetic")) {
						object.put("stack", cosmeticItem.clone());
					} else {
						object.put("stack", buf);
					}
					buf = "";
				} else if ((c == '"' || c == '\'') && lastEscapeIndex != (i - 1)) {
					buildingString = true;
					lastEscapeIndex = i;
				} else {
					buf += c;
				}
			}
			if (building == 0) {
				buf = buf.trim();
				if (buf.equalsIgnoreCase("cosmetic")) {
					object.put("stack", cosmeticItem.clone());
				} else {
					object.put("stack", buf);
				}
			} else if (building == 1) {
				object.put("price", buf.trim());
			} else if (building == 2) {
				try {
					object.put("times", Integer.parseInt(buf.trim()));
				} catch (Throwable t) {
				}
			}
		}
		
		/* In future there will be better way how to do this! we don't recommended to use hacks if there's another way*/
		if (object.containsKey("guihack")) {
			String hack = object.get("guihack").toString();
			if (hack.equalsIgnoreCase("noheader")) {
				render_header_row_start = 55;
				render_start_offset = 0;
				if (rows < 6) {
					rows++;
				}
			} else if (hack.equalsIgnoreCase("nofooter")) {
				render_footer_row_start = 55;
				if (rows < 6) {
					rows++;
				}
			} else if (hack.equalsIgnoreCase("noheaderfooter")) {
				render_header_row_start = 55;
				render_footer_row_start = 55;
				if (rows < 5) {
					rows += 2;
				} else if (rows < 6) {
					rows++;
				}
			}
			return lastpos;
		}
		
		if (object.containsKey("include")) {
			// [loader]:[data]@[path]
			// Example: yml:shop3@shops.yml
			// Another example: csv@shop.csv
			
			String path = object.get("include").toString();
			String data = "data";
			String loaderType = null;
			
			String[] splitByAt = path.split("@", 2);
			if (splitByAt.length == 2) {
				loaderType = splitByAt[0];
				path = splitByAt[1];
				String[] second_split = loaderType.split(":", 2);
				if (second_split.length == 2) {
					loaderType = second_split[0];
					data = second_split[1];
				}
			}
			if (loaderType != null) {
				loaderType = loaderType.trim();
			}
			data = data.trim();
			
			File file = new File(path);
			try {
				Loader loader;
				if (loaderType == null || "".equals(loaderType)) {
					loader = LoaderRegister.getLoader(file);
				} else {
					loader = LoaderRegister.getLoader(loaderType);
				}
				Origin or = loader.readData(file, data);
				List<Object> items = or.getContent();
				for (Object item : items) {
					lastpos = generateItem(parent, item, lastpos, or);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return lastpos;
		}

		if (object.containsKey("insert")) {
			Object obj = object.get("insert");
			if (obj instanceof String) {
				String insert = (String) obj;
				if ("main".equalsIgnoreCase(insert)) {
					if (object.containsKey("items")) {
						List<Object> items = (List<Object>) object.get("items");
						for (Object itemObject : items) {
							this.lastpos = generateItem(null, itemObject, this.lastpos, origin);
						}
					}
					return parent == null ? this.lastpos : lastpos;
				} else if (insert.startsWith("§") || insert.startsWith("$")) {
					ItemInfo inserted = ids.get(insert.substring(1));
					if (object.containsKey("items")) {
						List<Object> items = (List<Object>) object.get("items");
						if (inserted != null) {
							for (Object itemObject : items) {
								inserted.lastpos = generateItem(inserted, itemObject, inserted.lastpos, origin);
							}
							return parent == inserted ? inserted.lastpos : lastpos;
						} else {
							insertingBuffer.add(new AbstractMap.SimpleEntry<String, List<Object>>(
								insert.substring(1), items));
							return lastpos;
						}
					}

				}
			} else if (obj instanceof List) {
				boolean yes_main = false;
				boolean yes_inserted = false;
				for (Object ob : (List<Object>) obj) {
					if (ob instanceof String) {
						String insert = (String) ob;
						if ("main".equalsIgnoreCase(insert)) {
							if (object.containsKey("items")) {
								List<Object> items = (List<Object>) object.get("items");
								for (Object itemObject : items) {
									this.lastpos = generateItem(null, itemObject, this.lastpos, origin);
								}
							}
							if (!yes_main) {
								yes_main = parent == null;
							}
						} else if (insert.startsWith("§") || insert.startsWith("$")) {
							ItemInfo inserted = ids.get(insert.substring(1));
							if (object.containsKey("items")) {
								List<Object> items = (List<Object>) object.get("items");
								if (inserted != null) {
									for (Object itemObject : items) {
										inserted.lastpos = generateItem(inserted, itemObject, inserted.lastpos, origin);
									}
									if (!yes_inserted) {
										yes_inserted = parent == inserted;
									}
								} else {
									insertingBuffer.add(new AbstractMap.SimpleEntry<String, List<Object>>(
										insert.substring(1), items));
								}
							}
						}
					}
				}
				return yes_main ? this.lastpos : yes_inserted ? parent.lastpos : lastpos;
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
					object.put("stack", this.cosmeticItem.clone());
				} else if (clone.startsWith("§") || clone.startsWith("$")) {
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
			} else if (st instanceof Map) {
				stack = SguiStackParser.parseSguiStack((Map<String, Object>) st);
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
			positionC += (getItemsOnPage() - (positionC % getItemsOnPage()));
		}
		if (object.containsKey("row")) {
			positionC = positionC - (positionC % getItemsOnPage()) + (((int) object.get("row") - 1) * items_on_row)
				+ (positionC % items_on_row);
		}
		if (object.containsKey("column")) {
			Object cl = object.get("column");
			int column = 0;
			if ("left".equals(cl) || "first".equals(cl)) {
				column = 0;
			} else if ("middle".equals(cl) || "center".equals(cl)) {
				column = items_on_row / 2;
			} else if ("right".equals(cl) || "last".equals(cl)) {
				column = items_on_row - 1;
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
			if (prop instanceof List) {
				List<Object> propertiesList = (List<Object>) prop;
				for (Object obj : propertiesList) {
					if (obj instanceof Map) {
						Map<String, Object> propertyMap = (Map<String, Object>) obj;
						Property pr = new Property(this,
							propertyMap.containsKey("name") ? (String) propertyMap.get("name") : null, propertyMap);
						properties.add(pr);
					} else if (obj instanceof String) {
						properties.add(new Property(this, (String) obj, new HashMap<String, Object>() {
							{
								put("name", (String) obj);
							}
						}));
					}
				}
			} else if (prop instanceof String) {
				properties.add(new Property(this, (String) prop, new HashMap<String, Object>() {
					{
						put("name", (String) prop);
					}
				}));
			}
		}
		List<ItemStack> animation = null;
		if (object.containsKey("animation")) {
			List<Object> anim = (List<Object>) object.get("animation");
			animation = new ArrayList<>();
			for (Object ani : anim) {
				if (ani instanceof ItemStack) {
					animation.add((ItemStack) ani);
				} else if (ani instanceof Map) {
					stack = SguiStackParser.parseSguiStack((Map<String, Object>) ani);
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

		if (object.containsKey("price") && !object.containsKey("price-type")) {
			Object f_price = object.get("price");
			String price = f_price.toString().trim();
			int index = price.toLowerCase().indexOf("of");
			if (index > 0 && price.length() > (index + 2)) {
				try {
					double pr = Double.parseDouble(price.substring(0, index).trim());
					String price_type = price.substring(index + 2).trim();

					object.put("price", pr);
					object.put("price-type", price_type);
				} catch (Throwable t) {
				}
			} else {
				String pr = "";
				String price_type = "";
				for (int i = 0; i < price.length(); i++) {
					char c = price.charAt(i);
					if (Character.isDigit(c) || c == '.') {
						pr += c;
					} else {
						price_type = price.substring(i).trim();
						break;
					}
				}
				try {
					if (!pr.isEmpty()) {
						object.put("price", Double.parseDouble(pr));
						object.put("price-type", price_type);
					}
				} catch (Throwable t) {
				}
			}
		}

		boolean write = object.containsKey("write") ? Boolean.parseBoolean("write") : true;
		ItemInfo info = new ItemInfo(this, parent, stack.clone(), positionC, visible, disabled, id, properties, object,
			animation, conditions, origin, write);
		if (id != null && !insertingBuffer.isEmpty()) {
			for (Map.Entry<String, List<Object>> entry : new ArrayList<>(insertingBuffer)) {
				if (entry.getKey().equals(id)) {
					insertingBuffer.remove(entry);
					for (Object itemObject : entry.getValue()) {
						info.lastpos = generateItem(info, itemObject, info.lastpos, origin);
					}
					
				}
			}
		}
		if (object.containsKey("items")) {
			List<Object> items = (List<Object>) object.get("items");
			for (Object itemObject : items) {
				info.lastpos = generateItem(info, itemObject, info.lastpos, origin);
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
			nextPosition += (getItemsOnPage() - (nextPosition % getItemsOnPage()));
		}
		if (linebreakC >= 2) {
			nextPosition += (items_on_row - (nextPosition % items_on_row));
		}
		if (pagebreakC < 2 && linebreakC < 2) {
			nextPosition++;
		}
		if (write) {
			lastpos = nextPosition;
		}
		if (object.containsKey("times")) {
			Map<String, Object> nobject = new HashMap<>(object);
			int times = ((Number) nobject.get("times")).intValue();
			if (times > 1) {
				if (nobject.containsKey("times-methods")) {
					Object met = nobject.get("times-methods");
					if (met instanceof List) {
						List<String> methods = (List<String>) met;
						for (String method : methods) {
							if (method.equalsIgnoreCase("cancel-positioning")) {
								for (String property : POSITION_PROPERTIES) {
									if (nobject.containsKey(property)) {
										nobject.remove(property);
									}
								}
							} else if (method.equalsIgnoreCase("no-id")) {
								if (nobject.containsKey("id")) {
									nobject.remove("id");
								}
							}
						}
					} else {
						String method = met.toString();
						if (method.equalsIgnoreCase("cancel-positioning")) {
							for (String property : POSITION_PROPERTIES) {
								if (nobject.containsKey(property)) {
									nobject.remove(property);
								}
							}
						} else if (method.equalsIgnoreCase("no-id")) {
							if (nobject.containsKey("id")) {
								nobject.remove("id");
							}
						}
					}
				}
				nobject.put("times", times - 1);
				lastpos = generateItem(parent, nobject, lastpos, origin);
			}
		}
		return lastpos;
	}

	private boolean isPositionProperty(String key) {
		return POSITION_PROPERTIES.contains(key);
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
		return items_on_row * rows;
	}

	public int getItemsOnRow() {
		return items_on_row;
	}

	public int getRows() {
		return rows;
	}

	public int getRenderRows() {
		return render_actual_rows;
	}

	public int getRenderHeaderStart() {
		return render_header_row_start;
	}

	public int getRenderFooterStart() {
		return render_footer_row_start;
	}

	public int getRenderOffset() {
		return render_start_offset;
	}

	public boolean getShowPageNumber() {
		return showPageNumber;
	}

	public SimpleGuiFormat openForPlayer(Player player) {
		new GuiHolder(player, this, null, 0);
		return this;
	}

	public GuiHolder getCurrentGuiHolder(Player player) {
		Inventory top = player.getOpenInventory().getTopInventory();
		if (top != null) {
			if (top.getHolder() instanceof GuiHolder) {
				return (GuiHolder) top.getHolder();
			}
		}
		return null;
	}

	public boolean isAllowedToExecuteConsoleCommands() {
		return this.allowAccessToConsole;
	}

	public ItemInfo findItemInfoById(String id) {
		if (id.startsWith("§") || id.startsWith("$")) {
			id = id.substring(1);
		}
		return this.ids.get(id);
	}
}
