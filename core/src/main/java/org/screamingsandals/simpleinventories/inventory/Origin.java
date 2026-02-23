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

import java.io.File;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.screamingsandals.simpleinventories.builder.FormatBuilder;
import org.screamingsandals.simpleinventories.item.BuyCallback;
import org.screamingsandals.simpleinventories.item.PostClickCallback;
import org.screamingsandals.simpleinventories.item.PreClickCallback;
import org.screamingsandals.simpleinventories.item.RenderCallback;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Origin {

	private final File file;
	private final List<Object> content;
	private final Type type;
	private final FormatBuilder builder;
	private final ConfigurationSection optionsSection;

	private List<OpenCallback> openCallbacks = Collections.emptyList();
	private List<RenderCallback> renderCallbacks = Collections.emptyList();
	private List<PreClickCallback> preClickCallbacks = Collections.emptyList();
	private List<BuyCallback> buyCallbacks = Collections.emptyList();
	private List<PostClickCallback> postClickCallbacks = Collections.emptyList();
	private List<CloseCallback> closeCallbacks = Collections.emptyList();
	
	public Origin(List<Object> content) {
		this(null, content, Type.INTERNAL, null, null);
	}
	
	public Origin(File file, List<Object> content) {
		this(file, content, Type.FILE, null, null);
	}

	public Origin(File file, List<Object> content, ConfigurationSection optionsSection) {
		this(file, content, Type.FILE, null, optionsSection);
	}
	
	public Origin(FormatBuilder builder, List<Object> content) {
		this(null, content, Type.BUILDER, builder, null);
	}
	
	public static enum Type {
		FILE,
		BUILDER,
		INTERNAL;
	}

}
