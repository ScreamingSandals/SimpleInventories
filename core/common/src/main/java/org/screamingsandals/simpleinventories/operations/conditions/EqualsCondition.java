package org.screamingsandals.simpleinventories.operations.conditions;


import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.lib.player.PlayerWrapper;

public class EqualsCondition extends AbstractCondition {

	public EqualsCondition(InventorySet format, Object obj1, Object obj2) {
		super(format, obj1, obj2);
	}

	@Override
	protected boolean process(PlayerWrapper player, Object obj1, Object obj2, PlayerItemInfo info) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return ((Number) obj1).doubleValue() == ((Number) obj2).doubleValue();
		}
		if (obj1 instanceof String && obj2 instanceof String) {
			return ((String) obj1).equals((String) obj2);
		}
		if ((obj1 instanceof Number || obj2 instanceof Number) && (obj1 instanceof String || obj2 instanceof String)) {
			try {
				if (obj1 instanceof Number) {
					obj2 = Double.parseDouble((String) obj2);
				} else {
					obj1 = Double.parseDouble((String) obj1);
				}
				if (((Number) obj1).doubleValue() == ((Number) obj2).doubleValue()) {
					return true;
				}
			} catch (NumberFormatException ex) {
			}
		}
		return false;
	}

}
