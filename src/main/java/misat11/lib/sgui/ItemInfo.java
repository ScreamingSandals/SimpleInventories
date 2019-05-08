package misat11.lib.sgui;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class ItemInfo {
	private ItemInfo parent;
	private int position;
	private ItemStack item;
	private List<ItemStack> animation;
	private boolean visible = false;
	private boolean disabled = false;
	private String id;
	private List<Property> properties;
	private Map<String, Object> data;
	private ItemStack book;
	public int lastpos = 0;
	
	public ItemInfo(ItemInfo parent, ItemStack item, int position, boolean visible, boolean disabled, String id, List<Property> properties, Map<String, Object> data, List<ItemStack> animation) {
		this.parent = parent;
		this.item = item;
		this.position = position;
		this.visible = visible;
		this.disabled = disabled;
		this.id = id;
		this.properties = properties;
		this.data = data;
		this.animation = animation;
	}
	
	public int getPosition() {
		return position;
	}
	
	public ItemStack getItem() {
		return item;
	}

	public ItemInfo getParent() {
		return parent;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public boolean isDisabled() {
		return this.disabled;
	}

	public String getId() {
		return id;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public Map<String, Object> getData() {
		return data;
	}
	
	public List<ItemStack> getAnimation() {
		return animation;
	}
	
	public boolean hasId() {
		return id != null;
	}
	
	public boolean hasProperties() {
		return properties != null && !properties.isEmpty();
	}
	
	public boolean hasData() {
		return data != null && !data.isEmpty();
	}
	
	public boolean hasAnimation() {
		return animation != null && !animation.isEmpty();
	}

	public ItemStack getBook() {
		return book;
	}

	public void setBook(ItemStack book) {
		this.book = book;
	}
	
	public boolean hasBook() {
		return book != null;
	}
	
}
