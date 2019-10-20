package misat11.lib.sgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import misat11.lib.sgui.operations.conditions.Condition;

public class PlayerItemInfo {
	private ItemInfo original;
	private ItemStack stack;
	private boolean visible;
	private boolean disabled;
	private List<ItemStack> animation;
	private Player player;
	private Map<String, Object> data;
	
	public PlayerItemInfo(Player player, ItemInfo original, ItemStack stack, boolean visible, boolean disabled) {
		this.player = player;
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
		
		/* We can (and we must) use deprecated variables in internal code :D */
		this.data = original.getData();

		Map<Condition, Map<String, Object>> conditions = original.getConditions();
		for (Map.Entry<Condition, Map<String, Object>> entry : conditions.entrySet()) {
			Condition cond = entry.getKey();
			if (cond.process(player, this)) {
				for (Map.Entry<String, Object> change : entry.getValue().entrySet()) {
					this.data.put(change.getKey(), change.getValue());
					if (change.getKey().equals("visible")) {
						this.visible = (boolean) change.getValue();
					} else if (change.getKey().equals("disabled")) {
						this.disabled = (boolean) change.getValue();
					} else if (change.getKey().equals("stack")) {
						this.stack = (ItemStack) change.getValue();
					} else if (change.getKey().equals("animation")) {
						animation = new ArrayList<ItemStack>();
						for (Object anim : (List<Object>) change.getValue()) {
							if (anim instanceof ItemStack) {
								animation.add(((ItemStack) anim).clone());
							} else if (anim instanceof String) {
								animation.add(ShortStackParser.parseShortStack((String) anim));
							}
						}
					}
				}
			}
		}
	}

	/* You shouldn't work with original item! */
	@Deprecated
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
	
	@Deprecated
	public Map<String, Object> getData() {
		return data;
	}
	
	public MapReader getReader() {
		return new MapReader(original.getFormat(), data, player, this);
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
	
	public SimpleGuiFormat getFormat() {
		return original.getFormat();
	}
	
	/* Returned map should be read with MapReader */
	public Map<Condition, Map<String, Object>> getConditions() {
		return original.getConditions();
	}
	
	public Origin getOrigin() {
		return original.getOrigin();
	}
}
