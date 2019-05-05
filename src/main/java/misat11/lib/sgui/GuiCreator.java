package misat11.lib.sgui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface GuiCreator {
	
	public SimpleGuiFormat getFormat();
	
	public void openForPlayer(Player player);
	
	public String getPrefix();
	
	public ItemStack getBackItem();
	
	public ItemStack getPageBackItem();
	
	public ItemStack getPageForwardItem();
	
	public ItemStack getCosmeticItem();
	
}
