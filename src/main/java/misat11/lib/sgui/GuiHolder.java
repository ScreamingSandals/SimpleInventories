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
import org.bukkit.inventory.meta.ItemMeta;

import misat11.lib.sgui.events.GenerateItemEvent;
import misat11.lib.sgui.events.OpenInventoryEvent;

public class GuiHolder implements InventoryHolder {

	private final SimpleGuiFormat format;
	private final List<ItemInfo> items;
	private final Map<Integer, PlayerItemInfo> itemsInInventory;
	private final ItemInfo parent;
	private final int page;
	private final Player player;
	private final Inventory inv;
	private boolean animationExists = false;
	private final List<PlayerItemInfo> itemsWithAnimation;
	private GuiAnimator animator;

	public GuiHolder(Player player, SimpleGuiFormat format, ItemInfo parent, int page) {
		this.format = format;
		this.parent = parent;
		this.page = page;
		List<ItemInfo> items = new ArrayList<ItemInfo>();
		if (this.format.getDynamicInfo().containsKey(null)) {
			Map<Integer, List<ItemInfo>> map = this.format.getDynamicInfo().get(parent);
			if (map.containsKey(page)) {
				items.addAll(map.get(page));
			}
		}
		this.items = items;
		this.player = player;
		this.inv = Bukkit.createInventory(this, 54, format.getPrefix() + "§r - " + (page + 1));
		this.itemsInInventory = new HashMap<Integer, PlayerItemInfo>();
		this.itemsWithAnimation = new ArrayList<PlayerItemInfo>();
		this.repaint();

		OpenInventoryEvent event = new OpenInventoryEvent(this.player, this.format, this.inv, this.parent, this.page);
		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}

		this.player.openInventory(this.inv);
	}

	public void repaint() {
		this.animationExists = false;
		this.inv.clear();
		this.itemsInInventory.clear();
		this.itemsWithAnimation.clear();
		if (animator != null) {
			try {
				animator.cancel();
			} catch (Throwable t) {
			}
			animator = null;
		}

		if (this.parent != null) {
			this.inv.setItem(0, format.getBackItem());
		} else {
			this.inv.setItem(0, format.getCosmeticItem());
		}

		for (int a = 1; a <= 8; a++) {
			this.inv.setItem(a, format.getCosmeticItem());
		}

		if (page > 0) {
			this.inv.setItem(45, format.getPageBackItem());
		} else {
			this.inv.setItem(45, format.getCosmeticItem());
		}

		for (int a = 1; a <= 7; a++) {
			this.inv.setItem(45 + a, format.getCosmeticItem());
		}

		if (this.format.getLastPageNumbers().get(this.parent) > this.page) {
			this.inv.setItem(53, format.getPageForwardItem());
		} else {
			this.inv.setItem(53, format.getCosmeticItem());
		}

		for (ItemInfo item : items) {
			ItemStack stack = item.getItem();
			MapReader reader = item.getReader(player);
			if (reader.containsKey("clone")) {
				String clone = reader.getString("clone");
				if (clone != null) {
					if ("cosmetic".equalsIgnoreCase(clone)) {
						stack = this.format.getCosmeticItem();
					}
				}
			}
			
			ItemStack cloned = stack.clone();
			if (cloned.hasItemMeta()) {
				ItemMeta meta = cloned.getItemMeta();
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
				cloned.setItemMeta(meta);
			}

			PlayerItemInfo playersInfo = new PlayerItemInfo(player, item, cloned, item.isVisible(),
					item.isDisabled());

			GenerateItemEvent event = new GenerateItemEvent(this.format, playersInfo, player);
			Bukkit.getPluginManager().callEvent(event);

			int cpos = (item.getPosition() % format.getItemsOnPage()) + format.getItemsOnRow();

			if (playersInfo.isVisible()) {
				if (playersInfo.hasAnimation()) {
					this.animationExists = true;
					this.itemsWithAnimation.add(playersInfo);
				}
				this.inv.setItem(cpos, event.getStack());
				this.itemsInInventory.put(cpos, playersInfo);
			}
		}

		if (this.animationExists && this.format.isAnimationsEnabled()) {
			animator = new GuiAnimator(this, this.itemsWithAnimation);
			animator.runTaskTimer(this.format.getPluginForRunnables(), 0L, 20L);
		}
	}

	public PlayerItemInfo getItemInfoOnPosition(int position) {
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

	public Player getPlayer() {
		return this.player;
	}

	@Override
	public Inventory getInventory() {
		return this.inv;
	}

}
