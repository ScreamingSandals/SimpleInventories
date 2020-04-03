package org.screamingsandals.simpleguiformat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.screamingsandals.simpleguiformat.SimpleGuiFormat;
import org.screamingsandals.simpleguiformat.item.ItemInfo;

public class OpenInventoryEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private Player player = null;
	private SimpleGuiFormat format = null;
	private Inventory inv = null;
	private boolean cancel = false;
	private ItemInfo parent = null;
	private int page = 0;

	public OpenInventoryEvent(Player player, SimpleGuiFormat format, Inventory inv, ItemInfo parent, int page) {
		this.player = player;
		this.format = format;
		this.inv = inv;
		this.parent = parent;
		this.page = page;
	}

	public static HandlerList getHandlerList() {
		return OpenInventoryEvent.handlers;
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public SimpleGuiFormat getFormat() {
		return this.format;
	}
	
	public Inventory getInventory() {
		return this.inv;
	}
	
	public ItemInfo getParent() {
		return this.parent;
	}
	
	public int getPage() {
		return this.page;
	}
	
	@Override
	public HandlerList getHandlers() {
		return OpenInventoryEvent.handlers;
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
