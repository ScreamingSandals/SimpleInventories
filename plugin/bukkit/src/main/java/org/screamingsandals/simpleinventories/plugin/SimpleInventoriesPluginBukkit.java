package org.screamingsandals.simpleinventories.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.permission.OrPermission;
import cloud.commandframework.permission.Permission;
import lombok.SneakyThrows;
import org.screamingsandals.lib.bukkit.command.PaperScreamingCloudManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.bukkit.SimpleInventoriesBukkit;
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
    private PaperScreamingCloudManager manager;

    @SneakyThrows
    @Override
    public void enable() {
        this.inventories = new HashMap<>();

        loadSimpleInventories();

        // TODO: use Slib instead of platform call
        manager = new PaperScreamingCloudManager(
                getPluginDescription().as(org.bukkit.plugin.Plugin.class),
                CommandExecutionCoordinator.simpleCoordinator());

        var builder = manager.commandBuilder("simpleinventories", "si");

        manager.command(
                builder
                        .literal("list")
                        .permission(OrPermission.of(List.of(Permission.of("simpleinventories.use"), Permission.of("simpleinventories.admin"))))
                        .handler(context -> {
                            context.getSender().sendMessage("§f[SI] §aAvailable inventories:");
                            inventories.keySet().forEach(inventory ->
                                context.getSender().sendMessage("§7" + inventory)
                            );
                        })
        );

        manager.command(
                builder
                        .literal("open")
                        .permission(OrPermission.of(List.of(Permission.of("simpleinventories.use"), Permission.of("simpleinventories.admin"))))
                        .argument(StringArgument.of("inventory"))
                        .senderType(PlayerWrapper.class)
                        .handler(context -> {
                            String inventory = context.get("inventory");
                            var inv = inventories.get(inventory);
                            if (inv == null) {
                                context.getSender().sendMessage("§cInventory " + inventory + " doesn't exist!");
                                return;
                            }
                            PlayerMapper.wrapPlayer(context.getSender()).openInventory(inv);
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
                            var player = PlayerMapper.getPlayer(playerName);
                            if (player == null) {
                                context.getSender().sendMessage("§cPlayer " + playerName + " doesn't exist! or is offline");
                                return;
                            }

                            var inv = inventories.get(inventoryName);
                            if (inv == null) {
                                context.getSender().sendMessage("§cInventory " + inventoryName + " doesn't exist!");
                                return;
                            }
                            PlayerMapper.wrapPlayer(player).openInventory(inv);
                        })
        );

        manager.command(builder
                .meta(CommandMeta.DESCRIPTION, "Command of SimpleInventories plugin")
                .handler(c -> c.getSender().sendMessage("whoosh"))
        );


        PlayerMapper.getConsoleSender().sendMessage("§6=========§f=============  by ScreamingSandals <Misat11, Ceph>");
        PlayerMapper.getConsoleSender()
                .sendMessage("§6+ Simple §fInventories +  §6Version: " + getPluginDescription().getVersion());
        PlayerMapper.getConsoleSender()
                .sendMessage("§6=========§f=============  " + (getPluginDescription().getVersion().contains("SNAPSHOT") ? "§cSNAPSHOT VERSION" : "§aSTABLE VERSION"));

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
        PlayerMapper.getPlayers().forEach(player ->
                SimpleInventoriesBukkit.getInventoryRenderer(player).ifPresent(InventoryRenderer::close)
        );
        inventories.clear();
    }

    public void reload() {
        disable();

        this.inventories = new HashMap<>();

        loadSimpleInventories();
    }

    private void loadSimpleInventories() {
        var config = new File(getPluginDescription().getDataFolder().toFile(), "config.yml");
        if (!config.exists()) {
            saveResource("config.yml", false);
            saveResource("sample.yml", false);
            saveResource("sample.groovy", false);
        }

        var loader = YamlConfigurationLoader.builder()
                .path(getPluginDescription().getDataFolder().resolve("config.yml"))
                .build();

        try {
            var root = loader.load();

            root.node("inventories").childrenMap().forEach((key, configuration) -> {
                //noinspection ConstantConditions
                inventories.put(key.toString(), SimpleInventoriesBukkit.builder()
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
