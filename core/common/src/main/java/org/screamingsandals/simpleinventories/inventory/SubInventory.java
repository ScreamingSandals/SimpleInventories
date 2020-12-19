package org.screamingsandals.simpleinventories.inventory;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
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

    @Setter(AccessLevel.PRIVATE)
    private Inventory format;

    @Setter(AccessLevel.PRIVATE)
    private int lastpos;
}
