package org.screamingsandals.simpleinventories.plugin;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import cloud.commandframework.permission.OrPermission;
import cloud.commandframework.permission.Permission;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.player.PlayerUtils;
import org.screamingsandals.simpleinventories.bukkit.SimpleInventoriesBukkit;
import org.screamingsandals.simpleinventories.inventory.Include;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class SimpleInventoriesPluginBukkit extends JavaPlugin {
    private Map<String, InventorySet> inventories;
    private PaperCommandManager<CommandSender> manager;

    @SneakyThrows
    @Override
    public void onEnable() {
        SimpleInventoriesBukkit.init(this);

        this.inventories = new HashMap<>();

        load();

        manager = new PaperCommandManager<>(this,
                CommandExecutionCoordinator.simpleCoordinator(),
                UnaryOperator.identity(),
                UnaryOperator.identity());

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
                        .senderType(Player.class)
                        .handler(context -> {
                            String inventory = context.get("inventory");
                            var inv = inventories.get(inventory);
                            if (inv == null) {
                                context.getSender().sendMessage("§cInventory " + inventory + " doesn't exist!");
                                return;
                            }
                            PlayerUtils.wrapPlayer(context.getSender()).openInventory(inv);
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
                            var player = Bukkit.getPlayer(playerName);
                            if (player == null) {
                                context.getSender().sendMessage("§cPlayer " + playerName + " doesn't exist! or is offline");
                                return;
                            }

                            var inv = inventories.get(inventoryName);
                            if (inv == null) {
                                context.getSender().sendMessage("§cInventory " + inventoryName + " doesn't exist!");
                                return;
                            }
                            PlayerUtils.wrapPlayer(player).openInventory(inv);
                        })
        );

        manager.command(builder
                .meta(CommandMeta.DESCRIPTION, "Command of SimpleInventories plugin")
                .handler(c -> c.getSender().sendMessage("whoosh"))
        );


        Bukkit.getConsoleSender().sendMessage("§6=========§f=============  by ScreamingSandals <Misat11, Ceph>");
        Bukkit.getConsoleSender()
                .sendMessage("§6+ Simple §fInventories +  §6Version: " + getDescription().getVersion());
        Bukkit.getConsoleSender()
                .sendMessage("§6=========§f=============  " + (getDescription().getVersion().contains("SNAPSHOT") ? "§cSNAPSHOT VERSION" : "§aSTABLE VERSION"));

        this.inventories.values().forEach(inv -> {
            try {
                inv.getMainSubInventory().process();
            } catch (Exception e) {
                getLogger().severe("Your configuration is bad!");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player ->
                SimpleInventoriesBukkit.getInventoryRenderer(PlayerUtils.wrapPlayer(player)).ifPresent(InventoryRenderer::close)
        );
        inventories.clear();
    }

    public void reload() {
        onDisable();

        this.inventories = new HashMap<>();

        load();
    }

    private void load() {
        var config = new File(getDataFolder(), "config.yml");
        if (!config.exists()) {
            saveResource("config.yml", false);
            saveResource("sample.yml", false);
            saveResource("sample.groovy", false);
        }

        var loader = YamlConfigurationLoader.builder()
                .path(Path.of(getDataFolder().toString(), "config.yml"))
                .build();

        try {
            var root = loader.load();

            root.node("inventories").childrenMap().forEach((key, configuration) -> {
                inventories.put(key.toString(), SimpleInventoriesBukkit.builder()
                        .genericShop(configuration.node("options", "genericShop").getBoolean())
                        .genericShopPriceTypeRequired(configuration.node("options", "genericShopPriceTypeRequired").getBoolean(true))
                        .animationsEnabled(configuration.node("options", "animationsEnabled").getBoolean())
                        .allowAccessToConsole(configuration.node("options", "allowAccessToConsole").getBoolean())
                        .allowBungeecordPlayerSending(configuration.node("options", "allowBungeecordPlayerSending").getBoolean())
                        .categoryOptions(localOptionsBuilder ->
                            localOptionsBuilder.getLocalOptions().fromNode(configuration.node("options"))
                        )
                        .include(Include.of(configuration.node("file").getString(), configuration.node("section").getString("data")))
                        .process()
                        .getInventorySet());
            });
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }
}
