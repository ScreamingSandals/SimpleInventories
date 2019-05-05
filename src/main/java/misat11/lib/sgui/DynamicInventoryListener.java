package misat11.lib.sgui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import misat11.lib.sgui.events.PostActionEvent;
import misat11.lib.sgui.events.PreActionEvent;

public class DynamicInventoryListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.isCancelled() || !(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		Inventory inventory = e.getInventory();
		if (inventory.getHolder() instanceof DynamicGuiHolder) {
			Player player = (Player) e.getWhoClicked();
			e.setCancelled(true);
			DynamicGuiHolder holder = (DynamicGuiHolder) e.getInventory().getHolder();
			DynamicGuiCreator creator = holder.getCreator();
			ItemInfo parent = holder.getParent();
			int page = holder.getPage();
			int slot = e.getSlot();
			SimpleGuiFormat format = holder.getFormat();
			ItemInfo item = holder.getItemInfoOnPosition(slot);

			PreActionEvent event = new PreActionEvent(player, format, inventory, parent, item);

			Bukkit.getPluginManager().callEvent(event);

			if (event.isCancelled()) {
				player.closeInventory();
				return;
			}

			if (item == null) {
				ItemStack cur = e.getCurrentItem();
				ItemStack back = creator.getBackItem();
				ItemStack pageBack = creator.getPageBackItem();
				ItemStack pageForward = creator.getPageForwardItem();
				if (back.equals(cur) && slot == 0) {
					// TODO
				} else if (pageBack.equals(cur) && slot == 45) {
					// TODO
				} else if (pageForward.equals(cur) && slot == 53) {
					// TODO
				}
				return;
			}
			
			if (creator.getDynamicInfo().containsKey(item)) {
				new DynamicGuiHolder(player, creator, item, 0);
				return;
			}
			
			PostActionEvent postEvent = new PostActionEvent(player, format, inventory, parent, item);
			Bukkit.getPluginManager().callEvent(postEvent);
			
			if (player.getInventory().equals(inventory)) {
				holder.repaint();
			}
		}
	}

}
