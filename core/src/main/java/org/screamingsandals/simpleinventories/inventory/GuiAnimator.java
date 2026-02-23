/*
 * Copyright (C) 2026 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.simpleinventories.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

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
		if (holder.getFormat().getCurrentGuiHolder(holder.getPlayer()) == holder) {
			for (Map.Entry<PlayerItemInfo, Integer> entry : this.itemsWithAnimation.entrySet()) {
				PlayerItemInfo info = entry.getKey();
				int position = entry.getValue();
				List<ItemStack> animation = info.getAnimation();
				if ((animation.size() - 1) < position) {
					position = 0;
				}
				int cpos = (info.getPosition() % this.holder.getLocalOptions().getItemsOnPage()) + this.holder.getLocalOptions().getRender_offset();
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
