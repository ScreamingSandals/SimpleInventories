package misat11.lib.sgui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Options {

	public static final int ROWS = 4;
	
	private ItemStack backItem = new ItemStack(Material.BARRIER);
	private ItemStack pageBackItem = new ItemStack(Material.ARROW);
	private ItemStack pageForwardItem = new ItemStack(Material.ARROW);
	private ItemStack cosmeticItem = new ItemStack(Material.AIR);
	private int rows = ROWS;
	private boolean genericShop = false;
	private boolean genericShopPriceTypeRequired = true;
	private boolean animationsEnabled = false;
	private Plugin animationPlugin = null;
	private final String prefix = "Inventory";
	
	public Options() {
		
	}

	public ItemStack getBackItem() {
		return backItem;
	}

	public void setBackItem(ItemStack backItem) {
		this.backItem = backItem;
	}

	public ItemStack getPageBackItem() {
		return pageBackItem;
	}

	public void setPageBackItem(ItemStack pageBackItem) {
		this.pageBackItem = pageBackItem;
	}

	public ItemStack getPageForwardItem() {
		return pageForwardItem;
	}

	public void setPageForwardItem(ItemStack pageForwardItem) {
		this.pageForwardItem = pageForwardItem;
	}

	public ItemStack getCosmeticItem() {
		return cosmeticItem;
	}

	public void setCosmeticItem(ItemStack cosmeticItem) {
		this.cosmeticItem = cosmeticItem;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public boolean isGenericShop() {
		return genericShop;
	}

	public void setGenericShop(boolean genericShop) {
		this.genericShop = genericShop;
	}

	public boolean isGenericShopPriceTypeRequired() {
		return genericShopPriceTypeRequired;
	}

	public void setGenericShopPriceTypeRequired(boolean genericShopPriceTypeRequired) {
		this.genericShopPriceTypeRequired = genericShopPriceTypeRequired;
	}

	public boolean isAnimationsEnabled() {
		return animationsEnabled;
	}

	public void setAnimationsEnabled(boolean animationsEnabled, Plugin animationPlugin) {
		this.animationsEnabled = animationsEnabled;
		this.animationPlugin = animationPlugin;
	}

	public Plugin getAnimationPlugin() {
		return animationPlugin;
	}

	public String getPrefix() {
		return prefix;
	}
}
