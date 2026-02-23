/*
 * Copyright (C) 2026 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.simpleinventories.item;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.inventory.LocalOptions;
import org.screamingsandals.simpleinventories.inventory.Origin;
import org.screamingsandals.simpleinventories.operations.conditions.Condition;
import org.screamingsandals.simpleinventories.utils.MapReader;

public class ItemInfo {
	private ItemInfo parent;
	private int position;
	private ItemStack item;
	private List<ItemStack> animation;
	private boolean visible = false;
	private boolean disabled = false;
	private String id;
	private List<ItemProperty> properties;
	private Map<String, Object> data;
	private ItemStack book;
	private SimpleInventories format;
	private Map<Condition, Map<String, Object>> conditions;
	private Origin origin;
	private boolean written;
	private LocalOptions localOptions;
	@Getter
	private List<RenderCallback> renderCallbacks;
	public int lastpos = 0;

	public ItemInfo(SimpleInventories format, ItemInfo parent, ItemStack item, int position, boolean visible,
			boolean disabled, String id, List<ItemProperty> properties, Map<String, Object> data, List<ItemStack> animation,
			Map<Condition, Map<String, Object>> conditions, Origin origin, boolean written, List<RenderCallback> renderCallbacks,
					LocalOptions localOptions) {
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
		this.origin = origin;
		this.written = written;
		this.renderCallbacks = renderCallbacks;
		this.localOptions = localOptions;
	}

	public LocalOptions getLocalOptions() {
		return localOptions != null ? localOptions : (parent != null ? parent.getLocalOptions() : format.getLocalOptions());
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

	public List<ItemProperty> getProperties() {
		return properties;
	}

	@Deprecated
	public Map<String, Object> getData() {
		return data;
	}

	public MapReader getReader(Player owner) {
		return new MapReader(format, data, owner, null);
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

	public SimpleInventories getFormat() {
		return format;
	}
	
	public Map<Condition, Map<String, Object>> getConditions() {
		return conditions;
	}
	
	public Origin getOrigin() {
		return origin;
	}
	
	public boolean isWritten() {
		return written;
	}
}
