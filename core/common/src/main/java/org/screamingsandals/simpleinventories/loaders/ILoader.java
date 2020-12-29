package org.screamingsandals.simpleinventories.loaders;

import org.screamingsandals.simpleinventories.inventory.SubInventory;

import java.nio.file.Path;

public interface ILoader {
    void loadPathInto(SubInventory subInventory, Path path, String configPath) throws Exception;
}
