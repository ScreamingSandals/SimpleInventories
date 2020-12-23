package org.screamingsandals.simpleinventories.loaders;

import org.screamingsandals.simpleinventories.builder.BuilderUtils;
import org.screamingsandals.simpleinventories.builder.ItemInfoBuilder;
import org.screamingsandals.simpleinventories.inventory.GenericItemInfo;
import org.screamingsandals.simpleinventories.inventory.Include;
import org.screamingsandals.simpleinventories.inventory.Insert;
import org.screamingsandals.simpleinventories.inventory.SubInventory;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;

public class ConfigurateLoader {

    public static void loadConfigurationNodeInto(SubInventory subInventory, ConfigurationNode configurationNode) {
        configurationNode.childrenList().forEach(itemNode -> {
            try {
                if (itemNode.isMap()) {
                    if (itemNode.hasChild("define")) {
                        //noinspection ConstantConditions
                        BuilderUtils.buildDefinition(subInventory.getFormat(), itemNode.node("define").getString());
                    } else if (itemNode.hasChild("include")) {
                        subInventory.getWaitingQueue().add(new Include(itemNode.node("include").getString()));
                    } else if (itemNode.hasChild("insert")) {
                        var insert = itemNode.node("insert");
                        var items = itemNode.node("items");
                        var inserts = new ArrayList<String>();
                        if (insert.isList()) {
                            inserts.addAll(insert.getList(String.class));
                        } else {
                            inserts.add(insert.getString());
                        }
                        var insertContainer = new SubInventory(false, null, subInventory.getFormat());
                        inserts.forEach(s -> subInventory.getWaitingQueue().add(new Insert(s, insertContainer)));
                        items.childrenList().forEach(child -> loadConfigurationNodeInto(subInventory, child));
                    } else {
                        var absolute = itemNode.node("absolute");
                        var animation = itemNode.node("animation");
                        var clone = itemNode.node("clone");
                        var cloneMethod = itemNode.node("clone-method");
                        var column = itemNode.node("column");
                        var conditions = itemNode.node("conditions");
                        var disabled = itemNode.node("disabled");
                        var execute = itemNode.node("execute");
                        var id = itemNode.node("id");
                        var items = itemNode.node("items");
                        var linebreak = itemNode.node("linebreak");
                        var locate = itemNode.node("locate");
                        var options = itemNode.node("options");
                        var pagebreak = itemNode.node("pagebreak");
                        var price = itemNode.node("price");
                        var priceType = itemNode.node("price-type");
                        var properties = itemNode.node("properties");
                        var row = itemNode.node("row");
                        var skip = itemNode.node("skip");
                        var stack = itemNode.node("stack");
                        var times = itemNode.node("times");
                        var timesMethods = itemNode.node("times-methods");
                        var visible = itemNode.node("visible");
                        var write = itemNode.node("write");

                        var builder = ItemInfoBuilder.of(new GenericItemInfo(subInventory.getFormat()));

                        // TODO - item loading

                        subInventory.getWaitingQueue().add(builder.getItemInfo());
                    }
                } else {
                    var string = itemNode.getString();
                    if (string != null) {
                        string = string.trim();
                        if (string.startsWith("define ")) {
                            BuilderUtils.buildDefinition(subInventory.getFormat(), string.substring(7));
                        } else if (string.startsWith("@")) {
                            subInventory.getWaitingQueue().add(new Include(string.substring(1)));
                        } else {
                            subInventory.getWaitingQueue().add(BuilderUtils.buildItem(subInventory.getFormat(), string));
                        }
                    }
                }
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        });
    }

    /*
    public static Object transformer(ConfigurationNode node) {
        if (node.empty()) {
            return null;
        }

        if (node.isList()) {
            return node.childrenList().stream().map(ConfigurateLoader::transformer).collect(Collectors.toList());
        } else if (node.isMap()) {
            var map = new HashMap<String, Object>();
            node.childrenMap().entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().toString(), transformer(entry.getValue())))
                    .forEach(entry -> map.put(entry.getKey(), entry.getValue()));
            return map;
        } else {
            // Oh, how to continue here? xdd
        }
    }*/
}
