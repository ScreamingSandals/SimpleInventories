package org.screamingsandals.simpleinventories.inventory;

import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;

import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubInventory extends AbstractInventory {
    private final List<GenericItemInfo> items = new ArrayList<>();
    private final boolean main;
    @Nullable
    private final GenericItemInfo itemOwner;
    @ToString.Exclude
    private final Inventory format;

    @Setter(AccessLevel.NONE)
    private int lastpos = 0;

    @Setter(AccessLevel.NONE)
    private GenericItemInfo lastItem;


    private final List<GenericItemInfo> contents = new ArrayList<>();

    /**
     * Used for adding operations and items for future process.
     */
    @ToString.Exclude
    private final Queue<Object> waitingQueue = new LinkedList<>();

    public boolean acceptsLink(String link) {
        if (main && link.equalsIgnoreCase("main")) {
            return true;
        }
        return itemOwner != null
                && (link.startsWith("$") || link.startsWith("§"))
                && link.substring(1).equals(itemOwner.getId());
    }

    @Override
    public LocalOptions getLocalOptions() {
        var options = super.getLocalOptions();
        if (options == null) {
            if (!main) {
                //noinspection ConstantConditions
                options = itemOwner.getParent().getLocalOptions().clone();
            } else {
                options = format.getLocalOptions();
            }
        }
        return options;
    }

    public LocalOptions getLocalOrParentOptions() {
        var options = super.getLocalOptions();
        if (options == null) {
            if (!main) {
                //noinspection ConstantConditions
                options = itemOwner.getParent().getLocalOrParentOptions();
            } else {
                options = format.getLocalOptions();
            }
        }
        return options;
    }

    public int getHighestPage() {
        return contents.stream().mapToInt(item -> item.getPosition() / getLocalOrParentOptions().getItemsOnPage()).distinct().max().orElse(0);
    }

    public void process() {
        format.getInsertQueue().stream().filter(i -> acceptsLink(i.getLink())).forEach(insert -> {
            format.getInsertQueue().remove(insert);
            var clone = new LinkedList<>();
            insert.getSubInventory().getWaitingQueue().stream().map(e -> {
                if (e instanceof GenericItemInfo) {
                    return ((GenericItemInfo) e).clone();
                }
                return e;
            }).forEach(clone::add);
            process(clone);
        });
        process(waitingQueue);
    }
    public void process(Queue<Object> queue) {
        while (!queue.isEmpty()) {
            process(queue.remove());
        }
        SimpleInventoriesCore.getAllInventoryRenderersForSubInventory(this).forEach(InventoryRenderer::render);
    }

    private void process(Object object) {
        if (object instanceof Insert) {
            var insert = (Insert) object;
            var linkedInventory = format.resolveCategoryLink(insert.getLink());
            linkedInventory.ifPresentOrElse(subInventory -> {
                var clone = new LinkedList<>();
                insert.getSubInventory().getWaitingQueue().stream().map(e -> {
                    if (e instanceof GenericItemInfo) {
                        return ((GenericItemInfo) e).clone();
                    }
                    return e;
                }).forEach(clone::add);
                subInventory.process(clone);
            }, () -> format.getInsertQueue().add(insert));
        } else if (object instanceof Include) {
            var include = (Include) object;
            // TODO: reading files
        } else if (object instanceof GenericItemInfo) {
            var item = (GenericItemInfo) object;
            item.setParent(this);
            if (item.getRequestedClone() != null) {
                var clone = item.getRequestedClone();
                if (clone.getCloneLink().equalsIgnoreCase("cosmetic")) {
                    if (clone.getCloneMethod().isOverride() || item.getItem() == null || item.getItem().getMaterial().equals(ItemFactory.getAir().getMaterial())) {
                        item.setItem(format.getLocalOptions().getCosmeticItem());
                    }
                } else {
                    GenericItemInfo originalItem;
                    if (clone.getCloneLink().equalsIgnoreCase("previous")) {
                        originalItem = lastItem;
                    } else {
                        originalItem = format.resolveItemLink(clone.getCloneLink()).orElse(null);
                    }
                    if (originalItem != null) {
                        if (originalItem.getItem() != null && !originalItem.getItem().getMaterial().isAir() && (clone.getCloneMethod().isOverride() || item.getItem() == null || item.getItem().getMaterial().isAir())) {
                            item.setItem(originalItem.getItem());
                        }
                        if (originalItem.hasAnimation()) {
                            if (!item.hasAnimation() || clone.getCloneMethod().isIncrement()) {
                                item.getAnimation().addAll(originalItem.getAnimation());
                            } else if (clone.getCloneMethod().isOverride()) {
                                item.getAnimation().clear();
                                item.getAnimation().addAll(originalItem.getAnimation());
                            }
                        }
                        if (originalItem.getVisible() != null && (clone.getCloneMethod().isOverride() || item.getVisible() == null)) {
                            item.setVisible(originalItem.getVisible());
                        }
                        if (originalItem.getDisabled() != null && (clone.getCloneMethod().isOverride() || item.getDisabled() == null)) {
                            item.setDisabled(originalItem.getDisabled());
                        }
                        item.getProperties().addAll(originalItem.getProperties());
                        if (originalItem.hasBook() && (clone.getCloneMethod().isOverride() || !item.hasBook())) {
                            item.setBook(originalItem.getBook().clone());
                        }
                        if (originalItem.getWritten() != null && (clone.getCloneMethod().isOverride() || item.getWritten() == null)) {
                            item.setWritten(originalItem.getWritten());
                        }
                        item.getEventManager().cloneEventManager(originalItem.getEventManager());
                        if (originalItem.hasChildInventory()) {
                            if (!item.hasChildInventory()) {
                                item.setChildInventory(new SubInventory(false, item, format));
                                originalItem.getChildInventory().getContents().stream().map(GenericItemInfo::clone).forEach(item.getChildInventory().getWaitingQueue()::add);
                            } else if (clone.getCloneMethod().isIncrement()) {
                                originalItem.getChildInventory().getContents().stream().map(GenericItemInfo::clone).forEach(item.getChildInventory().getWaitingQueue()::add);
                            } else if (clone.getCloneMethod().isOverride()) {
                                item.setChildInventory(new SubInventory(false, item, format));
                                originalItem.getChildInventory().getContents().stream().map(GenericItemInfo::clone).forEach(item.getChildInventory().getWaitingQueue()::add);
                            }
                        }
                        if (originalItem.getLocate() != null && (item.getLocate() == null || clone.getCloneMethod().isOverride())) {
                            item.setLocate(originalItem.getLocate());
                        }
                        if (!originalItem.getExecutions().isEmpty()) {
                            if (clone.getCloneMethod().isIncrement() || item.getExecutions().isEmpty()) {
                                item.getExecutions().addAll(originalItem.getExecutions());
                            } else if (clone.getCloneMethod().isOverride()) {
                                item.getExecutions().clear();
                                item.getExecutions().addAll(originalItem.getExecutions());
                            }
                        }
                        if (originalItem.hasData()) {
                            if (!item.hasData()) {
                                item.setData(new HashMap<>());
                            }
                            originalItem.getData().forEach((key, val) -> {
                                if (val instanceof Collection) {
                                    if (!item.getData().containsKey(key)) {
                                        item.getData().put(key, new ArrayList<>((Collection<?>) val));
                                    } else if (clone.getCloneMethod().isIncrement()) {
                                        //noinspection unchecked
                                        ((Collection<Object>) item.getData().get(key)).addAll((Collection<?>) val);
                                    } else if (clone.getCloneMethod().isOverride()) {
                                        item.getData().put(key, new ArrayList<>((Collection<?>) val));
                                    }
                                } else if (!item.getData().containsKey(key) || clone.getCloneMethod().isOverride()) {
                                    if (val instanceof Cloneable) {
                                        try {
                                            item.getData().put(key, val.getClass().getMethod("clone").invoke(val));
                                        } catch (Exception ex) {
                                            item.getData().put(key, val);
                                        }
                                    } else {
                                        item.getData().put(key, val);
                                    }
                                }
                            });
                        }
                    }
                }
            }

            if (item.hasId()) {
                format.getIds().put(item.getId(), item);
            }

            if (item.isWritten()) {
                var newPosition = lastpos;
                if (item.getRequestedPosition() != null) {
                    newPosition = item.getRequestedPosition().calculateThatPosition(newPosition, getLocalOptions().getItemsOnRow(), getLocalOptions().getRows());
                }
                item.setPosition(newPosition);
                if (item.getRequestedPosition() != null) {
                    newPosition += item.getRequestedPosition().calculateNextPositionOffset(newPosition, getLocalOptions().getItemsOnRow(), getLocalOptions().getRows());
                } else {
                    newPosition++;
                }
                lastpos = newPosition;
            }

            contents.add(item);
            lastItem = item;
            if (item.hasChildInventory()) {
                item.getChildInventory().process();
            }
            if (item.getRequestedTimes() != null) {
                int times = item.getRequestedTimes().getRepeat();
                if (times > 1) {
                    var clone = item.clone();
                    //noinspection ConstantConditions
                    clone.getRequestedTimes().setRepeat(times - 1);
                    process(clone);
                }
            }
        } else {
            throw new RuntimeException("Invalid object in queue!");
        }
    }
}