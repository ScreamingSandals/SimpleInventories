package org.screamingsandals.simpleinventories.inventory;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

@Data
@AllArgsConstructor
public class Property implements Cloneable {
    @NotNull
    @Setter(onMethod_ = @Deprecated) // unsafe
    private InventorySet inventorySet;
    @Setter(onMethod_ = @Deprecated) // unsafe
    private String propertyName;
    @Setter(onMethod_ = @Deprecated) // unsafe
    private ConfigurationNode propertyData;

    public Property(@NotNull InventorySet inventorySet, String propertyName) {
        this(inventorySet, propertyName, BasicConfigurationNode.root());
    }

    public boolean hasName() {
        return propertyName != null;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Property clone() {
        return new Property(inventorySet, propertyName, propertyData.copy());
    }
}
