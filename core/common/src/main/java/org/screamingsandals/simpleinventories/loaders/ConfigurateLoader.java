/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.simpleinventories.loaders;

import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.simpleinventories.builder.BuilderUtils;
import org.screamingsandals.simpleinventories.builder.ItemInfoBuilder;
import org.screamingsandals.simpleinventories.inventory.*;
import org.screamingsandals.simpleinventories.operations.OperationParser;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Supplier;

@RequiredArgsConstructor(staticName = "of")
public class ConfigurateLoader implements ILoader {
    private final Supplier<AbstractConfigurationLoader.Builder<?,?>> configurationLoaderBuilder;

    @Override
    public void loadPathInto(SubInventory subInventory, Path path, String configPath) throws Exception {
        var loader = configurationLoaderBuilder.get()
                .path(path)
                .build();

        final ConfigurationNode root = loader.load();

        var configKeys = configPath.split("\\.");
        var node = root.node((Object[]) configKeys);

        loadConfigurationNodeInto(subInventory, node);
    }

    public static void loadConfigurationNodeInto(SubInventory subInventory, ConfigurationNode configurationNode) {
        configurationNode.childrenList().forEach(itemNode -> {
            try {
                if (itemNode.isMap()) {
                    if (itemNode.hasChild("define")) {
                        //noinspection ConstantConditions
                        BuilderUtils.buildDefinition(subInventory.getInventorySet(), itemNode.node("define").getString());
                    } else if (itemNode.hasChild("include")) {
                        //noinspection ConstantConditions
                        subInventory.getWaitingQueue().add(Include.of(itemNode.node("include").getString()));
                    } else if (itemNode.hasChild("insert")) {
                        var insert = itemNode.node("insert");
                        var items = itemNode.node("items");
                        var inserts = new ArrayList<String>();
                        if (insert.isList()) {
                            inserts.addAll(insert.getList(String.class));
                        } else {
                            inserts.add(insert.getString());
                        }
                        var insertContainer = new SubInventory(false, null, subInventory.getInventorySet());
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

                        var item = new GenericItemInfo(subInventory.getInventorySet());

                        var builtItem = ItemStackFactory.build(stack);
                        item.setItem(builtItem != null ? builtItem : ItemStackFactory.getAir());

                        subInventory.getWaitingQueue().add(item);

                        var builder = ItemInfoBuilder.of(item);
                        if (!absolute.empty()) {
                            builder.absolute(absolute.getInt());
                        }
                        if (!animation.empty() && animation.isList()) {
                            animation.childrenList().forEach(builder.getAnimation()::stack);
                        }
                        if (!clone.empty()) {
                            builder.clone(clone.getString());
                        }
                        if (!cloneMethod.empty()) {
                            //noinspection ConstantConditions
                            builder.cloneMethod(cloneMethod.getString());
                        }
                        if (!column.empty()) {
                            try {
                                //noinspection ConstantConditions
                                builder.column(column.get(Integer.class)); // I need this to fail if it's not number
                            } catch (SerializationException | NullPointerException ignored) {
                                //noinspection ConstantConditions
                                builder.column(column.getString());
                            }
                        }
                        if (!conditions.empty()) {
                            conditions.childrenList().forEach(condition -> {
                                var statement = condition.node("if");
                                var thenBody = condition.node("then");
                                var elseBody = condition.node("else");
                                if (!statement.empty() && (!thenBody.empty() || !elseBody.empty())) {
                                    var parsedCondition = OperationParser.getFinalCondition(item.getFormat(), statement.getString());
                                    builder.render(itemRenderEvent -> {
                                        if (parsedCondition.process(itemRenderEvent.getPlayer(), itemRenderEvent.getItem())) {
                                            if (!thenBody.empty()) {
                                                var visibleRender = thenBody.node("visible");
                                                var disabledRender = thenBody.node("disabled");
                                                var stackRender = thenBody.node("stack");
                                                var animationRender = thenBody.node("animation");

                                                if (!visibleRender.empty()) {
                                                    itemRenderEvent.setVisible(visibleRender.getBoolean());
                                                }

                                                if (!disabledRender.empty()) {
                                                    itemRenderEvent.setDisabled(disabledRender.getBoolean());
                                                }

                                                if (!stackRender.empty()) {
                                                    var anotherBuiltItem = ItemStackFactory.build(stackRender);
                                                    itemRenderEvent.setStack(anotherBuiltItem != null ? anotherBuiltItem : itemRenderEvent.getStack());
                                                }

                                                if (!animationRender.empty()) {
                                                    itemRenderEvent.getAnimation().clear();
                                                    animationRender.childrenList().forEach(itemRenderEvent.getAnimation()::stack);
                                                }
                                            }
                                        } else if (!elseBody.empty()) {
                                                var visibleRender = elseBody.node("visible");
                                                var disabledRender = elseBody.node("disabled");
                                                var stackRender = elseBody.node("stack");
                                                var animationRender = elseBody.node("animation");

                                                if (!visibleRender.empty()) {
                                                    itemRenderEvent.setVisible(visibleRender.getBoolean());
                                                }

                                                if (!disabledRender.empty()) {
                                                    itemRenderEvent.setDisabled(disabledRender.getBoolean());
                                                }

                                                if (!stackRender.empty()) {
                                                    var anotherBuiltItem = ItemStackFactory.build(stackRender);
                                                    itemRenderEvent.setStack(anotherBuiltItem != null ? anotherBuiltItem : itemRenderEvent.getStack());
                                                }

                                                if (!animationRender.empty()) {
                                                    itemRenderEvent.getAnimation().clear();
                                                    animationRender.childrenList().forEach(itemRenderEvent.getAnimation()::stack);
                                                }
                                            }
                                    });
                                }
                            });
                        }
                        if (!disabled.empty()) {
                            builder.disabled(disabled.getString());
                        }
                        if (!execute.empty()) {
                            if (execute.isList()) {
                                builder.execute(execute.getList(String.class));
                            } else {
                                builder.execute(execute.getString());
                            }
                        }
                        if (!id.empty()) {
                            builder.id(id.getString());
                        }
                        if (!linebreak.empty()) {
                            //noinspection ConstantConditions
                            builder.linebreak(linebreak.getString());
                        }
                        if (!locate.empty()) {
                            builder.locate(locate.getString());
                        }
                        if (!options.empty()) {
                            builder.getSubInventory().getLocalOptions().fromNode(options);
                        }
                        if (!pagebreak.empty()) {
                            //noinspection ConstantConditions
                            builder.pagebreak(pagebreak.getString());
                        }
                        if (!price.empty()) {
                            builder.price(price.getString());
                        }
                        if (!priceType.empty()) {
                            builder.priceType(priceType.getString());
                        }
                        if (!properties.empty()) {
                            if (properties.isList()) {
                                properties.childrenList().forEach(builder::property);
                            } else if (properties.isMap()) {
                                builder.property(properties);
                            } else {
                                builder.property(properties.getString());
                            }
                        }
                        if (!row.empty()) {
                            builder.row(row.getInt());
                        }
                        if (!skip.empty()) {
                            builder.skip(skip.getInt(0));
                        }
                        if (!times.empty()) {
                            builder.times(times.getInt(1));
                        }
                        if (!timesMethods.empty()) {
                            if (timesMethods.isList()) {
                                //noinspection ConstantConditions
                                builder.timesMethods(timesMethods.getList(Object.class));
                            } else {
                                //noinspection ConstantConditions
                                builder.timesMethods(timesMethods.getString());
                            }
                        }
                        if (!visible.empty()) {
                            builder.visible(visible.getString());
                        }
                        if (!write.empty()) {
                            builder.write(write.getBoolean());
                        }

                        subInventory.getInventorySet().getVariableToPropertyMap().forEach((variable, property) -> {
                            var node = itemNode.node(variable);
                            if (!node.empty()) {
                                item.getProperties().add(new Property(subInventory.getInventorySet(), property, node));
                            }
                        });

                        if (!items.empty()) {
                            loadConfigurationNodeInto(builder.getSubInventory(), items);
                        }

                    }
                } else {
                    var string = itemNode.getString();
                    if (string != null) {
                        string = string.trim();
                        if (string.startsWith("define ")) {
                            BuilderUtils.buildDefinition(subInventory.getInventorySet(), string.substring(7));
                        } else if (string.startsWith("@")) {
                            subInventory.getWaitingQueue().add(Include.of(string.substring(1)));
                        } else {
                            subInventory.getWaitingQueue().add(BuilderUtils.buildItem(subInventory.getInventorySet(), string));
                        }
                    }
                }
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        });
    }
}
