/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.simpleinventories.bukkit;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.bukkit.action.BukkitClickActionHandler;
import org.screamingsandals.simpleinventories.bukkit.action.BukkitCloseInventoryActionHandler;
import org.screamingsandals.simpleinventories.bukkit.action.BukkitItemDragActionHandler;
import org.screamingsandals.simpleinventories.bukkit.action.BukkitItemMoveActionHandler;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;
import org.screamingsandals.simpleinventories.bukkit.placeholders.WorldPlaceholderParser;
import org.screamingsandals.simpleinventories.bukkit.render.BukkitInventoryRenderer;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.screamingsandals.simpleinventories.placeholders.IPlaceholderParser;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.lib.player.Player;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Service
public class SimpleInventoriesBukkit extends SimpleInventoriesCore {
    private final Plugin plugin;

    public SimpleInventoriesBukkit(Plugin plugin) {
        super(plugin.getLogger());
        this.plugin = plugin;
    }

    @OnEnable
    public void onEnable() {
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
    protected BukkitInventoryRenderer openInventory0(Player playerWrapper, SubInventory subInventory) {
        return new BukkitInventoryRenderer(playerWrapper, subInventory, 0);
    }

    @Override
    protected InventoryRenderer getInventoryRenderer0(Player playerWrapper) {
        var player = playerWrapper.as(org.bukkit.entity.Player.class);
        return getInventoryRenderer0(player);
    }

    protected InventoryRenderer getInventoryRenderer0(org.bukkit.entity.Player player) {
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
        return Bukkit.getOnlinePlayers().stream()
                .map(this::getInventoryRenderer0)
                .filter(Objects::nonNull)
                .filter(inventoryRenderer -> inventoryRenderer.getSubInventory() == subInventory)
                .collect(Collectors.toList());
    }

    @Override
    protected List<InventoryRenderer> getAllInventoryRenderersForInventorySet0(InventorySet inventorySet) {
        return Bukkit.getOnlinePlayers().stream()
                .map(this::getInventoryRenderer0)
                .filter(Objects::nonNull)
                .filter(inventoryRenderer -> inventoryRenderer.getSubInventory().getInventorySet() == inventorySet)
                .collect(Collectors.toList());
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
    protected void registerPlatformSpecificPlaceholders0(Map<String, IPlaceholderParser> placeholders) {
        placeholders.put("world", new WorldPlaceholderParser());
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
