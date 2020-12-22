package org.screamingsandals.simpleinventories.bukkit;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.bukkit.material.builder.BukkitItemFactory;
import org.screamingsandals.simpleinventories.bukkit.render.BukkitInventoryRenderer;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.Optional;

@Getter
public class SimpleInventoriesBukkit extends SimpleInventoriesCore {
    private final Plugin plugin;

    public static void init(Plugin plugin) {
        SimpleInventoriesCore.init(() -> new SimpleInventoriesBukkit(plugin));
    }

    public SimpleInventoriesBukkit(Plugin plugin) {
        this.plugin = plugin;

        BukkitItemFactory.init();

        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(Player.class, playerWrapper -> Bukkit.getPlayer(playerWrapper.getUuid()));
    }

    public static Plugin getPlugin() {
        if (core == null) {
            throw new UnsupportedOperationException("SimpleInventoriesCore isn't initialized yet.");
        }
        return ((SimpleInventoriesBukkit) core).plugin;
    }

    @Override
    protected BukkitInventoryRenderer openInventory0(PlayerWrapper playerWrapper, SubInventory subInventory) {
        return new BukkitInventoryRenderer(playerWrapper, subInventory, 0);
    }

    @Override
    protected void closeInventory0(PlayerWrapper playerWrapper) {
        Optional.ofNullable(playerWrapper.as(Player.class)).ifPresent(Player::closeInventory);
    }
}
