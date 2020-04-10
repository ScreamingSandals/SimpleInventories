package org.screamingsandals.simpleinventories.operations;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

public class BlankOperation implements Operation {

	private SimpleInventories format;
	private Object obj;
	
	public BlankOperation(SimpleInventories format, Object obj) {
		this.format = format;
		this.obj = obj;
	}
	
	public Object getBlankObject() {
		return this.obj;
	}
	
	@Override
	public Object resolveFor(Player player, PlayerItemInfo info) {
		Object ob = this.obj;
		if (ob instanceof Operation) {
			ob = ((Operation) ob).resolveFor(player, info);
		}
		if (ob instanceof String) {
			ob = format.processPlaceholders(player, (String) ob, info);
		}
		return ob;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "[format=" + format + ";obj=" + obj + "]";
	}
	
}
