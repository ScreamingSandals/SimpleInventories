package org.screamingsandals.simpleinventories;

import org.screamingsandals.simpleinventories.builder.InventoryBuilder;
import org.screamingsandals.simpleinventories.material.MaterialMapping;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.material.meta.EnchantmentMapping;
import org.screamingsandals.simpleinventories.material.meta.PotionMapping;
import org.screamingsandals.simpleinventories.utils.ArgumentConverter;
import org.screamingsandals.simpleinventories.utils.ResultConverter;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.function.Supplier;

public abstract class SimpleInventoriesCore {

    protected ResultConverter<PlayerWrapper> playerResultConverter = ResultConverter.build();
    protected ArgumentConverter<PlayerWrapper> playerArgumentConverter = ArgumentConverter.build();

    private static SimpleInventoriesCore core;

    public static void init(Supplier<SimpleInventoriesCore> supplier) {
        if (core != null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore is already initialized.");
        }

        core = supplier.get();

        assert MaterialMapping.isInitialized();
        assert PotionMapping.isInitialized();
        assert EnchantmentMapping.isInitialized();
        assert ItemFactory.isInitialized();

        core.playerResultConverter.finish();
        core.playerArgumentConverter.finish();


    }

    public static <T> T convertPlayerWrapper(PlayerWrapper player, Class<T> type) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.playerResultConverter.convert(player, type);
    }

    public static <T> PlayerWrapper wrapPlayer(T player) {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return core.playerArgumentConverter.convert(player);
    }

    public static InventoryBuilder builder() {
        return new InventoryBuilder();
    }
}
