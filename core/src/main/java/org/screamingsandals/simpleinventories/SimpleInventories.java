package org.screamingsandals.simpleinventories;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.simpleinventories.builder.FormatBuilder;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.simpleinventories.item.*;
import org.screamingsandals.simpleinventories.loaders.Loader;
import org.screamingsandals.simpleinventories.loaders.LoaderRegister;
import org.screamingsandals.simpleinventories.operations.OperationParser;
import org.screamingsandals.simpleinventories.operations.conditions.Condition;
import org.screamingsandals.simpleinventories.placeholders.AdvancedPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PAPIPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PagePlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PermissionPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.PlayerPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.RuntimeDefinedPlaceholder;
import org.screamingsandals.simpleinventories.placeholders.ThisPlaceholderParser;
import org.screamingsandals.simpleinventories.placeholders.WorldPlaceholderParser;
import org.screamingsandals.simpleinventories.utils.InventoryUtils;
import org.screamingsandals.simpleinventories.utils.StackParser;

public class SimpleInventories {

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
	@Getter
	private boolean allowBungeecord = false;

	private Map<String, PlaceholderParser> placeholders = new HashMap<>();
	private Map<String, AdvancedPlaceholderParser> advancedPlaceholders = new HashMap<>();

	// FROM CREATOR
	private final Map<ItemInfo, Map<Integer, List<ItemInfo>>> infoByAbsolutePosition = new HashMap<ItemInfo, Map<Integer, List<ItemInfo>>>();
	private final Map<ItemInfo, Integer> lastPageNumbers = new HashMap<ItemInfo, Integer>();
	private final String prefix;
	private final List<Map.Entry<String, List<Object>>> insertingBuffer = new ArrayList<>();
	
	private static final List<String> POSITION_PROPERTIES = Arrays.asList("row", "column", "skip", "linebreak", "pagebreak", "absolute");

	@Getter
	private List<OpenCallback> openCallbacks = new ArrayList<>();
	@Getter
	private List<RenderCallback> renderCallbacks = new ArrayList<>();
	@Getter
	private List<PreClickCallback> preClickCallbacks = new ArrayList<>();
	@Getter
	private List<BuyCallback> buyCallbacks = new ArrayList<>();
	@Getter
	private List<PostClickCallback> postClickCallbacks = new ArrayList<>();
	@Getter
	private List<CloseCallback> closeCallbacks = new ArrayList<>();

	@Getter
	private LocalOptions localOptions;

	public SimpleInventories(Options options) {
		this.prefix = options.getPrefix();
		this.animationsEnabled = options.isAnimationsEnabled();
		this.pluginForRunnables = options.getPlugin();
		this.genericShopEnabled = options.isGenericShop();
		this.genericShopPriceTypeRequired = options.isGenericShopPriceTypeRequired();
		this.showPageNumber = options.isShowPageNumber();
		this.allowAccessToConsole = options.isAllowAccessToConsole();
		this.allowBungeecord = options.isAllowBungeecordPlayerSending();

		initPlaceholders();

		this.placeholders.putAll(options.getPlaceholders());
		this.advancedPlaceholders.putAll(options.getAdvancedPlaceholders());

		this.localOptions = new LocalOptions(options);
	}

	private void initPlaceholders() {
		/* STANDARD PLACEHOLDERS */
		placeholders.put("player", new PlayerPlaceholderParser());
		placeholders.put("permission", new PermissionPlaceholderParser());
		placeholders.put("world", new WorldPlaceholderParser());
		placeholders.put("papi", new PAPIPlaceholderParser());

		/* ADVANCED PLACEHOLDERS */
		AdvancedPlaceholderParser thisParser = new ThisPlaceholderParser();
		advancedPlaceholders.put("this", thisParser);
		advancedPlaceholders.put("self", thisParser);
		advancedPlaceholders.put("page", new PagePlaceholderParser());
	}

	public SimpleInventories carrigeReturn() {
		this.lastpos -= (lastpos - localOptions.getItems_on_row()) % localOptions.getItems_on_row();
		return this;
	}

	public SimpleInventories lineBreak() {
		this.lastpos += (localOptions.getItems_on_row() - (lastpos % localOptions.getItems_on_row()));
		return this;
	}

	public SimpleInventories pageBreak() {
		this.lastpos += (localOptions.getItemsOnPage() - (lastpos % localOptions.getItemsOnPage()));
		return this;
	}

	public SimpleInventories jump(int number) {
		this.lastpos += number;
		if (this.lastpos < 0) {
			this.lastpos = 0;
		}
		return this;
	}

	public SimpleInventories absolute(int position) {
		this.lastpos = position;
		return this;
	}

	public SimpleInventories column(Column column) {
		return column(column.convert(localOptions.getItems_on_row()));
	}

	public SimpleInventories column(int column) {
		this.lastpos = (this.lastpos - (this.lastpos % localOptions.getItems_on_row())) + column;
		return this;
	}

	public SimpleInventories row(int row) {
		this.lastpos = this.lastpos - (this.lastpos % localOptions.getItemsOnPage()) + ((row - 1) * localOptions.getItems_on_row())
			+ (this.lastpos % localOptions.getItems_on_row());
		return this;
	}

	public SimpleInventories clearInput() {
		this.data.clear();
		this.previous = null;
		return this;
	}

	public SimpleInventories purgeData() {
		this.clearInput();
		this.ids.clear();
		this.generatedData.clear();
		this.lastpos = 0;
		this.infoByAbsolutePosition.clear();
		this.lastPageNumbers.clear();
		this.insertingBuffer.clear();
		return this;
	}

	public List<Origin> cloneCurrentInput() {
		return new ArrayList<>(data);
	}

	public SimpleInventories load(List<Object> data) {
		this.data.add(new Origin(data));
		return this;
	}

	public SimpleInventories load(Origin origin) {
		this.data.add(origin);
		return this;
	}

	public SimpleInventories load(FormatBuilder builder) {
		this.data.add(new Origin(builder, builder.getResult()));
		return this;
	}

	public SimpleInventories load(String fileName) throws Exception {
		return load(new File(fileName), "data");
	}

	public SimpleInventories load(String fileName, String configPath) throws Exception {
		return load(new File(fileName), configPath);
	}

	public SimpleInventories loadFromDataFolder(File pluginDataFolder, String fileName) throws Exception {
		return load(new File(pluginDataFolder, fileName), "data");
	}

	public SimpleInventories loadFromDataFolder(File pluginDataFolder, String fileName, String configPath)
		throws Exception {
		return load(new File(pluginDataFolder, fileName), configPath);
	}

	public SimpleInventories load(File file) throws Exception {
		return load(file, "data");
	}

	public SimpleInventories load(File file, String configPath) throws Exception {
		return load(file, configPath, LoaderRegister.getLoader(file));
	}

	public SimpleInventories load(File file, Loader loader) throws Exception {
		return load(file, "data", loader);
	}

	public SimpleInventories load(File file, String configPath, Loader loader) throws Exception {
		this.data.add(loader.readData(file, configPath, this.localOptions));
		return this;
	}

	public boolean isAnimationsEnabled() {
		return this.animationsEnabled && this.pluginForRunnables != null;
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

	public SimpleInventories generateData() {
		for (Origin origin : data) {
			this.openCallbacks.addAll(origin.getOpenCallbacks());
			this.renderCallbacks.addAll(origin.getRenderCallbacks());
			this.preClickCallbacks.addAll(origin.getPreClickCallbacks());
			this.buyCallbacks.addAll(origin.getBuyCallbacks());
			this.postClickCallbacks.addAll(origin.getPostClickCallbacks());
			this.closeCallbacks.addAll(origin.getCloseCallbacks());
			for (Object object : origin.getContent()) {
				lastpos = generateItem(null, object, lastpos, origin, localOptions);
			}
		}

		for (ItemInfo info : this.generatedData) {
			if (!info.isWritten()) {
				continue;
			}
			if (!infoByAbsolutePosition.containsKey(info.getParent())) {
				infoByAbsolutePosition.put(info.getParent(), new HashMap<Integer, List<ItemInfo>>());
			}
			LocalOptions parentOptions = info.getParent() != null ? info.getParent().getLocalOptions() : this.localOptions;
			int page = (info.getPosition() / parentOptions.getItemsOnPage());
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

	private int generateItem(ItemInfo parent, Object original, int lastpos, Origin origin, LocalOptions currentOptions) {
		Map<String, Object> object = new HashMap<>();
		if (original instanceof Map) {
			object.putAll((Map<String, Object>) original);
		} else {
			String obj = original.toString().trim();
			if (obj.startsWith("define")) {
				String[] definition = obj.split(" ", 2);
				if (definition.length == 2) {
					object.put("define", definition[1]);
				}
			} else {
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
								object.put("stack", currentOptions.getCosmeticItem().clone());
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
							object.put("stack", currentOptions.getCosmeticItem().clone());
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
						object.put("stack", currentOptions.getCosmeticItem().clone());
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
		}

		/* In future there will be better way how to do this! we don't recommended to use hacks if there's another way*/
		if (object.containsKey("guihack")) {
			String hack = object.get("guihack").toString();
			if (hack.equalsIgnoreCase("noheader")) {
				currentOptions.setRender_header_start(55);
				currentOptions.setRender_offset(0);
				if (currentOptions.getRows() < 6) {
					currentOptions.setRows(currentOptions.getRows() + 1);
				}
			} else if (hack.equalsIgnoreCase("nofooter")) {
				currentOptions.setRender_footer_start(55);
				if (currentOptions.getRows() < 6) {
					currentOptions.setRows(currentOptions.getRows() + 1);
				}
			} else if (hack.equalsIgnoreCase("noheaderfooter")) {
				currentOptions.setRender_header_start(55);
				currentOptions.setRender_footer_start(55);
				currentOptions.setRender_offset(0);
				if (currentOptions.getRows() < 5) {
					currentOptions.setRows(currentOptions.getRows() + 2);
				} else if (currentOptions.getRows() < 6) {
					currentOptions.setRows(currentOptions.getRows() + 1);
				}
			}
			return lastpos;
		}

		if (object.containsKey("define")) {
			String definition = object.get("define").toString();
			String[] defsplit = definition.split(" as ", 2);
			String key = defsplit[0].trim();
			if (key.startsWith("%")) {
				key = key.substring(1);
			}
			if (key.endsWith("%")) {
				key = key.substring(0, key.length() - 1);
			}
			String[] placeholderFormat = key.split("(?<!\\.)\\.(?!\\.)", 2);
			key = placeholderFormat[0];
			String placeholderArguments = placeholderFormat.length == 1 ? "" : placeholderFormat[1];
			if (placeholders.containsKey(key)) {
				Bukkit.getLogger().severe("[SimpleInventories] Placeholder " + key + " is already defined as non-dynamic placeholder!");
				return lastpos;
			}
			if (!advancedPlaceholders.containsKey(key)) {
				advancedPlaceholders.put(key, new RuntimeDefinedPlaceholder());
			}
			AdvancedPlaceholderParser parser = advancedPlaceholders.get(key);
			if (parser instanceof RuntimeDefinedPlaceholder) {
				if (defsplit.length == 2) {
					RuntimeDefinedPlaceholder rdp = (RuntimeDefinedPlaceholder) parser;
					if (!placeholderArguments.isEmpty()) {
						rdp.register(placeholderArguments, defsplit[1].trim());
					} else {
						rdp.putDefault(defsplit[1].trim());
					}
				}
			} else {
				Bukkit.getLogger().severe("[SimpleInventories] Placeholder " + key + " is already defined as non-dynamic advanced placeholder!");
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
				Origin or = loader.readData(file, data, currentOptions);
				List<Object> items = or.getContent();
				for (Object item : items) {
					lastpos = generateItem(parent, item, lastpos, or, currentOptions);
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
							this.lastpos = generateItem(null, itemObject, this.lastpos, origin, this.localOptions);
						}
					}
					return parent == null ? this.lastpos : lastpos;
				} else if (insert.startsWith("§") || insert.startsWith("$")) {
					ItemInfo inserted = ids.get(insert.substring(1));
					if (object.containsKey("items")) {
						List<Object> items = (List<Object>) object.get("items");
						if (inserted != null) {
							for (Object itemObject : items) {
								inserted.lastpos = generateItem(inserted, itemObject, inserted.lastpos, origin, inserted.getLocalOptions());
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
									this.lastpos = generateItem(null, itemObject, this.lastpos, origin, this.localOptions);
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
										inserted.lastpos = generateItem(inserted, itemObject, inserted.lastpos, origin, inserted.getLocalOptions());
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
					object.put("stack", localOptions.getCosmeticItem().clone());
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
			stack = StackParser.parse(object.get("stack"));
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
			positionC += (currentOptions.getItemsOnPage() - (positionC % currentOptions.getItemsOnPage()));
		}
		if (object.containsKey("row")) {
			positionC = positionC - (positionC % currentOptions.getItemsOnPage()) + (((int) object.get("row") - 1) * localOptions.getItems_on_row())
					+ (positionC % localOptions.getItems_on_row());
		}
		if (object.containsKey("column")) {
			Object cl = object.get("column");
			int column = 0;
			if ("left".equals(cl) || "first".equals(cl)) {
				column = 0;
			} else if ("middle".equals(cl) || "center".equals(cl)) {
				column = localOptions.getItems_on_row() / 2;
			} else if ("right".equals(cl) || "last".equals(cl)) {
				column = localOptions.getItems_on_row() - 1;
			} else {
				column = (int) cl;
			}

			positionC = (positionC - (positionC % localOptions.getItems_on_row())) + column;
		}
		if (linebreakC == 1 || linebreakC == 3) {
			positionC += (localOptions.getItems_on_row() - (positionC % localOptions.getItems_on_row()));
		}
		if (object.containsKey("skip")) {
			positionC += (int) object.get("skip");
		}
		if (object.containsKey("absolute")) {
			positionC = (int) object.get("absolute");
		}
		String id = object.containsKey("id") ? (String) object.get("id") : null;
		List<ItemProperty> properties = new ArrayList<ItemProperty>();
		if (object.containsKey("properties")) {
			Object prop = object.get("properties");
			if (prop instanceof List) {
				List<Object> propertiesList = (List<Object>) prop;
				for (Object obj : propertiesList) {
					if (obj instanceof Map) {
						Map<String, Object> propertyMap = (Map<String, Object>) obj;
						ItemProperty pr = new ItemProperty(this,
								propertyMap.containsKey("name") ? (String) propertyMap.get("name") : null, propertyMap);
						properties.add(pr);
					} else if (obj instanceof String) {
						properties.add(new ItemProperty(this, (String) obj, new HashMap<String, Object>() {
							{
								put("name", (String) obj);
							}
						}));
					}
				}
			} else if (prop instanceof String) {
				properties.add(new ItemProperty(this, (String) prop, new HashMap<String, Object>() {
					{
						put("name", (String) prop);
					}
				}));
			}
		}
		List<ItemStack> animation = null;
		if (object.containsKey("animation")) {
			List<Object> anim = (List<Object>) object.get("animation");
			animation = StackParser.parseAll(anim);
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

		List<RenderCallback> callbacks = new ArrayList<>();

		Object f_visible = object.getOrDefault("visible", true);
		boolean visible = true;
		if (f_visible instanceof Boolean) {
			visible = (Boolean) f_visible;
		} else if (f_visible instanceof Predicate) {
			callbacks.add(info -> info.setVisible(((Predicate<PlayerItemInfo>) f_visible).test(info)));
		} else if (f_visible instanceof String) {
			Condition f_visible_cond = OperationParser.getFinalCondition(this, f_visible.toString());
			callbacks.add(info -> info.setVisible(f_visible_cond.process(info.getPlayer(), info)));
		}

		Object f_disabled = object.getOrDefault("disabled", false);
		boolean disabled = false;
		if (f_disabled instanceof Boolean) {
			disabled = (Boolean) f_disabled;
		} else if (f_disabled instanceof Predicate) {
			callbacks.add(info -> info.setDisabled(((Predicate<PlayerItemInfo>) f_disabled).test(info)));
		} else if (f_disabled instanceof String) {
			Condition f_disabled_cond = OperationParser.getFinalCondition(this, f_disabled.toString());
			callbacks.add(info -> info.setDisabled(f_disabled_cond.process(info.getPlayer(), info)));
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

		if (object.containsKey("rendercallbacks")) {
			List<?> render = (List<?>) object.get("rendercallbacks");
			for (Object o : render) {
				if (o instanceof RenderCallback) {
					callbacks.add((RenderCallback) o);
				}
			}

		}

		LocalOptions options = null;
		if (object.containsKey("options")) {
			Object opt = object.get("options");
			if (opt instanceof LocalOptions) {
				options = (LocalOptions) opt;
			} else if (opt instanceof Map) {
				MemoryConfiguration memory = new MemoryConfiguration();
				memory.addDefaults((Map<String, Object>) opt);
				options = LocalOptions.deserialize(this.localOptions, memory);
			}
		}

		boolean write = object.containsKey("write") ? Boolean.parseBoolean("write") : true;
		ItemInfo info = new ItemInfo(this, parent, stack.clone(), positionC, visible, disabled, id, properties, object,
			animation, conditions, origin, write, callbacks, options);
		if (id != null && !insertingBuffer.isEmpty()) {
			for (Map.Entry<String, List<Object>> entry : new ArrayList<>(insertingBuffer)) {
				if (entry.getKey().equals(id)) {
					insertingBuffer.remove(entry);
					for (Object itemObject : entry.getValue()) {
						info.lastpos = generateItem(info, itemObject, info.lastpos, origin, info.getLocalOptions());
					}
					
				}
			}
		}
		if (object.containsKey("items")) {
			List<Object> items = (List<Object>) object.get("items");
			for (Object itemObject : items) {
				info.lastpos = generateItem(info, itemObject, info.lastpos, origin, info.getLocalOptions());
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
			nextPosition += (currentOptions.getItemsOnPage() - (nextPosition % currentOptions.getItemsOnPage()));
		}
		if (linebreakC >= 2) {
			nextPosition += (currentOptions.getItems_on_row() - (nextPosition % currentOptions.getItems_on_row()));
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
				lastpos = generateItem(parent, nobject, lastpos, origin, currentOptions);
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

	public boolean getShowPageNumber() {
		return showPageNumber;
	}

	public SimpleInventories openForPlayer(Player player) {
		new GuiHolder(player, this, null, 0);
		return this;
	}

	public GuiHolder getCurrentGuiHolder(Player player) {
		Inventory top = player.getOpenInventory().getTopInventory();
		if (top != null) {
			InventoryHolder holder = InventoryUtils.getInventoryHolderWithoutSnapshot(top);
			if (holder instanceof GuiHolder) {
				return (GuiHolder) holder;
			} else if (GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.containsKey(top)) {
				return GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.get(top);
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
