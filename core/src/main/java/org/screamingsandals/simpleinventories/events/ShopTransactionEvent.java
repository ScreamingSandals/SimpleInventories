package org.screamingsandals.simpleinventories.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.ItemProperty;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

@Getter
public class ShopTransactionEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final SimpleInventories format;
	private final int price;
	private final String type;
	private final ItemStack stack;
	private final PlayerItemInfo item;
	private boolean cancel = false;
	private final ClickType clickType;
	private final boolean hasExecutions;
	@Setter
	private boolean runExecutions = false;

	public ShopTransactionEvent(Player player, SimpleInventories format, PlayerItemInfo item, int price, String type, ClickType clickType, boolean hasExecutions) {
		this.player = player;
		this.format = format;
		this.item = item;
		this.stack = item.getOriginal().getItem().clone();
		if (this.stack.hasItemMeta()) {
			ItemMeta meta = this.stack.getItemMeta();
			if (meta.hasDisplayName()) {
				meta.setDisplayName(format.processPlaceholders(player, meta.getDisplayName(), item));
			}
			if (meta.hasLore()) {
				List<String> lore = new ArrayList<String>();
				for (String str : meta.getLore()) {
					lore.add(format.processPlaceholders(player, str, item));
				}
				meta.setLore(lore);
			}
			this.stack.setItemMeta(meta);
		}
		this.price = price;
		this.type = type;
		this.clickType = clickType;
		this.hasExecutions = hasExecutions;
	}

	public static HandlerList getHandlerList() {
		return ShopTransactionEvent.handlers;
	}
	
	public boolean hasPlayerInInventory() {
		return hasPlayerInInventory(this.stack);
	}
	
	public boolean hasPlayerInInventory(ItemStack stack) {
		return this.player.getInventory().containsAtLeast(stack, stack.getAmount());
	}
	
	public HashMap<Integer, ItemStack>  sellStack() {
		return sellStack(this.stack);
	}
	
	public HashMap<Integer, ItemStack> sellStack(ItemStack stack) {
		return this.player.getInventory().removeItem(stack);
	}
	
	public HashMap<Integer, ItemStack>  buyStack() {
		return buyStack(this.stack);
	}
	
	public HashMap<Integer, ItemStack>  buyStack(ItemStack stack) {
		return this.player.getInventory().addItem(stack);
	}
	
	public List<ItemProperty> getProperties() {
		return this.item.getProperties();
	}
	
	public boolean hasProperties() {
		return this.item.hasProperties();
	}

	@Override
	public HandlerList getHandlers() {
		return ShopTransactionEvent.handlers;
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
