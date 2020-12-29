package org.screamingsandals.simpleinventories.operations.bitwise;

import org.screamingsandals.simpleinventories.inventory.InventorySet;

public class ZeroFillRightShiftBitwise extends AbstractBitwise {

	public ZeroFillRightShiftBitwise(InventorySet format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	public Object resolveFor(Object obj1, Object obj2) {
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return ((Number) obj1).intValue() >>> ((Number) obj2).intValue();
		}
		return 0;
	}

}
