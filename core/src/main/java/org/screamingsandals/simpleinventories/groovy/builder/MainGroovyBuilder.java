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

package org.screamingsandals.simpleinventories.groovy.builder;

import groovy.lang.Closure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.simpleinventories.events.*;
import org.screamingsandals.simpleinventories.groovy.callback.*;
import org.screamingsandals.simpleinventories.inventory.CloseCallback;
import org.screamingsandals.simpleinventories.inventory.LocalOptions;
import org.screamingsandals.simpleinventories.inventory.OpenCallback;
import org.screamingsandals.simpleinventories.item.BuyCallback;
import org.screamingsandals.simpleinventories.item.PostClickCallback;
import org.screamingsandals.simpleinventories.item.PreClickCallback;
import org.screamingsandals.simpleinventories.item.RenderCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MainGroovyBuilder extends GroovyBuilder {
    private final LocalOptions localOptions;

    private final List<RenderCallback> renderCallbacks = new ArrayList<>();
    private final List<PreClickCallback> preClickCallbacks = new ArrayList<>();
    private final List<PostClickCallback> postClickCallbacks = new ArrayList<>();
    private final List<OpenCallback> openCallbacks = new ArrayList<>();
    private final List<CloseCallback> closeCallbacks = new ArrayList<>();
    private final List<BuyCallback> buyCallbacks = new ArrayList<>();

    private final List<Object> list = new ArrayList<>();

    public void define(String definition) {
        putItem(new HashMap<String, Object>() {
            {
                put("define", definition);
            }
        });
    }

    @Override
    public IGroovyLocalOptionsBuilder getCategoryOptions() {
        return new GroovyDirectOptionsBuilder(localOptions);
    }

    @Override
    protected void putItem(Object object) {
        if (!list.contains(object)) {
            list.add(object);
        }
    }

    public void render(Closure<GroovyRenderCallback.GroovyRenderBuilder> closure) {
        renderCallbacks.add(new GroovyRenderCallback(closure));
    }

    public void preClick(Closure<PreActionEvent> closure) {
        preClickCallbacks.add(new GroovyPreClickCallback(closure));
    }

    public void click(Closure<PostActionEvent> closure) {
        postClickCallbacks.add(new GroovyPostClickCallback(closure));
    }

    public void open(Closure<OpenInventoryEvent> closure) {
        openCallbacks.add(new GroovyOpenCallback(closure));
    }

    public void close(Closure<CloseInventoryEvent> closure) {
        closeCallbacks.add(new GroovyCloseCallback(closure));
    }

    public void buy(Closure<ShopTransactionEvent> closure) {
        buyCallbacks.add(new GroovyBuyCallback(closure));
    }
}
