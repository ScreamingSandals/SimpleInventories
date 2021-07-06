package org.screamingsandals.simpleinventories.bukkit.action;

import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.simpleinventories.action.ClickActionHandler;
import org.screamingsandals.simpleinventories.bukkit.SimpleInventoriesBukkit;
import org.screamingsandals.simpleinventories.bukkit.holder.AbstractHolder;
import org.screamingsandals.simpleinventories.bukkit.utils.InventoryUtils;
import org.screamingsandals.simpleinventories.utils.ClickType;
import org.screamingsandals.lib.player.PlayerWrapper;

public class BukkitClickActionHandler extends ClickActionHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled() || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        var primaryInventory = event.getInventory();
        var possibleHolder = primaryInventory.getHolder();
        // TODO: Tile inventory holder converter
        if (possibleHolder instanceof AbstractHolder) {
            var player = (Player) event.getWhoClicked();
            var inventoryRenderer = ((AbstractHolder) possibleHolder).getInventoryRenderer();
            if (!inventoryRenderer.getPlayer().equals(PlayerMapper.wrapPlayer(player))) {
                event.setCancelled(true);
                return; // HOW???
            }
            var inventory = InventoryUtils.getInventory(event.getView(), event.getRawSlot());
            if (!primaryInventory.equals(inventory)) {
                if (event.getClick().isShiftClick() || event.getClick().isKeyboardClick() || event.getClick().isCreativeAction()) {
                    event.setCancelled(true);
                }
                return;
            }
            event.setCancelled(true);
            handleAction(inventoryRenderer, event.getSlot(), ClickType.convert(event.getClick().name()));
        }
    }

    @Override
    protected void dispatchPlayerCommand(PlayerWrapper playerWrapper, String command) {
        playerWrapper.as(Player.class).performCommand(command);
    }

    @Override
    protected void dispatchConsoleCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    @Override
    protected void movePlayerOnProxy(PlayerWrapper playerWrapper, String server) {
        if (!Bukkit.getMessenger().getOutgoingChannels(SimpleInventoriesBukkit.getPlugin()).contains("BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(SimpleInventoriesBukkit.getPlugin(), "BungeeCord");
        }

        //noinspection UnstableApiUsage
        var out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);

        playerWrapper.as(Player.class).sendPluginMessage(SimpleInventoriesBukkit.getPlugin(), "BungeeCord", out.toByteArray());
    }
}
