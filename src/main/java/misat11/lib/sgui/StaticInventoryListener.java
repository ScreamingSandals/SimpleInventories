package misat11.lib.sgui;

import java.util.List;
import java.util.prefs.BackingStoreException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import misat11.lib.sgui.events.PostActionEvent;
import misat11.lib.sgui.events.PreActionEvent;

public class StaticInventoryListener implements Listener {

	private final StaticGuiCreator creator;

	public StaticInventoryListener(StaticGuiCreator creator) {
		this.creator = creator;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.isCancelled() || !(e.getWhoClicked() instanceof Player)) {
			return;
		}

		if (!creator.getInventoriesForHandler().containsKey(e.getInventory())) {
			return;
		}

		e.setCancelled(true);

		Player player = (Player) e.getWhoClicked();
		int slot = e.getSlot();
		Inventory inv = e.getInventory();

		ItemInfo parent = creator.getInventoriesForHandler().get(inv);

		SimpleGuiFormat format = creator.getFormat();

		ItemInfo item = creator.getItemInfoOnPosition(inv, slot);

		PreActionEvent event = new PreActionEvent(player, format, inv, parent, item);

		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			player.closeInventory();
			return;
		}

		if (item == null) {
			List<Inventory> inventories = creator.getInventories(parent);
			int curpg = inventories.indexOf(inv);
			ItemStack cur = e.getCurrentItem();
			ItemStack back = creator.getBackItem();
			ItemStack pageBack = creator.getPageBackItem();
			ItemStack pageForward = creator.getPageForwardItem();
			if (back.equals(cur) && slot == 0) {
				player.openInventory(creator.getInventoryOfItem(parent));
			} else if (pageBack.equals(cur) && slot == 45) {
				if (curpg > 0) {
					player.openInventory(inventories.get(curpg - 1));
				}
			} else if (pageForward.equals(cur) && slot == 53) {
				if (curpg < (inventories.size() - 1)) {
					player.openInventory(inventories.get(curpg + 1));
				}
			}
			return;
		}
		
		List<Inventory> inventor = creator.getInventories(item);
		
		if (inventor != null && !inventor.isEmpty()) {
			player.openInventory(inventor.get(0));
			return;
		}
		
		PostActionEvent postEvent = new PostActionEvent(player, format, inv, parent, item);
		Bukkit.getPluginManager().callEvent(postEvent);

	}
}
