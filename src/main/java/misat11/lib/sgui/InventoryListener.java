package misat11.lib.sgui;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import misat11.lib.sgui.events.PostActionEvent;
import misat11.lib.sgui.events.PreActionEvent;
import misat11.lib.sgui.events.ShopTransactionEvent;

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
			Inventory inventory = e.getClickedInventory();
			if (!inventory.equals(primaryInventory)) { // check if inventory with GuiHolder and clicked inventory is
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
				if (back.equals(cur) && slot == 0) {
					if (parent != null) {
						ItemInfo parentOfParent = parent.getParent();
						int pageOfParent = (parent.getPosition() / SimpleGuiFormat.ITEMS_ON_PAGE);
						new GuiHolder(player, format, parentOfParent, pageOfParent);
					}
				} else if (pageBack.equals(cur) && slot == 45) {
					if (page > 0) {
						new GuiHolder(player, format, parent, page - 1);
					}
				} else if (pageForward.equals(cur) && slot == 53) {
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

				} catch (Throwable t) {
				}

				player.getInventory().setItem(heldslot, oldItem);
				return;
			}

			if (format.isGenericShopEnabled()) {
				Map<String, Object> originalData = playersItem.getData();
				if (format.isPriceTypeRequired()) {
					if (originalData.containsKey("price") && originalData.containsKey("price-type")) {
						int price = (int) originalData.get("price");
						String type = (String) originalData.get("price-type");

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
						int price = (int) originalData.get("price");
						String type = (String) originalData.getOrDefault("price-type", "default");

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

			PostActionEvent postEvent = new PostActionEvent(player, format, inventory, parent, playersItem, e.getClick());
			Bukkit.getPluginManager().callEvent(postEvent);

			if (player.getOpenInventory().getTopInventory().getHolder() == holder) {
				holder.repaint();
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

}
