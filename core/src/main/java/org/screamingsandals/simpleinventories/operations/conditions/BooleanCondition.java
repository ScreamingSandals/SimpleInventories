package org.screamingsandals.simpleinventories.operations.conditions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;
import org.screamingsandals.simpleinventories.operations.Operation;

public class BooleanCondition implements Condition {
	protected SimpleInventories format;
	protected Object obj;

	public BooleanCondition(SimpleInventories format, Object obj) {
		this.format = format;
		this.obj = obj;
	}

	@Override
	public boolean process(Player player, PlayerItemInfo info) {
		Object ob = obj;
		if (ob instanceof Operation) {
			ob = ((Operation) ob).resolveFor(player, info);
		}
		if (ob instanceof Boolean) {
			return (Boolean) ob;
		}
		if (ob instanceof Number) {
			return ((Number) ob).doubleValue() != 0;
		}
		if (ob instanceof String) {
			try {
				double number = Double.parseDouble((String) ob);
				return number != 0;
			} catch (Throwable t) {
			}
			ob = format.processPlaceholders(player, (String) ob, info);
			return !((String) ob).isEmpty() && !"false".equalsIgnoreCase((String) ob)
					&& !"null".equalsIgnoreCase((String) ob);
		}
		return ob != null;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "[format=" + format + ";obj=" + obj + "]";
	}
}
