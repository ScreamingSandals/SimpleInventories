package org.screamingsandals.simpleinventories.inventory;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.events.GenerateItemEvent;
import org.screamingsandals.simpleinventories.events.OpenInventoryEvent;
import org.screamingsandals.simpleinventories.item.ItemInfo;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuiHolder implements InventoryHolder {
	public static final Map<Inventory, GuiHolder> TILE_ENTITY_HOLDER_CONVERTOR = new ConcurrentHashMap<>();

	private final Player player;
	private final Inventory inv;

	private SimpleInventories format;
	private List<ItemInfo> items;
	private Map<Integer, PlayerItemInfo> itemsInInventory;
	private ItemInfo parent;
	private int page;
	private boolean animationExists = false;
	private List<PlayerItemInfo> itemsWithAnimation;
	private GuiAnimator animator;
	@Getter
	private LocalOptions localOptions;

	public GuiHolder(Player player, SimpleInventories format, ItemInfo parent, int page) {
		this.format = format;
		this.parent = parent;
		this.page = page;
		this.items = getItemsInfo();
		this.player = player;
		this.localOptions = parent != null ? parent.getLocalOptions() : format.getLocalOptions();
		if (localOptions.getInventoryType() == InventoryType.CHEST) {
			this.inv = Bukkit.createInventory(this, localOptions.getItems_on_row() * localOptions.getRender_actual_rows(),
					format.getPrefix() + (format.getShowPageNumber() ? ("§r - " + (page + 1)) : ""));
		} else {
			this.inv = Bukkit.createInventory(this, localOptions.getInventoryType(),
					format.getPrefix() + (format.getShowPageNumber() ? ("§r - " + (page + 1)) : ""));
		}
		this.itemsInInventory = new HashMap<>();
		this.itemsWithAnimation = new ArrayList<>();
		this.repaint();

		OpenInventoryEvent event = new OpenInventoryEvent(this.player, this.format, this.inv, this.parent, this.page);

		format.getOpenCallbacks().forEach(openCallback -> openCallback.open(event));

		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}

		if (this.inv.getHolder() != this) {
			TILE_ENTITY_HOLDER_CONVERTOR.put(this.inv, this);
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
			} catch (Throwable ignored) {
			}
			animator = null;
		}


		if (this.parent != null) {
			safePutStackToInventory(localOptions.getRender_header_start(), localOptions.getBackItem().clone());
		} else {
			safePutStackToInventory(localOptions.getRender_header_start(), localOptions.getCosmeticItem().clone());
		}

		for (int a = 1; a < localOptions.getItems_on_row(); a++) {
			safePutStackToInventory(localOptions.getRender_header_start() + a, localOptions.getCosmeticItem().clone());
		}

		if (page > 0) {
			safePutStackToInventory(localOptions.getRender_footer_start(), localOptions.getPageBackItem().clone());
		} else {
			safePutStackToInventory(localOptions.getRender_footer_start(), localOptions.getCosmeticItem().clone());
		}

		for (int a = 1; a < localOptions.getItems_on_row(); a++) {
			safePutStackToInventory(localOptions.getRender_footer_start() + a, localOptions.getCosmeticItem().clone());
		}

		if (this.format.getLastPageNumbers().get(this.parent) > this.page) {
			safePutStackToInventory(localOptions.getRender_footer_start() + localOptions.getItems_on_row() - 1,
					localOptions.getPageForwardItem());
		} else {
			safePutStackToInventory(localOptions.getRender_footer_start() + localOptions.getItems_on_row() - 1,
					localOptions.getCosmeticItem());
		}

		for (ItemInfo item : items) {
			ItemStack stack = item.getItem();

			ItemStack cloned = stack.clone();

			PlayerItemInfo playersInfo = new PlayerItemInfo(player, item, cloned, item.isVisible(), item.isDisabled());
			
			if (cloned.hasItemMeta()) {
				ItemMeta meta = cloned.getItemMeta();
				if (meta.hasDisplayName()) {
					meta.setDisplayName(format.processPlaceholders(player, meta.getDisplayName(), playersInfo));
				}
				if (meta.hasLore()) {
					List<String> lore = new ArrayList<>();
					for (String str : meta.getLore()) {
						lore.add(format.processPlaceholders(player, str, playersInfo));
					}
					meta.setLore(lore);
				}
				cloned.setItemMeta(meta);
				playersInfo.setStack(cloned);
			}

			GenerateItemEvent event = new GenerateItemEvent(this.format, playersInfo, player);
			Bukkit.getPluginManager().callEvent(event);

			int cpos = (item.getPosition() % localOptions.getItemsOnPage()) + localOptions.getRender_offset();

			if (playersInfo.isVisible()) {
				if (playersInfo.hasAnimation()) {
					this.animationExists = true;
					this.itemsWithAnimation.add(playersInfo);
				}
				safePutStackToInventory(cpos, event.getStack());
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

	public SimpleInventories getFormat() {
		return this.format;
	}

	public void setFormat(SimpleInventories format) {
		this.format = format;
		this.items = getItemsInfo();
	}

	public List<ItemInfo> getItems() {
		return new ArrayList<>(this.items);
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

	public void safePutStackToInventory(int i, ItemStack stack) {
		if (i < this.inv.getSize()) {
			this.inv.setItem(i, stack);
		}
	}

	private List<ItemInfo> getItemsInfo() {
		List<ItemInfo> items = new ArrayList<>();
		if (this.format.getDynamicInfo().containsKey(null)) {
			Map<Integer, List<ItemInfo>> map = this.format.getDynamicInfo().get(parent);
			if (map.containsKey(page)) {
				items.addAll(map.get(page));
			}
		}
		return items;
	}
}
