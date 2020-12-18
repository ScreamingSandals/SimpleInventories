package org.screamingsandals.simpleinventories.operations.bitwise;

import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.operations.arithmetic.AbstractArithmetic;

public abstract class AbstractBitwise extends AbstractArithmetic {

	public AbstractBitwise(Inventory format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}
}

