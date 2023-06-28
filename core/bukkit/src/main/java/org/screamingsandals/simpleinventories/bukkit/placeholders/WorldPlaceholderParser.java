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

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;

public class WorldPlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, org.screamingsandals.lib.player.Player player, PlayerItemInfo item, String[] arguments) {
		World world = player.as(Player.class).getWorld();
		if (arguments.length >= 1) {
			switch(arguments[0]) {
				case "time":
					return Long.toString(world.getTime());
				case "difficulty":
					return world.getDifficulty().name();
				case "type":
				case "environment":
					return world.getEnvironment().name();
				case "weather":
					return world.getWorldType().name();
				case "pvp":
				case "ispvp":
					return Boolean.toString(world.getPVP());
			}
		}
		return world.getName();
	}
}
