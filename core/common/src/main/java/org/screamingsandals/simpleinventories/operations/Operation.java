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

package org.screamingsandals.simpleinventories.operations;

import org.screamingsandals.lib.player.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;

public interface Operation {
	@Deprecated
	default Object resolveFor(Player player) {
		return resolveFor(player, null);
	}
	
	Object resolveFor(Player player, PlayerItemInfo info);
}
