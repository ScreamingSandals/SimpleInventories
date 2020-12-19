package org.screamingsandals.simpleinventories.inventory;

import lombok.*;
import org.screamingsandals.simpleinventories.utils.MapReader;
import org.screamingsandals.simpleinventories.wrapper.PlayerWrapper;

import java.util.Map;

@Data
@AllArgsConstructor
public class Property {
    private final Inventory format;
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
}
