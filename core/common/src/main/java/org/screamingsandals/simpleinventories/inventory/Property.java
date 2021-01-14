package org.screamingsandals.simpleinventories.inventory;

import lombok.*;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

@Data
@AllArgsConstructor
public class Property implements Cloneable {
    private final InventorySet inventorySet;
    @Setter(onMethod_ = @Deprecated)
    private String propertyName;
    @Setter(onMethod_ = @Deprecated)
    private ConfigurationNode propertyData;

    public Property(InventorySet inventorySet, String propertyName) {
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
