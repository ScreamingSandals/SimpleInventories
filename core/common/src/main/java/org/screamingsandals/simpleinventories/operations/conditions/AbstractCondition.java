package org.screamingsandals.simpleinventories.operations.conditions;

import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.operations.Operation;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public abstract class AbstractCondition implements Condition {
	protected Inventory format;
	protected Object obj1;
	protected Object obj2;

	public AbstractCondition(Inventory format, Object obj1, Object obj2) {
		this.format = format;
		this.obj1 = obj1;
		this.obj2 = obj2;
	}

	@Override
	public boolean process(PlayerWrapper player, PlayerItemInfo info) {
		Object ob1 = this.obj1;
		Object ob2 = this.obj2;
		if (ob1 instanceof Operation) {
			ob1 = ((Operation) ob1).resolveFor(player, info);
		}
		if (ob2 instanceof Operation) {
			ob2 = ((Operation) ob2).resolveFor(player, info);
		}
		if (ob1 instanceof String) {
			ob1 = format.processPlaceholders(player, (String) ob1, info);
		}
		if (ob2 instanceof String) {
			ob2 = format.processPlaceholders(player, (String) ob2, info);
		}
		return process(player, ob1, ob2, info);
	}

	protected abstract boolean process(PlayerWrapper player, Object obj1, Object obj2, PlayerItemInfo info);

	@Override
	public String toString() {
		return this.getClass().getName() + "[format=" + format + ";obj1=" + obj1 + ";obj2=" + obj2 + "]";
	}
}
