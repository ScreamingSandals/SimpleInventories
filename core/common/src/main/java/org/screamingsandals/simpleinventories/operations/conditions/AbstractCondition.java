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

import org.screamingsandals.lib.player.Player;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.operations.Operation;

public abstract class AbstractCondition implements Condition {
	protected InventorySet format;
	protected Object obj1;
	protected Object obj2;

	public AbstractCondition(InventorySet format, Object obj1, Object obj2) {
		this.format = format;
		this.obj1 = obj1;
		this.obj2 = obj2;
	}

	@Override
	public boolean process(Player player, PlayerItemInfo info) {
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

	protected abstract boolean process(Player player, Object obj1, Object obj2, PlayerItemInfo info);

	@Override
	public String toString() {
		return this.getClass().getName() + "[format=" + format + ";obj1=" + obj1 + ";obj2=" + obj2 + "]";
	}
}
