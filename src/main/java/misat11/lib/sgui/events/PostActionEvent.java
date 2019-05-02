package misat11.lib.sgui.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

import misat11.lib.sgui.ItemInfo;
import misat11.lib.sgui.SimpleGuiFormat;

public class PostActionEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private Player player = null;
	private SimpleGuiFormat format = null;
	private Inventory inv = null;
	private ItemInfo parent = null;
	private ItemInfo item = null;
	private boolean cancel = false;

	public PostActionEvent(Player player, SimpleGuiFormat format, Inventory inv, ItemInfo parent, ItemInfo item) {
		this.player = player;
		this.format = format;
		this.inv = inv;
		this.parent = parent;
		this.item = item;
	}

	public static HandlerList getHandlerList() {
		return PostActionEvent.handlers;
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
	
	public ItemInfo getItem() {
		return this.item;
	}

	@Override
	public HandlerList getHandlers() {
		return PostActionEvent.handlers;
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
