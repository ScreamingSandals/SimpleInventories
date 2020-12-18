package org.screamingsandals.simpleinventories.operations.bitwise;

import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.operations.Operation;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public class ComplimentBitwise implements Operation {
	
	private Inventory format;
	private Object obj;

	public ComplimentBitwise(Inventory format, Object obj) {
		this.format = format;
		this.obj = obj;
	}

	@Override
	public Object resolveFor(PlayerWrapper player, PlayerItemInfo info) {
		Object ob = this.obj;
		if (ob instanceof Operation) {
			ob = ((Operation) ob).resolveFor(player, info);
		}
		if (ob instanceof String) {
			ob = format.processPlaceholders(player, (String) ob, info);
		}
		if (ob instanceof String) {
			try {
				ob = Double.parseDouble((String) ob);
			} catch (NumberFormatException ex) {
			}
		}
		if (ob instanceof Number) {
			return ~(((Number) ob).intValue());
		}
		return 0;
	}

}
