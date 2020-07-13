package org.screamingsandals.simpleinventories.groovy.builder;

import groovy.lang.Closure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.screamingsandals.simpleinventories.events.PostActionEvent;
import org.screamingsandals.simpleinventories.events.PreActionEvent;
import org.screamingsandals.simpleinventories.events.ShopTransactionEvent;
import org.screamingsandals.simpleinventories.groovy.callback.GroovyBuyCallback;
import org.screamingsandals.simpleinventories.groovy.callback.GroovyPostClickCallback;
import org.screamingsandals.simpleinventories.groovy.callback.GroovyPreClickCallback;
import org.screamingsandals.simpleinventories.groovy.callback.GroovyRenderCallback;
import org.screamingsandals.simpleinventories.item.PlayerItemInfo;

import java.util.*;
import java.util.function.Predicate;

import static org.screamingsandals.simpleinventories.groovy.utils.GroovyUtils.internalCallClosure;

@Getter
@AllArgsConstructor
public class GroovyItemBuilder extends GroovyBuilder {
    private final Map<String, Object> itemMap;

    @Override
    public IGroovyLocalOptionsBuilder getCategoryOptions() {
        if (!itemMap.containsKey("options")) {
            itemMap.put("options", new HashMap<>()); // TODO get parent settings
        }
        return new GroovyMapOptionsBuilder((Map<String, Object>) itemMap.get("options"));
    }

    @Override
    protected void putItem(Object object) {
        if (!itemMap.containsKey("items")) {
            itemMap.put("items", new ArrayList<>());
        }

        List<Object> list = (List<Object>) itemMap.get("items");

        if (!list.contains(object)) {
            list.add(object);
        }
    }

    public void stack(Closure<IGroovyStackBuilder> closure) {
        internalCallClosure(closure, getStack());
    }

    public IGroovyStackBuilder getStack() {
        if (!itemMap.containsKey("stack")) {
            itemMap.put("stack", new HashMap<>());
        }

        return new GroovyLongStackBuilder((Map<String, Object>) itemMap.get("stack"));
    }

    public void price(String price) {
        itemMap.put("price", price);
    }

    public void priceType(String priceType) {
        itemMap.put("price-type", priceType);
    }

    public void property(String name) {
        property(new HashMap<String, Object>() {
            {
                put("name", name);
            }
        });
    }

    public void property(String name, Map<String, Object> map) {
        map.put("name", name);
        property(map);
    }

    public void property(Map<String, Object> map) {
        if (!itemMap.containsKey("properties")) {
            itemMap.put("properties", new ArrayList<>());
        }

        List<Object> list = (List<Object>) itemMap.get("properties");

        if (!list.contains(map)) {
            list.add(map);
        }
    }

    public void absolute(int absolute) {
        itemMap.put("absolute", absolute);
    }

    public void clone(String link) {
        itemMap.put("clone", link);
    }

    public void cloneMethod(String cloneMethod) {
        itemMap.put("clone-method", cloneMethod);
    }

    public void column(int number) {
        itemMap.put("column", number);
    }

    public void column(String colName) {
        itemMap.put("column", colName);
    }

    public void disabled(boolean disabled) {
        itemMap.put("disabled", disabled);
    }

    public void disabled(String condition) {
        itemMap.put("disabled", condition);
    }

    public void disabled(Predicate<PlayerItemInfo> predicate) {
        itemMap.put("disabled", predicate);
    }

    public void id(String id) {
        itemMap.put("id", id);
    }

    public void linebreak(String linebreak) {
        itemMap.put("linebreak", linebreak);
    }

    public void pagebreak(String pagebreak) {
        itemMap.put("pagebreak", pagebreak);
    }

    public void row(int row) {
        itemMap.put("row", row);
    }

    public void skip(int skip) {
        itemMap.put("skip", skip);
    }

    public void times(int times) {
        itemMap.put("times", times);
    }

    public void timesMethods(String method) {
        timesMethods(Collections.singletonList(method));
    }

    public void timesMethods(List<String> methods) {
        itemMap.put("times-methods", methods);
    }

    public void visible(boolean visible) {
        itemMap.put("visible", visible);
    }

    public void visible(String condition) {
        itemMap.put("visible", condition);
    }

    public void visible(Predicate<PlayerItemInfo> predicate) { itemMap.put("visible", predicate); }

    public void write(boolean write) {
        itemMap.put("write", write);
    }

    public GroovyAnimationBuilder getAnimation() {
        if (!itemMap.containsKey("animation")) {
            itemMap.put("animation", new ArrayList<>());
        }

        return new GroovyAnimationBuilder((List<Object>) itemMap.get("animation"));
    }

    public void animation(Closure<GroovyAnimationBuilder> closure) {
        internalCallClosure(closure, getAnimation());
    }

    public void execute(String command) {
        execute(Collections.singletonList(command));
    }

    public void execute(List<String> commands) {
        itemMap.put("execute", commands);
    }

    public void locate(String locate) {
        itemMap.put("locate", locate);
    }

    public void render(Closure<GroovyRenderCallback.GroovyRenderBuilder> closure) {
        if (!itemMap.containsKey("rendercallbacks")) {
            itemMap.put("rendercallbacks", new ArrayList<>());
        }

        List<Object> list = (List<Object>) itemMap.get("rendercallbacks");
        list.add(new GroovyRenderCallback(closure));
    }

    public void preClick(Closure<PreActionEvent> closure) {
        if (!itemMap.containsKey("preclickcallbacks")) {
            itemMap.put("preclickcallbacks", new ArrayList<>());
        }

        List<Object> list = (List<Object>) itemMap.get("preclickcallbacks");
        list.add(new GroovyPreClickCallback(closure));
    }

    public void click(Closure<PostActionEvent> closure) {
        if (!itemMap.containsKey("postclickcallbacks")) {
            itemMap.put("postclickcallbacks", new ArrayList<>());
        }

        List<Object> list = (List<Object>) itemMap.get("postclickcallbacks");
        list.add(new GroovyPostClickCallback(closure));
    }

    public void buy(Closure<ShopTransactionEvent> closure) {
        if (!itemMap.containsKey("buycallbacks")) {
            itemMap.put("buycallbacks", new ArrayList<>());
        }

        List<Object> list = (List<Object>) itemMap.get("buycallbacks");
        list.add(new GroovyBuyCallback(closure));
    }

    /* Minigames only, do nothing with other plugins */
    public void upgrade(Map<String, Object> map) {
        if (!itemMap.containsKey("upgrade")) {
            itemMap.put("upgrade", new HashMap<>());
        }

        Map<String, Object> upgrades = (Map<String, Object>) itemMap.get("upgrade");
        if (!upgrades.containsKey("entities")) {
            upgrades.put("entities", new ArrayList<>());
        }

        List<Object> list = (List<Object>) upgrades.get("entities");
        if (!list.contains(map)) {
            list.add(map);
        }
    }
}
