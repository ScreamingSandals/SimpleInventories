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

package org.screamingsandals.simpleinventories.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.simpleinventories.SimpleInventories;
import org.screamingsandals.simpleinventories.item.ItemInfo;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

@Getter
@RequiredArgsConstructor
public class GenerateItemEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private final SimpleInventories format;
	private final PlayerItemInfo info;
	private final Player player;
	
	@Deprecated
	public ItemInfo getOriginalInfo() {
		return info.getOriginal();
	}

    public ItemStack getStack() {
    	return info.getStack();
    }
    
    public void setStack(ItemStack stack) {
    	info.setStack(stack);
    }
    
    public boolean isVisible() {
    	return info.isVisible();
    }
    
    public void setVisible(boolean visible) {
    	info.setVisible(visible);
    }
    
    public boolean isDisabled() {
    	return info.isDisabled();
    }
    
    public void setDisabled(boolean disabled) {
    	info.setDisabled(disabled);
    }

	public static HandlerList getHandlerList() {
		return GenerateItemEvent.handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return GenerateItemEvent.handlers;
	}
	

}
