package misat11.lib.sgui.generator;

import java.util.HashMap;
import java.util.Map;

public class ItemStack implements Cloneable {
	
	public final Map<String, Object> obj;
	
	public ItemStack(Map<String, Object> obj) {
		this.obj = obj;
	}
	
	public ItemStack clone() {
		return new ItemStack(new HashMap<>(obj));
	}
	
	
}
