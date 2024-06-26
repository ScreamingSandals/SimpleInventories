/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.simpleinventories.placeholders;

import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;

import java.util.Objects;

public class PlayerPlaceholderParser implements PlaceholderParser {

	@Override
	public String processPlaceholder(String key, Player playerWrapper, PlayerItemInfo item, String[] arguments) {
		if (arguments.length >= 1) {
			switch (arguments[0]) {
				case "exp":
				case "xp":
					return Float.toString(playerWrapper.getExp());
				case "level":
					return Integer.toString(playerWrapper.getLevel());
				case "displayName":
					return playerWrapper.getDisplayName().toLegacy();
				case "tabName":
					return playerWrapper.getPlayerListName().toLegacy();
				case "health":
					return Double.toString(playerWrapper.getHealth());
				case "maxhealth":
					return Double.toString(Objects.requireNonNull(playerWrapper.getAttribute(AttributeType.of("generic.max_health"))).getValue());
				case "isdead":
				case "dead":
					return Boolean.toString(playerWrapper.isDead());
				case "isalive":
				case "alive":
					return Boolean.toString(!playerWrapper.isDead());
				case "food":
					return Double.toString(playerWrapper.getFoodLevel());
				case "firsttime":
					return Long.toString(playerWrapper.getFirstPlayed());
				case "lasttime":
					return Long.toString(playerWrapper.getLastPlayed());
				case "playertime":
					return Long.toString(playerWrapper.getPlayerTime());
				case "gamemode":
					return playerWrapper.getGameMode().location().path();
			}
		}
		return playerWrapper.getName();
	}
}
