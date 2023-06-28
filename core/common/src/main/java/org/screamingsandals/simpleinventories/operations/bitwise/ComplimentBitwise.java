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

package org.screamingsandals.simpleinventories.operations.bitwise;

import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.operations.Operation;
import org.screamingsandals.lib.player.Player;

public class ComplimentBitwise implements Operation {
	
	private InventorySet format;
	private Object obj;

	public ComplimentBitwise(InventorySet format, Object obj) {
		this.format = format;
		this.obj = obj;
	}

	@Override
	public Object resolveFor(Player player, PlayerItemInfo info) {
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
