package misat11.lib.sgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class GuiAnimator extends BukkitRunnable {
	private GuiHolder holder;
	private Map<PlayerItemInfo, Integer> itemsWithAnimation;
	
	public GuiAnimator(GuiHolder holder, List<PlayerItemInfo> itemsWithAnimation) {
		this.holder = holder;
		this.itemsWithAnimation = new HashMap<PlayerItemInfo, Integer>();
		for (PlayerItemInfo info : itemsWithAnimation) {
			this.itemsWithAnimation.put(info, 0);
		}
	}
	
	@Override
	public void run() {
		if (holder.getPlayer().getOpenInventory().getTopInventory().getHolder() == holder) {
			for (Map.Entry<PlayerItemInfo, Integer> entry : this.itemsWithAnimation.entrySet()) {
				PlayerItemInfo info = entry.getKey();
				int position = entry.getValue();
				List<ItemStack> animation = info.getAnimation();
				if ((animation.size() - 1) < position) {
					position = 0;
				}
				int cpos = (info.getPosition() % this.holder.getFormat().getItemsOnPage()) + this.holder.getFormat().getRenderOffset();
				ItemStack anim = animation.get(position).clone();
				if (anim.hasItemMeta()) {
					ItemMeta meta = anim.getItemMeta();
					if (meta.hasDisplayName()) {
						meta.setDisplayName(this.holder.getFormat().processPlaceholders(this.holder.getPlayer(), meta.getDisplayName(), info));
					}
					if (meta.hasLore()) {
						List<String> lore = new ArrayList<String>();
						for (String str : meta.getLore()) {
							lore.add(this.holder.getFormat().processPlaceholders(this.holder.getPlayer(), str, info));
						}
						meta.setLore(lore);
					}
					anim.setItemMeta(meta);
				}
				this.holder.safePutStackToInventory(cpos, anim);
				this.itemsWithAnimation.put(info, position + 1);
				
			}
		} else {
			this.cancel();
		}
	}
}
