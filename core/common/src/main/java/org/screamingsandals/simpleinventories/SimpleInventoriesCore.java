package org.screamingsandals.simpleinventories;

import org.screamingsandals.simpleinventories.builder.InventorySetBuilder;
import org.screamingsandals.simpleinventories.events.EventManager;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.material.Item;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.simpleinventories.utils.BidirectionalConverter;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

public abstract class SimpleInventoriesCore {

    protected final BidirectionalConverter<PlayerWrapper> playerConverter = BidirectionalConverter.build();
    protected final EventManager eventManager = new EventManager(null);
    protected Logger logger;

    protected static SimpleInventoriesCore core;

    public static void init(Supplier<SimpleInventoriesCore> supplier) {
        if (core != null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore is already initialized.");
        }

        core = supplier.get();

        assert ItemFactory.isInitialized();
        assert core.logger != null;

        core.playerConverter.finish();
    }

    public static <T> T convertPlayerWrapper(PlayerWrapper player, Class<T> type) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.playerConverter.convert(player, type);
    }

    public static <T> PlayerWrapper wrapPlayer(T player) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.playerConverter.convert(player);
    }

    public static InventoryRenderer openInventory(PlayerWrapper playerWrapper, SubInventory subInventory) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        var renderer = core.openInventory0(playerWrapper, subInventory);
        renderer.render();
        return renderer;
    }

    public static void closeInventory(PlayerWrapper playerWrapper) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        core.closeInventory0(playerWrapper);
    }

    protected abstract InventoryRenderer openInventory0(PlayerWrapper playerWrapper, SubInventory subInventory);

    protected abstract void closeInventory0(PlayerWrapper playerWrapper);

    public static Logger getLogger() {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.logger;
    }

    public static EventManager getEventManager() {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.eventManager;
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

    public static Object readConfigurationSerializable(Map<String, Object> configuration) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.readConfigurationSerializable0(configuration);
    }

    protected abstract Object readConfigurationSerializable0(Map<String, Object> configuration);

    public static boolean hasPlayerInInventory(PlayerWrapper playerWrapper, Item item) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.hasPlayerInInventory0(playerWrapper, item);
    }

    protected abstract boolean hasPlayerInInventory0(PlayerWrapper playerWrapper, Item item);

    public static List<Item> giveItemsToPlayer(PlayerWrapper playerWrapper, List<Item> items) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.giveItemsToPlayer0(playerWrapper, items);
    }

    protected abstract List<Item> giveItemsToPlayer0(PlayerWrapper playerWrapper, List<Item> items);

    public static List<Item> removeItemsFromPlayer(PlayerWrapper playerWrapper, List<Item> items) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.removeItemsFromPlayer0(playerWrapper, items);
    }

    protected abstract List<Item> removeItemsFromPlayer0(PlayerWrapper playerWrapper, List<Item> items);

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
