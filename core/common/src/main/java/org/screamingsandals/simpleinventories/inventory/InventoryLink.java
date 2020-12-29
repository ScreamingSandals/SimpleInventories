package org.screamingsandals.simpleinventories.inventory;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryLink {
    @ToString.Exclude
    private InventorySet inventorySet;
    private String link;

    private SubInventory subInventory;

    @NotNull
    public static InventoryLink of(@NotNull InventorySet inventorySet, @NotNull String link) {
        var locate = new InventoryLink();
        locate.inventorySet = inventorySet;
        locate.link = link;
        return locate;
    }

    @NotNull
    public static InventoryLink of(@NotNull SubInventory subInventory) {
        var locate = new InventoryLink();
        locate.subInventory = subInventory;
        return locate;
    }

    @NotNull
    public static InventoryLink of(@NotNull InventorySet inventorySet) {
        return of(inventorySet.getMainSubInventory());
    }

    public Optional<SubInventory> resolve() {
        if (subInventory != null) {
            return Optional.of(subInventory);
        } else {
            return inventorySet.resolveCategoryLink(link);
        }
    }
}
