package org.screamingsandals.simpleinventories.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.ItemInfo;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

public class PreActionEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private Player player = null;
	private SimpleInventories format = null;
	private Inventory inv = null;
	private ItemInfo parent = null;
	private PlayerItemInfo item = null;
	private boolean cancel = false;
	private ClickType clickType = null;

	public PreActionEvent(Player player, SimpleInventories format, Inventory inv, ItemInfo parent, PlayerItemInfo item, ClickType clickType) {
		this.player = player;
		this.format = format;
		this.inv = inv;
		this.parent = parent;
		this.item = item;
		this.clickType = clickType;
	}

	public static HandlerList getHandlerList() {
		return PreActionEvent.handlers;
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public SimpleInventories getFormat() {
		return this.format;
	}
	
	public Inventory getInventory() {
		return this.inv;
	}
	
	public ItemInfo getParent() {
		return this.parent;
	}
	
	public PlayerItemInfo getItem() {
		return this.item;
	}
	
	@Deprecated
	public ItemInfo getOriginalItem() {
		return this.item.getOriginal();
	}
	
	public ClickType getClickType() {
		return this.clickType;
	}

	@Override
	public HandlerList getHandlers() {
		return PreActionEvent.handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
