package misat11.lib.sgui;

import java.util.List;
import java.util.Map;

public class ItemData {
	private String id;
	private List<Property> properties;
	private Map<String, Object> data;
	public int lastpos = 0;
	
	public ItemData(String id, List<Property> properties, Map<String, Object> data) {
		this.id = id;
		this.properties = properties;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
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
	
}
