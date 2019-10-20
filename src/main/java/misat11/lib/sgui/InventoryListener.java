package misat11.lib.sgui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import misat11.lib.sgui.events.CloseInventoryEvent;
import misat11.lib.sgui.events.PostActionEvent;
import misat11.lib.sgui.events.PreActionEvent;
import misat11.lib.sgui.events.ShopTransactionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class InventoryListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.isCancelled() || !(e.getWhoClicked() instanceof Player)) {
			return;
		}

		Inventory primaryInventory = e.getInventory();
		if (primaryInventory.getHolder() instanceof GuiHolder) {
			Player player = (Player) e.getWhoClicked();
			e.setCancelled(true);
			Inventory inventory = getInventory(e.getView(), e.getRawSlot()); 
			if (!primaryInventory.equals(inventory)) { // check if inventory with GuiHolder and clicked inventory is
														// same
				return;
			}
			GuiHolder holder = (GuiHolder) inventory.getHolder();
			ItemInfo parent = holder.getParent();
			int page = holder.getPage();
			int slot = e.getSlot();
			SimpleGuiFormat format = holder.getFormat();
			PlayerItemInfo playersItem = holder.getItemInfoOnPosition(slot);
			ItemInfo originalItem = playersItem != null ? playersItem.getOriginal() : null;

			PreActionEvent event = new PreActionEvent(player, format, inventory, parent, playersItem, e.getClick());

			Bukkit.getPluginManager().callEvent(event);

			if (event.isCancelled()) {
				player.closeInventory();
				return;
			}

			if (playersItem == null) {
				ItemStack cur = e.getCurrentItem();
				ItemStack back = format.getBackItem();
				ItemStack pageBack = format.getPageBackItem();
				ItemStack pageForward = format.getPageForwardItem();
				if (back.equals(cur) && slot == format.getRenderHeaderStart()) {
					if (parent != null) {
						ItemInfo parentOfParent = parent.getParent();
						int pageOfParent = (parent.getPosition() / format.getItemsOnPage());
						new GuiHolder(player, format, parentOfParent, pageOfParent);
					}
				} else if (pageBack.equals(cur) && slot == format.getRenderFooterStart()) {
					if (page > 0) {
						new GuiHolder(player, format, parent, page - 1);
					}
				} else if (pageForward.equals(cur) && slot == (format.getRenderFooterStart() + format.getItemsOnRow() - 1)) {
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

				try {
					if (pc == null || ppocp == null || pds == null)
						throw new NullPointerException("Craftbukkit classes not found!");
					int number = Integer.parseInt(version.split("_")[0].substring(1)) * 100
							+ Integer.parseInt(version.split("_")[1]);
					if (number < 113) {
						pc.getMethod("sendPacket", p).invoke(getConnection(player),
								ppocp.getConstructor(String.class, pds).newInstance("MC|BOpen",
										pds.getConstructor(ByteBuf.class).newInstance(
												Unpooled.buffer(256).setByte(0, (byte) 0).writerIndex(1))));
					} else if (number == 113) {
						Class<?> mk = getNMSClass("MinecraftKey");
						pc.getMethod("sendPacket", p).invoke(getConnection(player),
								ppocp.getConstructor(mk, pds).newInstance(
										mk.getMethod("a", String.class).invoke(mk, "minecraft:book_open"),
										pds.getConstructor(ByteBuf.class).newInstance(
												Unpooled.buffer(256).setByte(0, (byte) 0).writerIndex(1))));
					} else if (number > 113) {
						pc.getMethod("sendPacket", p).invoke(getConnection(player), ppoob.getConstructor(eh)
								.newInstance(eh.getDeclaredMethod("valueOf", String.class).invoke(eh, "MAIN_HAND")));
					}

				} catch (Throwable ignored) {
				}

				player.getInventory().setItem(heldslot, oldItem);
				return;
			}

			MapReader originalData = playersItem.getReader();
			if (format.isGenericShopEnabled()) {
				if (format.isPriceTypeRequired()) {
					if (originalData.containsKey("price") && originalData.containsKey("price-type")) {
						int price = originalData.getInt("price");
						String type = originalData.getString("price-type");

						ShopTransactionEvent shopEvent = new ShopTransactionEvent(player, format, playersItem, price,
								type, e.getClick());
						Bukkit.getPluginManager().callEvent(shopEvent);

						if (player.getOpenInventory().getTopInventory().getHolder() == holder) {
							holder.repaint();
						}
						return;
					}
				} else {
					if (originalData.containsKey("price")) {
						int price = originalData.getInt("price");
						String type = originalData.getString("price-type", "default");

						ShopTransactionEvent shopEvent = new ShopTransactionEvent(player, format, playersItem, price,
								type, e.getClick());
						Bukkit.getPluginManager().callEvent(shopEvent);

						if (player.getOpenInventory().getTopInventory().getHolder() == holder) {
							holder.repaint();
						}
						return;
					}
				}
			}
			
			if (originalData.containsKey("execute")) {
				Object obj = originalData.get("execute");
				if (obj instanceof List) {
					List<String> list = (List<String>) obj;
					for (String str : list) {
						str = str.trim();
						if (str.startsWith("console:")) {
							str = str.substring(7).trim();
							if (format.isAllowedToExecuteConsoleCommands()) {
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), str);
							}
						} else {
							if (str.startsWith("player:")) {
								str = str.substring(6).trim();
							}
							Bukkit.getServer().dispatchCommand(player, str);
						}
					}
				} else {
					String str = obj.toString();
					str = str.trim();
					if (str.startsWith("console:")) {
						str = str.substring(7).trim();
						if (format.isAllowedToExecuteConsoleCommands()) {
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), str);
						}
					} else {
						if (str.startsWith("player:")) {
							str = str.substring(6).trim();
						}
						Bukkit.getServer().dispatchCommand(player, str);
					}
				}
			}

			PostActionEvent postEvent = new PostActionEvent(player, format, inventory, parent, playersItem, e.getClick());
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
		if (inventory.getHolder() instanceof GuiHolder) {
			GuiHolder holder = (GuiHolder) inventory.getHolder();
			SimpleGuiFormat format = holder.getFormat();

			CloseInventoryEvent closeInventoryEvent = new CloseInventoryEvent(player, format, holder, inventory);
			Bukkit.getPluginManager().callEvent(closeInventoryEvent);

			if (closeInventoryEvent.isCancelled()) {
				format.openForPlayer(player);
			}
		}
	}

	private static Class<?> getNMSClass(String nmsClassString) {
		try {
			return Class.forName("net.minecraft.server." + version + "." + nmsClassString);
		} catch (ClassNotFoundException ignore) {
		}
		return null;
	}

	private static Object getConnection(Player player) throws SecurityException, NoSuchMethodException,
			NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
		return nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
	}

	private static final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];
	private static final Class<?> ppocp = getNMSClass("PacketPlayOutCustomPayload");
	private static final Class<?> pc = getNMSClass("PlayerConnection");
	private static final Class<?> pds = getNMSClass("PacketDataSerializer");
	private static final Class<?> ppoob = getNMSClass("PacketPlayOutOpenBook");
	private static final Class<?> eh = getNMSClass("EnumHand");
	private static final Class<?> p = getNMSClass("Packet");
	
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
