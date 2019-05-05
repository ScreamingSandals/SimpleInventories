package misat11.lib.sgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import misat11.lib.sgui.events.GenerateItemEvent;

public class StaticGuiCreator implements GuiCreator {
	private final SimpleGuiFormat guiFormat;
	private final String prefix;

	private final Map<ItemInfo, List<Inventory>> inventories = new HashMap<ItemInfo, List<Inventory>>();
	private final Map<Inventory, Map<Integer, ItemInfo>> itemsInInventories = new HashMap<Inventory, Map<Integer, ItemInfo>>();
	private final Map<Inventory, ItemInfo> inventoryForHandler = new HashMap<Inventory, ItemInfo>();
	private final Map<ItemInfo, Inventory> backInfo = new HashMap<ItemInfo, Inventory>();
	private ItemStack backItem, pageBackItem, pageForwardItem, cosmeticItem;

	public StaticGuiCreator(String prefix, SimpleGuiFormat guiFormat, ItemStack backItem, ItemStack pageBackItem,
			ItemStack pageForwardItem, ItemStack cosmeticItem) {
		this.prefix = prefix;
		this.guiFormat = guiFormat;
		this.backItem = backItem;
		this.pageBackItem = pageBackItem;
		this.pageForwardItem = pageForwardItem;
		this.cosmeticItem = cosmeticItem;
	}

	public void generate() {
		createInventoryOnPage(1, null, prefix);

		for (ItemInfo item : guiFormat.getPreparedData()) {
			generateItem(item, item.getParent(), item.getParent() == null ? prefix
					: prefix + " > " + item.getParent().getItem().getItemMeta().getDisplayName());
		}
	}

	private void generateItem(ItemInfo item, ItemInfo parent, String pr) {
		if (item.getData().getData().containsKey("clone")) {
			Object obj = item.getData().getData().get("clone");
			if (obj instanceof String && obj != null) {
				String clone = (String) obj;
				if ("cosmetic".equalsIgnoreCase(clone)) {
					item.setItem(cosmeticItem.clone());
				}
			}
		}
		
		GenerateItemEvent event = new GenerateItemEvent(guiFormat, item);
		Bukkit.getPluginManager().callEvent(event);

		int page = (item.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE) + 1;
		Inventory inv = createInventoryOnPage(page, parent, pr);
		int cpos = (item.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE) + SimpleGuiFormat.ITEMS_ON_ROW;
		itemsInInventories.get(inv).put(cpos, item);
		inv.setItem(cpos, item.getItem());
		backInfo.put(item, inv);
	}

	private Inventory createInventoryOnPage(int page, ItemInfo info, String name) {
		if (!inventories.containsKey(info)) {
			inventories.put(info, new ArrayList<Inventory>());
		}

		List<Inventory> inv = inventories.get(info);

		if (inv.size() >= page) {
			return inv.get(page - 1);
		}

		Inventory inventory = Bukkit.getServer().createInventory(null, 54, name + "§r - " + page);

		inv.add(inventory);
		inventoryForHandler.put(inventory, info);
		Map<Integer, ItemInfo> map = new HashMap<Integer, ItemInfo>();
		itemsInInventories.put(inventory, map);

		if (info != null) {
			inventory.setItem(0, backItem);
		} else {
			inventory.setItem(0, cosmeticItem);
		}

		for (int a = 1; a <= 8; a++) {
			inventory.setItem(a, cosmeticItem);
		}

		if (page > 1) {
			inventory.setItem(45, pageBackItem);
			inv.get(page - 2).setItem(53, pageForwardItem);
		} else {
			inventory.setItem(45, cosmeticItem);
		}

		for (int a = 1; a <= 8; a++) {
			inventory.setItem(45 + a, cosmeticItem);
		}

		return inventory;
	}

	public Map<Inventory, ItemInfo> getInventoriesForHandler() {
		return inventoryForHandler;
	}

	@Override
	public SimpleGuiFormat getFormat() {
		return guiFormat;
	}

	public ItemInfo getItemInfoOnPosition(Inventory inv, int position) {
		return itemsInInventories.get(inv).get(position);
	}

	public Inventory getInventoryOfItem(ItemInfo info) {
		return backInfo.get(info);
	}

	public List<Inventory> getInventories(ItemInfo parent) {
		return inventories.get(parent);
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public ItemStack getBackItem() {
		return backItem;
	}

	@Override
	public ItemStack getPageBackItem() {
		return pageBackItem;
	}

	@Override
	public ItemStack getPageForwardItem() {
		return pageForwardItem;
	}

	@Override
	public ItemStack getCosmeticItem() {
		return cosmeticItem;
	}

	@Override
	public void openForPlayer(Player player) {
		player.openInventory(inventories.get(null).get(0));
	}
}
