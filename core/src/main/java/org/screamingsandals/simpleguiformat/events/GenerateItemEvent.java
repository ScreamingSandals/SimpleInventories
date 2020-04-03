package org.screamingsandals.simpleguiformat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleguiformat.SimpleGuiFormat;
import org.screamingsandals.simpleguiformat.item.ItemInfo;
import org.screamingsandals.simpleguiformat.item.PlayerItemInfo;

public class GenerateItemEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private SimpleGuiFormat format;
	private PlayerItemInfo info;
	private Player player;
	
	public GenerateItemEvent(SimpleGuiFormat format, PlayerItemInfo info, Player player) {
		this.format = format;
		this.info = info;
		this.player = player;
	}

	public SimpleGuiFormat getFormat() {
		return format;
	}

	public PlayerItemInfo getInfo() {
		return info;
	}
	
	@Deprecated
	public ItemInfo getOriginalInfo() {
		return info.getOriginal();
	}

    public ItemStack getStack() {
    	return info.getStack();
    }
    
    public void setStack(ItemStack stack) {
    	info.setStack(stack);
    }
    
    public boolean isVisible() {
    	return info.isVisible();
    }
    
    public void setVisible(boolean visible) {
    	info.setVisible(visible);
    }
    
    public boolean isDisabled() {
    	return info.isDisabled();
    }
    
    public void setDisabled(boolean disabled) {
    	info.setDisabled(disabled);
    }

	public static HandlerList getHandlerList() {
		return GenerateItemEvent.handlers;
	}
	
	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return GenerateItemEvent.handlers;
	}
	

}
