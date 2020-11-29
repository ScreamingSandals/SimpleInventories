package org.screamingsandals.simpleinventories.material;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public final class MaterialHolder {
    private final String platformName;
    private final short durability;

    public MaterialHolder(String platformName) {
        this(platformName, (short) 0);
    }

    public MaterialHolder newDurability(short durability) {
        return new MaterialHolder(platformName, durability);
    }

    public <T> T as(Class<T> theClass) {
        return MaterialMapping.convertMaterialHolder(this, theClass);
    }
}
