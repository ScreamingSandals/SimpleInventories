package misat11.lib.sgui;

import java.util.Map;

public class Property {
	private String propertyName;
	private Map<String, Object> propertyData;
	
	public Property(String propertyName, Map<String, Object> propertyData) {
		this.propertyName = propertyName;
		this.propertyData = propertyData;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public Map<String, Object> getPropertyData() {
		return propertyData;
	}
	
	public void setPropertyData(Map<String, Object> propertyData) {
		this.propertyData = propertyData;
	}
	
	public boolean hasName() {
		return propertyName != null;
	}
	
	public boolean hasData() {
		return propertyData != null && !propertyData.isEmpty();
	}
	
}
