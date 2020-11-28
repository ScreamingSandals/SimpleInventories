package org.screamingsandals.simpleinventories.material;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class MaterialHolder {
    private final String platformName;
    private final short durability;

    public abstract MaterialHolder newDurability(short durability);

    public abstract <T> T as(Class<T> theClass);
}
