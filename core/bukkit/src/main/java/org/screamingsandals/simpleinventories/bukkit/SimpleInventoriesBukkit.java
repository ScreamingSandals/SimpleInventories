package org.screamingsandals.simpleinventories.bukkit;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.bukkit.action.BukkitClickActionHandler;
import org.screamingsandals.simpleinventories.bukkit.action.BukkitCloseInventoryActionHandler;
import org.screamingsandals.simpleinventories.bukkit.action.BukkitItemDragActionHandler;
import org.screamingsandals.simpleinventories.bukkit.action.BukkitItemMoveActionHandler;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;
import org.screamingsandals.lib.bukkit.material.builder.BukkitItemFactory;
import org.screamingsandals.simpleinventories.bukkit.placeholders.PAPIPlaceholderParser;
import org.screamingsandals.simpleinventories.bukkit.placeholders.PermissionPlaceholderParser;
import org.screamingsandals.simpleinventories.bukkit.placeholders.PlayerPlaceholderParser;
import org.screamingsandals.simpleinventories.bukkit.placeholders.WorldPlaceholderParser;
import org.screamingsandals.simpleinventories.bukkit.render.BukkitInventoryRenderer;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class SimpleInventoriesBukkit extends SimpleInventoriesCore {
    private final Plugin plugin;

    public static void init(Plugin plugin) {
        SimpleInventoriesCore.init(() -> new SimpleInventoriesBukkit(plugin));
    }

    public SimpleInventoriesBukkit(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();

        BukkitItemFactory.init();

        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(Player.class, playerWrapper -> Bukkit.getPlayer(playerWrapper.getUuid()));

        Bukkit.getPluginManager().registerEvents(new BukkitClickActionHandler(), this.plugin);
        Bukkit.getPluginManager().registerEvents(new BukkitCloseInventoryActionHandler(), this.plugin);
        Bukkit.getPluginManager().registerEvents(new BukkitItemDragActionHandler(), this.plugin);
        Bukkit.getPluginManager().registerEvents(new BukkitItemMoveActionHandler(), this.plugin);
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

    @Override
    protected InventoryRenderer getInventoryRenderer0(PlayerWrapper playerWrapper) {
        var player = playerWrapper.as(Player.class);
        if (player != null) {
            return getInventoryRenderer0(player);
        }
        return null;
    }

    protected InventoryRenderer getInventoryRenderer0(Player player) {
        var top = player.getOpenInventory().getTopInventory();
        if (top.getHolder() instanceof AbstractHolder) {
            return ((AbstractHolder) top.getHolder()).getInventoryRenderer();
        } /*else if (GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.containsKey(top)) {
            return GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.get(top);
        }*/
        return null;
    }

    @Override
    protected List<InventoryRenderer> getAllInventoryRenderersForSubInventory0(SubInventory subInventory) {
        return Bukkit.getOnlinePlayers().stream().map(this::getInventoryRenderer0).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    protected Object readConfigurationSerializable0(Map<String, Object> configuration) {
        try {
            return ConfigurationSerialization.deserializeObject(configuration);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return configuration;
        }
    }

    @Override
    protected boolean hasPlayerInInventory0(PlayerWrapper playerWrapper, Item item) {
        return playerWrapper.as(Player.class).getInventory().contains(item.as(ItemStack.class));
    }

    @Override
    protected List<Item> giveItemsToPlayer0(PlayerWrapper playerWrapper, List<Item> items) {
        return playerWrapper.as(Player.class).getInventory()
                .addItem(items.stream().map(item -> item.as(ItemStack.class)).toArray(ItemStack[]::new))
                .values().stream().map(ItemFactory::build)
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    @Override
    protected List<Item> removeItemsFromPlayer0(PlayerWrapper playerWrapper, List<Item> items) {
        return playerWrapper.as(Player.class).getInventory()
                .removeItem(items.stream().map(item -> item.as(ItemStack.class)).toArray(ItemStack[]::new))
                .values().stream().map(ItemFactory::build)
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    @Override
    protected void registerPlatformSpecificPlaceholders0(Map<String, IPlaceholderParser> placeholders) {
        placeholders.put("papi", new PAPIPlaceholderParser());
        placeholders.put("world", new WorldPlaceholderParser());
        placeholders.put("player", new PlayerPlaceholderParser());
        placeholders.put("permission", new PermissionPlaceholderParser());
    }

    @Override
    protected void runJar0(File file) throws Exception {
        var plugin = Bukkit.getPluginManager().loadPlugin(file);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }

    @Override
    protected Path getRootPath0() {
        return plugin.getDataFolder().toPath();
    }

}
