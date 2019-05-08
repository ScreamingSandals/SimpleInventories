package misat11.lib.sgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class PlayerItemInfo {
	private ItemInfo original;
	private ItemStack stack;
	private boolean visible;
	private boolean disabled;
	private List<ItemStack> animation;
	
	public PlayerItemInfo(ItemInfo original, ItemStack stack, boolean visible, boolean disabled) {
		this.original = original;
		this.stack = stack;
		this.visible = visible;
		this.disabled = disabled;
		if (original.hasAnimation()) {
			animation = new ArrayList<ItemStack>();
			for (ItemStack anim : original.getAnimation()) {
				animation.add(anim.clone());
			}
		}
	}

	public ItemInfo getOriginal() {
		return original;
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public String getId() {
		return original.getId();
	}
	
	public ItemInfo getParent() {
		return original.getParent();
	}
	
	public int getPosition() {
		return original.getPosition();
	}
	
	public List<Property> getProperties() {
		return original.getProperties();
	}
	
	public Map<String, Object> getData() {
		return original.getData();
	}
	
	public boolean hasId() {
		return getId() != null;
	}
	
	public boolean hasProperties() {
		return getProperties() != null && !getProperties().isEmpty();
	}
	
	public boolean hasData() {
		return getData() != null && !getData().isEmpty();
	}
	
	public boolean hasAnimation() {
		return animation != null && !animation.isEmpty();
	}
	
	public List<ItemStack> getAnimation() {
		return this.animation;
	}
	
	public void setAnimation(List<ItemStack> animation) {
		this.animation = animation;
	}
	
	
}
