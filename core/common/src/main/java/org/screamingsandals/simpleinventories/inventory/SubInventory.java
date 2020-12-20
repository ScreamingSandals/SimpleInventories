package org.screamingsandals.simpleinventories.inventory;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubInventory extends AbstractInventory {
    private final List<GenericItemInfo> items = new ArrayList<>();
    private final boolean main;
    @Nullable
    private final GenericItemInfo itemOwner;
    @ToString.Exclude
    private final Inventory format;

    @Setter(AccessLevel.PRIVATE)
    private int lastpos;

    private final List<GenericItemInfo> contents = new ArrayList<>();

    /**
     * Used for adding operations and items for future process.
     */
    @ToString.Exclude
    private final List<Object> waitingQueue = new ArrayList<>();

    public void process() {

    }
}
