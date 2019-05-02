package misat11.lib.sgui.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import misat11.lib.sgui.ItemInfo;
import misat11.lib.sgui.SimpleGuiFormat;

public class GenerateItemEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private SimpleGuiFormat format;
	private ItemInfo info;
	
	public GenerateItemEvent(SimpleGuiFormat format, ItemInfo info) {
		this.format = format;
		this.info = info;
	}

	public SimpleGuiFormat getFormat() {
		return format;
	}

	public ItemInfo getInfo() {
		return info;
	}

    public ItemStack getStack() {
    	return info.getItem();
    }
    
    public void setStack(ItemStack stack) {
    	info.setItem(stack);
    }


	public static HandlerList getHandlerList() {
		return GenerateItemEvent.handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return GenerateItemEvent.handlers;
	}
	

}
