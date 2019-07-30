package misat11.lib.sgui;

import java.util.Map;

import org.bukkit.entity.Player;

public class Property {
	private SimpleGuiFormat format;
	private String propertyName;
	private Map<String, Object> propertyData;
	
	public Property(SimpleGuiFormat format, String propertyName, Map<String, Object> propertyData) {
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
	
	public MapReader getReader(Player player) {
		return new MapReader(format, propertyData, player);
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
