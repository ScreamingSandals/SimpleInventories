package misat11.lib.sgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import misat11.lib.sgui.events.GenerateItemEvent;

public class DynamicGuiHolder implements InventoryHolder {

	private final SimpleGuiFormat format;
	private final List<ItemInfo> items;
	private final Map<Integer, ItemInfo> itemsInInventory;
	private final ItemInfo parent;
	private final int page;
	private final DynamicGuiCreator creator;
	private final Player player;
	private final Inventory inv;
	
	public DynamicGuiHolder(Player player, DynamicGuiCreator creator, ItemInfo parent, int page) {
		this.format = creator.getFormat();
		this.creator = creator;
		this.parent = parent;
		this.page = page;
		List<ItemInfo> items = new ArrayList<ItemInfo>();
		if (this.creator.getDynamicInfo().containsKey(null)) {
			Map<Integer, List<ItemInfo>> map = this.creator.getDynamicInfo().get(parent);
			if (map.containsKey(page)) {
				items.addAll(map.get(page));
			}
		}
		this.items = items;
		this.player = player;
		this.inv = Bukkit.createInventory(this, 54, creator.getPrefix() + "§r - " + (page + 1));
		this.itemsInInventory = new HashMap<Integer, ItemInfo>();
		this.repaint();
		this.player.openInventory(this.inv);
	}
	
	public void repaint() {
		this.inv.clear();
		this.itemsInInventory.clear();
		
		if (this.parent != null) {
			this.inv.setItem(0, creator.getBackItem());
		} else {
			this.inv.setItem(0, creator.getCosmeticItem());
		}

		for (int a = 1; a <= 8; a++) {
			this.inv.setItem(a, creator.getCosmeticItem());
		}

		if (page > 0) {
			this.inv.setItem(45, creator.getPageBackItem());
		} else {
			this.inv.setItem(45, creator.getCosmeticItem());
		}

		for (int a = 1; a <= 7; a++) {
			this.inv.setItem(45 + a, creator.getCosmeticItem());
		}
		
		if (this.creator.getLastPageNumbers().get(this.parent) > this.page) {
			this.inv.setItem(53, creator.getPageForwardItem());
		} else {
			this.inv.setItem(53, creator.getCosmeticItem());
		}
		
		for (ItemInfo item : items) {
			ItemStack stack = item.getItem();
			if (item.getData().getData().containsKey("clone")) {
				Object obj = item.getData().getData().get("clone");
				if (obj instanceof String && obj != null) {
					String clone = (String) obj;
					if ("cosmetic".equalsIgnoreCase(clone)) {
						stack = this.creator.getCosmeticItem();
					}
				}
			}
			
			GenerateItemEvent event = new GenerateItemEvent(this.format, item);
			Bukkit.getPluginManager().callEvent(event);
			
			int cpos = (item.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE) + SimpleGuiFormat.ITEMS_ON_ROW;
			
			this.inv.setItem(cpos, stack);
			this.itemsInInventory.put(cpos, item);
		}
	}
	
	public ItemInfo getItemInfoOnPosition(int position) {
		return this.itemsInInventory.get(position);
	}
	
	public SimpleGuiFormat getFormat() {
		return this.format;
	}
	
	public List<ItemInfo> getItems() {
		return new ArrayList<ItemInfo>(this.items);
	}
	
	public ItemInfo getParent() {
		return this.parent;
	}
	
	public int getPage() {
		return this.page;
	}
	
	public DynamicGuiCreator getCreator() {
		return this.creator;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	@Override
	public Inventory getInventory() {
		return this.inv;
	}

}
