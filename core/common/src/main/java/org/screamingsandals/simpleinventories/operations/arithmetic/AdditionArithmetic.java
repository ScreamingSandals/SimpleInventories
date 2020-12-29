package org.screamingsandals.simpleinventories.operations.arithmetic;

import org.screamingsandals.simpleinventories.inventory.InventorySet;

public class AdditionArithmetic extends AbstractArithmetic {

	public AdditionArithmetic(InventorySet format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	public Object resolveFor(Object obj1, Object obj2) {
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return ((Number) obj1).doubleValue() + ((Number) obj2).doubleValue();
		}
		return obj1.toString() + obj2.toString();
	}

}
