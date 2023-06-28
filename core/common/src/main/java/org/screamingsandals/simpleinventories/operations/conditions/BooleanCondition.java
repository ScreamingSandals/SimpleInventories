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
import org.screamingsandals.simpleinventories.operations.Operation;
import org.screamingsandals.lib.player.Player;

public class BooleanCondition implements Condition {
	protected InventorySet format;
	protected Object obj;

	public BooleanCondition(InventorySet format, Object obj) {
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
