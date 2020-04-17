package org.screamingsandals.simpleinventories.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.ItemInfo;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

@Getter
@RequiredArgsConstructor
public class GenerateItemEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private final SimpleInventories format;
	private final PlayerItemInfo info;
	private final Player player;
	
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

	@Override
	public HandlerList getHandlers() {
		return GenerateItemEvent.handlers;
	}
	

}
