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

package org.screamingsandals.simpleinventories.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.permission.OrPermission;
import cloud.commandframework.permission.Permission;
import lombok.SneakyThrows;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.cloud.CloudConstructor;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.inventory.Include;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.screamingsandals.simpleinventories.VersionInfo;

@Plugin(id = "SimpleInventories", authors = {"Misat11"}, version = VersionInfo.VERSION)
@Init(services = {
        SimpleInventoriesCore.class
})
public class SimpleInventoriesPluginBukkit extends PluginContainer {
    private Map<String, InventorySet> inventories;
    private CommandManager<CommandSender> manager;

    @SneakyThrows
    @Override
    public void enable() {
        this.inventories = new HashMap<>();

        loadSimpleInventories();

        manager = CloudConstructor.construct(CommandExecutionCoordinator.simpleCoordinator());

        var builder = manager.commandBuilder("simpleinventories", "si");

        manager.command(
                builder
                        .literal("list")
                        .permission(OrPermission.of(List.of(Permission.of("simpleinventories.use"), Permission.of("simpleinventories.admin"))))
                        .handler(context -> {
                            context.getSender().sendMessage(Component.text("[SI] ", Color.WHITE).withAppendix(Component.text("Available inventories:", Color.GREEN)));
                            inventories.keySet().forEach(inventory ->
                                context.getSender().sendMessage(Component.text(inventory, Color.GRAY))
                            );
                        })
        );

        manager.command(
                builder
                        .literal("open")
                        .permission(OrPermission.of(List.of(Permission.of("simpleinventories.use"), Permission.of("simpleinventories.admin"))))
                        .argument(StringArgument.of("inventory"))
                        .senderType(Player.class)
                        .handler(context -> {
                            String inventory = context.get("inventory");
                            var inv = inventories.get(inventory);
                            if (inv == null) {
                                context.getSender().sendMessage(Component.text("Inventory " + inventory + " doesn't exist!", Color.RED));
                                return;
                            }
                            context.getSender().as(Player.class).openInventory(inv);
                        })
        );

        manager.command(
                builder
                        .literal("reload")
                        .permission(Permission.of("simpleinventories.admin"))
                        .handler(context -> reload())
        );

        manager.command(
                builder
                        .literal("send")
                        .permission(Permission.of("simpleinventories.admin"))
                        .argument(StringArgument.of("player"))
                        .argument(StringArgument.of("inventory"))
                        .handler(context -> {
                            String playerName = context.get("player");
                            String inventoryName = context.get("inventory");
                            var player = Players.getPlayer(playerName);
                            if (player == null) {
                                context.getSender().sendMessage(Component.text("Player " + playerName + " doesn't exist! or is offline", Color.RED));
                                return;
                            }

                            var inv = inventories.get(inventoryName);
                            if (inv == null) {
                                context.getSender().sendMessage(Component.text("Inventory " + inventoryName + " doesn't exist!", Color.RED));
                                return;
                            }
                            player.openInventory(inv);
                        })
        );

        manager.command(builder
                .meta(CommandMeta.DESCRIPTION, "Command of SimpleInventories plugin")
                .handler(c -> c.getSender().sendMessage(Component.text("whoosh")))
        );


        Server.getConsoleSender().sendMessage(Component.text("=========", Color.GOLD).withAppendix(Component.text("=============  by ScreamingSandals <Misat11, Ceph>", Color.WHITE)));
        Server.getConsoleSender()
                .sendMessage(Component.text("+ Simple ", Color.GOLD).withAppendix(Component.text("Inventories +  ", Color.WHITE), Component.text("Version: " + getPluginDescription().version())));
        Server.getConsoleSender()
                .sendMessage(Component.text("=========", Color.GOLD).withAppendix(Component.text("=============  ", Color.WHITE), getPluginDescription().version().contains("SNAPSHOT") ? Component.text("SNAPSHOT VERSION", Color.RED) : Component.text("STABLE VERSION", Color.GREEN)));

        this.inventories.values().forEach(inv -> {
            try {
                inv.getMainSubInventory().process();
            } catch (Exception e) {
                getLogger().error("Your configuration is bad!");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void disable() {
        Server.getConnectedPlayers().forEach(player ->
                SimpleInventoriesCore.getInventoryRenderer(player).ifPresent(InventoryRenderer::close)
        );
        inventories.clear();
    }

    public void reload() {
        disable();

        this.inventories = new HashMap<>();

        loadSimpleInventories();
    }

    private void loadSimpleInventories() {
        var config = new File(getPluginDescription().dataFolder().toFile(), "config.yml");
        if (!config.exists()) {
            saveResource("config.yml", false);
            saveResource("sample.yml", false);
            saveResource("sample.groovy", false);
        }

        var loader = YamlConfigurationLoader.builder()
                .path(getPluginDescription().dataFolder().resolve("config.yml"))
                .build();

        try {
            var root = loader.load();

            root.node("inventories").childrenMap().forEach((key, configuration) -> {
                //noinspection ConstantConditions
                inventories.put(key.toString(), SimpleInventoriesCore.builder()
                        .genericShop(configuration.node("options", "genericShop").getBoolean())
                        .genericShopPriceTypeRequired(configuration.node("options", "genericShopPriceTypeRequired").getBoolean(true))
                        .animationsEnabled(configuration.node("options", "animationsEnabled").getBoolean())
                        .allowAccessToConsole(configuration.node("options", "allowAccessToConsole").getBoolean())
                        .allowBungeecordPlayerSending(configuration.node("options", "allowBungeecordPlayerSending").getBoolean())
                        .categoryOptions(localOptionsBuilder ->
                            localOptionsBuilder.getLocalOptions().fromNode(configuration.node("options"))
                        )
                        .include(Include.ofSection(configuration.node("file").getString(), configuration.node("section").getString("data")))
                        .process()
                        .getInventorySet());
            });
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }
}
