package misat11.lib.sgui;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import misat11.lib.sgui.operations.conditions.Condition;

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
	private SimpleGuiFormat format;
	private Map<Condition, Map<String, Object>> conditions;
	public int lastpos = 0;

	public ItemInfo(SimpleGuiFormat format, ItemInfo parent, ItemStack item, int position, boolean visible,
			boolean disabled, String id, List<Property> properties, Map<String, Object> data, List<ItemStack> animation,
			Map<Condition, Map<String, Object>> conditions) {
		this.format = format;
		this.parent = parent;
		this.item = item;
		this.position = position;
		this.visible = visible;
		this.disabled = disabled;
		this.id = id;
		this.properties = properties;
		this.data = data;
		this.animation = animation;
		this.conditions = conditions;
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

	@Deprecated
	public Map<String, Object> getData() {
		return data;
	}

	public MapReader getReader(Player owner) {
		return new MapReader(format, data, owner);
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

	public SimpleGuiFormat getFormat() {
		return format;
	}
	
	public Map<Condition, Map<String, Object>> getConditions() {
		return conditions;
	}

}
