package org.screamingsandals.simpleinventories.placeholders;

import org.screamingsandals.simpleinventories.inventory.PlayerItemInfo;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

public interface IPlaceholderParser {
    String processPlaceholder(String key, PlayerWrapper player, PlayerItemInfo item, String[] arguments);
}
