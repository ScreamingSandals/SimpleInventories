/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.simpleinventories.operations.conditions;


import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.lib.player.PlayerWrapper;

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
