package org.screamingsandals.simpleinventories.operations.bitwise;

import org.screamingsandals.simpleinventories.inventory.Inventory;

public class AndBitwise extends AbstractBitwise {

	public AndBitwise(Inventory format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	public Object resolveFor(Object obj1, Object obj2) {
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return ((Number) obj1).intValue() & ((Number) obj2).intValue();
		}
		return 0;
	}

}
