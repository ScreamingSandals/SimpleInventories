package org.screamingsandals.simpleinventories.item;

import java.util.Map;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.utils.MapReader;

public class ItemProperty {
	private SimpleInventories format;
	private String propertyName;
	private Map<String, Object> propertyData;
	
	public ItemProperty(SimpleInventories format, String propertyName, Map<String, Object> propertyData) {
		this.format = format;
		this.propertyName = propertyName;
		this.propertyData = propertyData;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	@Deprecated
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	@Deprecated
	public MapReader getReader(Player player) {
		return new MapReader(format, propertyData, player, null);
	}
	
	public MapReader getReader(Player player, PlayerItemInfo info) {
		return new MapReader(format, propertyData, player, info);
	}
	
	@Deprecated
	public Map<String, Object> getPropertyData() {
		return propertyData;
	}
	
	@Deprecated
	public void setPropertyData(Map<String, Object> propertyData) {
		this.propertyData = propertyData;
	}
	
	public boolean hasName() {
		return propertyName != null;
	}
	
	public boolean hasData() {
		return propertyData != null && !propertyData.isEmpty();
	}
	
	public SimpleInventories getFormat() {
		return format;
	}
	
}
