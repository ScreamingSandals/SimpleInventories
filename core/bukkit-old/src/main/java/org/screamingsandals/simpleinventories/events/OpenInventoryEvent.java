package org.screamingsandals.simpleinventories.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.ItemInfo;

@Getter
@RequiredArgsConstructor
public class OpenInventoryEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final SimpleInventories format;
	private final Inventory inv;
	private boolean cancel = false;
	private final ItemInfo parent;
	private final int page;

	public static HandlerList getHandlerList() {
		return OpenInventoryEvent.handlers;
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
