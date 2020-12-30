package org.screamingsandals.simpleinventories.inventory;

import lombok.*;
import org.screamingsandals.lib.utils.MapReader;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.Map;

@Data
@AllArgsConstructor
public class Property implements Cloneable {
    private final InventorySet format;
    @Setter(onMethod_ = @Deprecated)
    private String propertyName;
    @Getter(onMethod_ = @Deprecated)
    @Setter(onMethod_ = @Deprecated)
    private Map<String, Object> propertyData;

    public boolean hasName() {
        return propertyName != null;
    }

    public boolean hasData() {
        return propertyData != null && !propertyData.isEmpty();
    }

    public MapReader getReader(PlayerWrapper player, PlayerItemInfo info) {
        return new MapReader(format, propertyData != null ? propertyData : Map.of(), player, info);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Property clone() {
        return new Property(format, propertyName, Map.copyOf(propertyData));
    }
}
