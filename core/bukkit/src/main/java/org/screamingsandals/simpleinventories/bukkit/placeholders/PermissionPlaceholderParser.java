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

package org.screamingsandals.simpleinventories.bukkit.placeholders;

import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.lib.player.PlayerWrapper;

public class PermissionPlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments) {
		if (arguments.length >= 2) {
			String[] permission = new String[arguments.length - 1];
			for (int i = 1; i < arguments.length; i++) {
				permission[i - 1] = arguments[i];
			}
			String perms = String.join(".", permission);
			if (arguments[0].equalsIgnoreCase("has")) {
				return Boolean.toString(player.as(Player.class).hasPermission(perms));
			}
		}
		return "";
	}
}
