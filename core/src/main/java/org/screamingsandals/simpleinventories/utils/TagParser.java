package org.screamingsandals.simpleinventories.utils;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TagParser {
    @NotNull ItemStack applyTag(@NotNull ItemStack stack, @NotNull String tag, int dataVersion) throws Exception;
}
