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

import org.screamingsandals.lib.player.Player;
import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RuntimeDefinedPlaceholder implements PlaceholderParser {

	private Map<String, String> map = new HashMap<>();
	private String defstr = null;
	
	@Override
	public String processPlaceholder(String key, Player player, PlayerItemInfo item, String[] arguments) {
		for (var entry : map.entrySet()) {
			var keys = entry.getKey();
			var value = entry.getValue();
			var split = keys.split("(?<!\\.)\\.(?!\\.)");
			if (split.length <= arguments.length) {
				var dollars = new ArrayList<String>();
				for (var i = 0; i < arguments.length && i < split.length; i++) {
					if (!split[i].equals("$") && !arguments[i].equals(split[i])) {
						break;
					}
					if (split[i].equals("$")) {
						dollars.add(arguments[i]);
					}
					if (i == (arguments.length - 1)) {
						var parsedOutput = "";
						var lastIndexOfEscape = -2;
						for (var j = 0; j < value.length(); j++) {
							if (lastIndexOfEscape != j - 1 && value.charAt(j) == '$' && !dollars.isEmpty()) {
								parsedOutput += dollars.get(0);
								dollars.remove(0);
							} else if (lastIndexOfEscape != j - 1 && value.charAt(j) == '\\') {
								lastIndexOfEscape = j;
							} else {
								parsedOutput += value.charAt(j);
							}
						}
						return item.getFormat().processPlaceholders(player, parsedOutput, item);
					}
				}
			}
		}
		if (this.defstr != null) {
			return item.getFormat().processPlaceholders(player, this.defstr, item);
		}
		return null;
	}
	
	public void putDefault(String defstr) {
		this.defstr = defstr;
	}
	
	public void register(String keys, String value) {
		map.put(keys, value);
	}
	
	public void unregister(String keys) {
		map.remove(keys);
	}
	
}
