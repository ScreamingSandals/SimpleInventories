package misat11.lib.sgui.events;

import misat11.lib.sgui.ItemInfo;
import misat11.lib.sgui.PlayerItemInfo;
import misat11.lib.sgui.SimpleGuiFormat;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public class PostActionEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private Player player = null;
	private SimpleGuiFormat format = null;
	private Inventory inv = null;
	private ItemInfo parent = null;
	private PlayerItemInfo item = null;
	private boolean cancel = false;
	private ClickType clickType = null;

	public PostActionEvent(Player player, SimpleGuiFormat format, Inventory inv, ItemInfo parent, PlayerItemInfo item, ClickType clickType) {
		this.player = player;
		this.format = format;
		this.inv = inv;
		this.parent = parent;
		this.item = item;
		this.clickType = clickType;
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
	
	public PlayerItemInfo getItem() {
		return this.item;
	}
	
	public ItemInfo getOriginalItem() {
		return this.item.getOriginal();
	}
	
	public ClickType getClickType() {
		return this.clickType;
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
