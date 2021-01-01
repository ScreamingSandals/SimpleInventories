package org.screamingsandals.simpleinventories.placeholders;

import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.lib.player.PlayerWrapper;

public interface IPlaceholderParser {
    String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments);
}
