package org.screamingsandals.simpleguiformat.item;

import java.util.Map;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleguiformat.SimpleGuiFormat;
import org.screamingsandals.simpleguiformat.utils.MapReader;

public class ItemProperty {
	private SimpleGuiFormat format;
	private String propertyName;
	private Map<String, Object> propertyData;
	
	public ItemProperty(SimpleGuiFormat format, String propertyName, Map<String, Object> propertyData) {
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
	
	public SimpleGuiFormat getFormat() {
		return format;
	}
	
}
