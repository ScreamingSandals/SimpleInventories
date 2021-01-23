package org.screamingsandals.simpleinventories;

import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.utils.annotations.AbstractMapping;
import org.screamingsandals.lib.utils.event.EventManager;
import org.screamingsandals.simpleinventories.builder.InventorySetBuilder;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

@AbstractMapping(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
public abstract class SimpleInventoriesCore {

    protected final EventManager eventManager = new EventManager(null);
    protected Logger logger;

    protected static SimpleInventoriesCore core;

    public static void init(Supplier<SimpleInventoriesCore> supplier) {
        if (core != null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore is already initialized.");
        }

        core = supplier.get();

        assert ItemFactory.isInitialized();
        assert PlayerMapper.isInitialized();
        assert core.logger != null;
    }

    public static boolean isInitialized() {
        return core != null;
    }

    public static InventoryRenderer openInventory(PlayerWrapper playerWrapper, SubInventory subInventory) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        var renderer = core.openInventory0(playerWrapper, subInventory);
        renderer.render();
        return renderer;
    }

    protected abstract InventoryRenderer openInventory0(PlayerWrapper playerWrapper, SubInventory subInventory);

    public static Logger getLogger() {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.logger;
    }

    public static Optional<InventoryRenderer> getInventoryRenderer(PlayerWrapper playerWrapper) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return Optional.ofNullable(core.getInventoryRenderer0(playerWrapper));
    }

    protected abstract InventoryRenderer getInventoryRenderer0(PlayerWrapper playerWrapper);

    public static List<InventoryRenderer> getAllInventoryRenderersForSubInventory(SubInventory subInventory) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.getAllInventoryRenderersForSubInventory0(subInventory);
    }

    protected abstract List<InventoryRenderer> getAllInventoryRenderersForSubInventory0(SubInventory subInventory);

    public static List<InventoryRenderer> getAllInventoryRenderersForInventorySet(InventorySet inventorySet) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.getAllInventoryRenderersForInventorySet0(inventorySet);
    }

    protected abstract List<InventoryRenderer> getAllInventoryRenderersForInventorySet0(InventorySet inventorySet);

    public static Object readConfigurationSerializable(Map<String, Object> configuration) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.readConfigurationSerializable0(configuration);
    }

    protected abstract Object readConfigurationSerializable0(Map<String, Object> configuration);

    public static void registerPlatformSpecificPlaceholders(Map<String, IPlaceholderParser> placeholders) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        core.registerPlatformSpecificPlaceholders0(placeholders);
    }

    protected abstract void registerPlatformSpecificPlaceholders0(Map<String, IPlaceholderParser> placeholders);

    public static void runJar(File file) throws Exception {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        core.runJar0(file);
    }

    protected abstract void runJar0(File file) throws Exception;

    public static Path getRootPath() {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.getRootPath0();
    }

    protected abstract Path getRootPath0();

    public static InventorySetBuilder builder() {
        return InventorySetBuilder.of(new InventorySet());
    }
}
