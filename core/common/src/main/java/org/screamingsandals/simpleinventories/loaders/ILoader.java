package org.screamingsandals.simpleinventories.loaders;

import org.screamingsandals.simpleinventories.inventory.SubInventory;

import java.io.File;

public interface ILoader {
    void loadFileInto(SubInventory subInventory, File file, String configPath) throws Exception;
}
