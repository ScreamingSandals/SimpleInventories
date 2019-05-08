package misat11.lib.sgui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
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
				int cpos = (info.getPosition() % SimpleGuiFormat.ITEMS_ON_PAGE) + SimpleGuiFormat.ITEMS_ON_ROW;
				ItemStack anim = animation.get(position);
				this.holder.getInventory().setItem(cpos, anim);
				this.itemsWithAnimation.put(info, position + 1);
				
			}
		} else {
			this.cancel();
		}
	}
}
