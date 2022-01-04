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

import org.bukkit.Bukkit;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.lib.player.PlayerWrapper;

public class PAPIPlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments) {
		var format = String.join(".", arguments);
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			var hooks = PlaceholderAPI.getPlaceholders();
			int index = format.indexOf("_");
			if (index <= 0 || index >= format.length()) {
				return "%" + format + "%"; // ignore this placeholder
			}
			var identifier = format.substring(0, index).toLowerCase();
			var params = format.substring(index + 1);
			if (hooks.containsKey(identifier)) {
				var value = hooks.get(identifier).onPlaceholderRequest(player.as(Player.class), params);
				if (value != null) {
					return value;
				}
			}
		}
		return "%" + format + "%"; // ignore this placeholder
	}
}
