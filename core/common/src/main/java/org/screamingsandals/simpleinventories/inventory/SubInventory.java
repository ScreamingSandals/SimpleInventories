package org.screamingsandals.simpleinventories.inventory;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.container.Openable;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.simpleinventories.utils.Column;
import org.screamingsandals.simpleinventories.utils.TimesFlags;

import java.util.*;

@Data
public class SubInventory implements Openable, SubInventoryLike<SubInventory> {
    private final boolean main;
    @Nullable
    @ToString.Exclude
    private final GenericItemInfo itemOwner;
    @ToString.Exclude
    @NotNull
    @NonNull // include in required args constructor
    @Setter(onMethod_ = @Deprecated) // unsafe
    private InventorySet inventorySet;
    private final LocalOptions localOptions = new LocalOptions();

    @Setter(AccessLevel.NONE)
    private int cursorPosition = 0;

    @Setter(AccessLevel.NONE)
    private GenericItemInfo lastItem;


    private final List<GenericItemInfo> contents = new ArrayList<>();

    /**
     * Used for adding operations and items for future process.
     */
    @ToString.Exclude
    private final Queue<Queueable> waitingQueue = new LinkedList<>();

    public boolean acceptsLink(String link) {
        if (main && link.equalsIgnoreCase("main")) {
            return true;
        }
        return itemOwner != null
                && (link.startsWith("$") || link.startsWith("§"))
                && link.substring(1).equals(itemOwner.getId());
    }

    @SuppressWarnings("ConstantConditions")
    public LocalOptions getLocalOptions() {
        // while loading parent can be null
        if (!main && itemOwner.getParent() != null) {
            localOptions.setParent(itemOwner.getParent().getLocalOptions());
        } else {
            localOptions.setParent(null);
        }
        return localOptions;
    }

    public int getHighestPage() {
        return contents.stream().mapToInt(item -> item.getPosition() / getLocalOptions().getItemsOnPage()).distinct().max().orElse(0);
    }

    public SubInventory process() {
        List.copyOf(inventorySet.getInsertQueue())
                .stream()
                .filter(Objects::nonNull)
                .filter(i -> acceptsLink(i.getLink()))
                .forEach(insert -> {
                    inventorySet.getInsertQueue().remove(insert);
                    insert.getTemporaryInventory().getQueue().stream().map(e -> {
                        if (e instanceof GenericItemInfo) {
                            return ((GenericItemInfo) e).clone();
                        }
                        return e;
                    }).forEach(this::process);
                });
        return process(waitingQueue);
    }

    public SubInventory process(Queue<Queueable> queue) {
        while (!queue.isEmpty()) {
            process(queue.remove());
        }
        forceReload();
        return this;
    }

    private void process(Queueable object) {
        if (object instanceof Insert) {
            var insert = (Insert) object;
            var linkedInventory = inventorySet.resolveCategoryLink(insert.getLink());
            linkedInventory.ifPresentOrElse(subInventory -> {
                var clone = new LinkedList<Queueable>();
                insert.getTemporaryInventory().getQueue().stream().map(e -> {
                    if (e instanceof GenericItemInfo) {
                        return ((GenericItemInfo) e).clone();
                    }
                    return e;
                }).forEach(clone::add);
                subInventory.process(clone);
            }, () -> inventorySet.getInsertQueue().add(insert));
        } else if (object instanceof Include) {
            ((Include) object).apply(this);
        } else if (object instanceof GenericItemInfo) {
            var item = (GenericItemInfo) object;
            item.setParent(this);
            if (item.getRequestedClone() != null) {
                var clone = item.getRequestedClone();
                if (clone.getCloneLink().equalsIgnoreCase("cosmetic")) {
                    if (clone.getCloneMethod().isOverride() || item.getItem() == null || item.getItem().getMaterial().equals(ItemFactory.getAir().getMaterial())) {
                        item.setItem(getLocalOptions().getCosmeticItem());
                    }
                } else {
                    GenericItemInfo originalItem;
                    if (clone.getCloneLink().equalsIgnoreCase("previous")) {
                        originalItem = lastItem;
                    } else {
                        originalItem = inventorySet.resolveItemLink(clone.getCloneLink()).orElse(null);
                    }
                    clone.cloneInto(originalItem, item);
                }
            }

            if (item.hasId()) {
                inventorySet.getIds().put(item.getId(), item);
            }

            if (item.isWritten()) {
                var newPosition = cursorPosition;
                if (item.getRequestedPosition() != null) {
                    newPosition = item.getRequestedPosition().calculateThatPosition(newPosition, getLocalOptions().getItemsOnRow(), getLocalOptions().getRows());
                }
                item.setPosition(newPosition);
                if (item.getRequestedPosition() != null) {
                    newPosition = item.getRequestedPosition().calculateNextPosition(newPosition, getLocalOptions().getItemsOnRow(), getLocalOptions().getRows());
                } else {
                    newPosition++;
                }
                cursorPosition = newPosition;
                // drop overridden items
                contents.stream().filter(genericItemInfo -> genericItemInfo.getPosition() == item.getPosition()).findFirst().ifPresent(genericItemInfo -> {
                    contents.remove(genericItemInfo);
                    if (genericItemInfo.hasId()) {
                        inventorySet.getIds().remove(genericItemInfo.getId());
                    }
                });
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
                    if (item.getRequestedTimes().getFlags().contains(TimesFlags.NO_ID)) {
                        clone.setId(null);
                    }
                    if (item.getRequestedTimes().getFlags().contains(TimesFlags.CANCEL_POSITIONING)) {
                        clone.setRequestedPosition(new Position());
                    }
                    process(clone);
                }
            }
        } else {
            throw new RuntimeException("Invalid object in queue!");
        }
    }

    @Override
    public void openInventory(Wrapper wrapper) {
        SimpleInventoriesCore.openInventory(wrapper.asOptional(PlayerWrapper.class).orElseThrow(), this);
    }

    // CURSOR MOVING
    public SubInventory carriageReturn() {
        this.cursorPosition -= (cursorPosition - localOptions.getItemsOnRow()) % localOptions.getItemsOnRow();
        return this;
    }

    public SubInventory lineBreak() {
        this.cursorPosition += (localOptions.getItemsOnRow() - (cursorPosition % localOptions.getItemsOnRow()));
        return this;
    }

    public SubInventory pageBreak() {
        this.cursorPosition += (localOptions.getItemsOnPage() - (cursorPosition % localOptions.getItemsOnPage()));
        return this;
    }

    public SubInventory skip(int skip) {
        this.cursorPosition += skip;
        return this;
    }

    @Deprecated
    public SubInventory absolute(int absolute) {
        this.cursorPosition = absolute;
        return this;
    }

    public SubInventory column(int column) {
        this.cursorPosition = (this.cursorPosition - (this.cursorPosition % localOptions.getItemsOnRow())) + column;
        return this;
    }

    public SubInventory column(Column column) {
        return column(column.convert(localOptions.getItemsOnRow()));
    }

    public SubInventory row(int row) {
        this.cursorPosition = this.cursorPosition - (this.cursorPosition % localOptions.getItemsOnPage()) + ((row - 1) * localOptions.getItemsOnRow())
                + (this.cursorPosition % localOptions.getItemsOnRow());
        return this;
    }

    public SubInventory dropContents() {
        contents.stream().filter(GenericItemInfo::hasId).forEach(genericItemInfo ->
                inventorySet.getIds().remove(genericItemInfo.getId())
        );
        contents.clear();
        forceReload();
        return this;
    }

    public SubInventory dropContentsOn(int position) {
        return dropContentsOn(List.of(position));
    }

    public SubInventory dropContentsOn(List<Integer> positions) {
        contents.removeIf(genericItemInfo -> {
            if (positions.contains(genericItemInfo.getPosition())) {
                if (genericItemInfo.hasId()) {
                    inventorySet.getIds().remove(genericItemInfo.getId());
                }
                return true;
            }
            return false;
        });
        return this;
    }

    public SubInventory dropContentsAfter(int position) {
        contents.removeIf(genericItemInfo -> {
            if (genericItemInfo.getPosition() >= position) {
                if (genericItemInfo.hasId()) {
                    inventorySet.getIds().remove(genericItemInfo.getId());
                }
                return true;
            }
            return false;
        });
        forceReload();
        return this;
    }

    public SubInventory dropContentsBefore(int position) {
        contents.removeIf(genericItemInfo -> {
            if (genericItemInfo.getPosition() < position) {
                if (genericItemInfo.hasId()) {
                    inventorySet.getIds().remove(genericItemInfo.getId());
                }
                return true;
            }
            return false;
        });
        forceReload();
        return this;
    }

    public SubInventory dropContentsBetween(int start, int end) {
        contents.removeIf(genericItemInfo -> {
            if (genericItemInfo.getPosition() >= start && genericItemInfo.getPosition() <= end) {
                if (genericItemInfo.hasId()) {
                    inventorySet.getIds().remove(genericItemInfo.getId());
                }
                return true;
            }
            return false;
        });
        forceReload();
        return this;
    }

    public SubInventory putIntoQueue(Queueable... items) {
        waitingQueue.addAll(Arrays.asList(items));
        return this;
    }

    @Override
    public Queue<Queueable> getQueue() {
        return waitingQueue;
    }

    public void forceReload() {
        SimpleInventoriesCore.getAllInventoryRenderersForSubInventory(this).forEach(InventoryRenderer::render);
    }
}
