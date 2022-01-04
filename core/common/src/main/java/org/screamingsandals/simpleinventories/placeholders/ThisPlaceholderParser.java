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

package org.screamingsandals.simpleinventories.placeholders;

import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.lib.player.PlayerWrapper;

public class ThisPlaceholderParser implements IPlaceholderParser {

	@Override
	public String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments) {
		if (arguments.length > 0) {
			if (arguments.length == 1) {
				switch(arguments[0]) {
					case "stack":
						return item.getStack().getMaterial().platformName();
					case "visible":
						return String.valueOf(item.isVisible());
					case "disabled":
						return String.valueOf(item.isDisabled());
				}
			}
		}
		return "%" + key + "%"; // ignore this placeholder
	}

}
