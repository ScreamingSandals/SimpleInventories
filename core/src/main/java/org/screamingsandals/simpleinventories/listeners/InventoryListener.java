package org.screamingsandals.simpleinventories.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.events.CloseInventoryEvent;
import org.screamingsandals.simpleinventories.events.PostActionEvent;
import org.screamingsandals.simpleinventories.events.PreActionEvent;
import org.screamingsandals.simpleinventories.events.ShopTransactionEvent;
import org.screamingsandals.simpleinventories.inventory.GuiHolder;
import org.screamingsandals.simpleinventories.inventory.LocalOptions;
import org.screamingsandals.simpleinventories.item.*;
import org.screamingsandals.simpleinventories.utils.BookUtils;
import org.screamingsandals.simpleinventories.utils.MapReader;

import java.util.Collections;
import java.util.List;

public class InventoryListener implements Listener {

    /**
     * This method will register listeners for using SimpleInventories library. This method you must call in onEnable function.
     *
     * @param plugin Plugin that should own this listener
     */
    public static void init(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled() || !(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Inventory primaryInventory = e.getInventory();

        InventoryHolder possibleHolder = primaryInventory.getHolder();
        if (GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.containsKey(primaryInventory)) {
            possibleHolder = GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.get(primaryInventory);
        }

        if (possibleHolder instanceof GuiHolder) {
            Player player = (Player) e.getWhoClicked();
            Inventory inventory = getInventory(e.getView(), e.getRawSlot());
            if (!primaryInventory.equals(inventory)) { // check if inventory with GuiHolder and clicked inventory is
                // same
                if (e.getClick().isShiftClick() || e.getClick().isKeyboardClick() || e.getClick().isCreativeAction()) {
                    e.setCancelled(true);
                }
                return;
            }
            e.setCancelled(true);
            GuiHolder holder = (GuiHolder) possibleHolder;
            ItemInfo parent = holder.getParent();
            int page = holder.getPage();
            int slot = e.getSlot();
            SimpleInventories format = holder.getFormat();
            PlayerItemInfo playersItem = holder.getItemInfoOnPosition(slot);
            ItemInfo originalItem = playersItem != null ? playersItem.getOriginal() : null;
            LocalOptions localOptions = parent != null ? parent.getLocalOptions() : format.getLocalOptions();

            PreActionEvent event = new PreActionEvent(player, format, inventory, parent, playersItem, e.getClick());

            if (playersItem != null && playersItem.getReader().containsKey("preclickcallbacks")) {
                List<Object> preclickcallbacks = (List<Object>) playersItem.getReader().get("preclickcallbacks");
                preclickcallbacks.forEach(callback -> ((PreClickCallback) callback).preClick(event));
            }

            format.getPreClickCallbacks().forEach(preClickCallback -> preClickCallback.preClick(event));

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                player.closeInventory();
                return;
            }

            if (playersItem == null) {
                ItemStack cur = e.getCurrentItem();
                ItemStack back = localOptions.getBackItem();
                ItemStack pageBack = localOptions.getPageBackItem();
                ItemStack pageForward = localOptions.getPageForwardItem();
                if (back.equals(cur) && slot == localOptions.getRender_header_start()) {
                    if (parent != null) {
                        ItemInfo parentOfParent = parent.getParent();
                        LocalOptions localOptionsOfParentOfParent = parentOfParent != null ? parentOfParent.getLocalOptions() : format.getLocalOptions();
                        int pageOfParent = 0;
                        if (parent.isWritten()) {
                            pageOfParent = (parent.getPosition() / localOptionsOfParentOfParent.getItemsOnPage());
                        }
                        new GuiHolder(player, format, parentOfParent, pageOfParent);
                    }
                } else if (pageBack.equals(cur) && slot == localOptions.getRender_footer_start()) {
                    if (page > 0) {
                        new GuiHolder(player, format, parent, page - 1);
                    }
                } else if (pageForward.equals(cur) && slot == (localOptions.getRender_footer_start() + localOptions.getItems_on_row() - 1)) {
                    if (format.getLastPageNumbers().get(parent) > page) {
                        new GuiHolder(player, format, parent, page + 1);
                    }
                }
                return;
            }

            if (playersItem.isDisabled()) {
                return;
            }

            if (format.getDynamicInfo().containsKey(originalItem)) {
                new GuiHolder(player, format, originalItem, 0);
                return;
            }

            if (playersItem.getOriginal().hasBook()) {
                player.closeInventory();

                ItemStack book = playersItem.getOriginal().getBook();
                int heldslot = player.getInventory().getHeldItemSlot();
                ItemStack oldItem = player.getInventory().getItem(heldslot);
                player.getInventory().setItem(heldslot, book);

                BookUtils.openBook(player);

                player.getInventory().setItem(heldslot, oldItem);
                return;
            }

            MapReader originalData = playersItem.getReader();
            if (originalData.containsKey("locate")) {
                String locate = originalData.getString("locate");
                if (locate.startsWith("§") || locate.startsWith("$")) {
                    ItemInfo info = format.findItemInfoById(locate);
                    if (info != null && format.getDynamicInfo().containsKey(info)) {
                        new GuiHolder(player, format, info, 0);
                        return;
                    }
                } else if (locate.equalsIgnoreCase("main")) {
                    new GuiHolder(player, format, null, 0);
                    return;
                }
            }

            if (format.isGenericShopEnabled() && originalData.containsKey("price") && (originalData.containsKey("price-type") || !format.isPriceTypeRequired())) {
                int price = originalData.getInt("price");
                String type = originalData.getString("price-type", "default");

                ShopTransactionEvent shopEvent = new ShopTransactionEvent(player, format, playersItem, price,
                        type, e.getClick());

                if (playersItem.getReader().containsKey("buycallbacks")) {
                    List<Object> buycallbacks = (List<Object>) playersItem.getReader().get("buycallbacks");
                    buycallbacks.forEach(callback -> ((BuyCallback) callback).buy(shopEvent));
                }

                format.getBuyCallbacks().forEach(buyCallback -> buyCallback.buy(shopEvent));

                Bukkit.getPluginManager().callEvent(shopEvent);

                if (player.getOpenInventory().getTopInventory().getHolder() == holder) {
                    holder.repaint();
                }
                return;
            }

            if (originalData.containsKey("execute")) {
                Object obj = originalData.get("execute");
                List<String> list = obj instanceof List ? (List<String>) obj : Collections.singletonList(obj.toString());
                for (String str : list) {
                    str = str.trim();
                    if (str.startsWith("console:")) {
                        if (format.isAllowedToExecuteConsoleCommands()) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), str.substring(8).trim());
                        }
                    } else if (str.startsWith("bungee:")) {
                        if (format.isAllowBungeecord()) {
                            final String server = str.substring(7).trim();
                            Bukkit.getScheduler().runTask(format.getPluginForRunnables(), () -> {
                                try {
                                    if (!Bukkit.getMessenger().getOutgoingChannels(format.getPluginForRunnables()).contains("BungeeCord")) {
                                        Bukkit.getMessenger().registerOutgoingPluginChannel(format.getPluginForRunnables(), "BungeeCord");
                                    }

                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();

                                    out.writeUTF("Connect");
                                    out.writeUTF(server);

                                    player.sendPluginMessage(format.getPluginForRunnables(), "BungeeCord", out.toByteArray());
                                } catch (Throwable throwable) {
                                    System.out.println("Something went wrong while teleporting player through bungeecord: " + throwable.getMessage());
                                    throwable.printStackTrace();
                                }
                            });
                        }
                    } else {
                        if (str.startsWith("player:")) {
                            str = str.substring(7).trim();
                        }
                        Bukkit.getServer().dispatchCommand(player, str);
                    }
                }
            }

            PostActionEvent postEvent = new PostActionEvent(player, format, inventory, parent, playersItem, e.getClick());

            if (playersItem.getReader().containsKey("postclickcallbacks")) {
                List<Object> postclickcallbacks = (List<Object>) playersItem.getReader().get("postclickcallbacks");
                postclickcallbacks.forEach(callback -> ((PostClickCallback) callback).postClick(postEvent));
            }

            format.getPostClickCallbacks().forEach(postClickCallback -> postClickCallback.postClick(postEvent));

            Bukkit.getPluginManager().callEvent(postEvent);

            if (player.getOpenInventory().getTopInventory().getHolder() == holder) {
                holder.repaint();
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        InventoryHolder possibleHolder = inventory.getHolder();
        if (GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.containsKey(inventory)) {
            possibleHolder = GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.get(inventory);
        }

        if (possibleHolder instanceof GuiHolder) {
            GuiHolder holder = (GuiHolder) possibleHolder;
            SimpleInventories format = holder.getFormat();

            CloseInventoryEvent closeInventoryEvent = new CloseInventoryEvent(player, format, holder, inventory);

            format.getCloseCallbacks().forEach(closeCallback -> closeCallback.close(closeInventoryEvent));

            Bukkit.getPluginManager().callEvent(closeInventoryEvent);

            if (closeInventoryEvent.isCancelled()) {
                format.openForPlayer(player);
            } else if (inventory.getHolder() != holder) {
                GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.remove(inventory);
            }
        }
    }

    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {
        if (event.isCancelled() || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Inventory primaryInventory = event.getInventory();

        InventoryHolder possibleHolder = primaryInventory.getHolder();
        if (GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.containsKey(primaryInventory)) {
            possibleHolder = GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.get(primaryInventory);
        }

        if (possibleHolder instanceof GuiHolder) {
            Player player = (Player) event.getWhoClicked();
            for (int slot : event.getRawSlots()) {
                Inventory inventory = getInventory(event.getView(), slot);
                if (primaryInventory.equals(inventory)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        InventoryHolder possibleHolder = event.getSource().getHolder();
        if (GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.containsKey(event.getSource())) {
            possibleHolder = GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.get(event.getSource());
        }

        InventoryHolder possibleDestHolder = event.getDestination().getHolder();
        if (GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.containsKey(event.getDestination())) {
            possibleDestHolder = GuiHolder.TILE_ENTITY_HOLDER_CONVERTOR.get(event.getDestination());
        }

        if (possibleHolder instanceof GuiHolder || possibleDestHolder instanceof GuiHolder) {
            event.setCancelled(true);
        }
    }

    // implement this method here for fix it on older craftbukkit servers
    public final Inventory getInventory(InventoryView view, int rawSlot) {
        if (rawSlot == InventoryView.OUTSIDE) {
            return null;
        }

        if (rawSlot < view.getTopInventory().getSize()) {
            return view.getTopInventory();
        } else {
            return view.getBottomInventory();
        }
    }

}
