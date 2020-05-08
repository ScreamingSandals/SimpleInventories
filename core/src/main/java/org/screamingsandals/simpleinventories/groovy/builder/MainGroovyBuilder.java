package org.screamingsandals.simpleinventories.groovy.builder;

import groovy.lang.Closure;
import lombok.Getter;
import org.screamingsandals.simpleinventories.events.*;
import org.screamingsandals.simpleinventories.groovy.callback.*;
import org.screamingsandals.simpleinventories.inventory.CloseCallback;
import org.screamingsandals.simpleinventories.inventory.OpenCallback;
import org.screamingsandals.simpleinventories.item.BuyCallback;
import org.screamingsandals.simpleinventories.item.PostClickCallback;
import org.screamingsandals.simpleinventories.item.PreClickCallback;
import org.screamingsandals.simpleinventories.item.RenderCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class MainGroovyBuilder extends GroovyBuilder {
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
