package org.screamingsandals.simpleinventories.operations.conditions;


import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public class GreaterThanCondition extends AbstractCondition {

	public GreaterThanCondition(InventorySet format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(PlayerWrapper player, Object obj1, Object obj2, PlayerItemInfo info) {
		if (obj1 instanceof String) {
			try {
				obj1 = Double.parseDouble((String) obj1);
			} catch (NumberFormatException ex) {
			}
		}
		if (obj2 instanceof String) {
			try {
				obj2 = Double.parseDouble((String) obj2);
			} catch (NumberFormatException ex) {
			}
		}
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return ((Number) obj1).doubleValue() > ((Number) obj2).doubleValue();
		}
		return obj1.toString().length() > obj2.toString().length();
	}

}
