package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Insert {
    private final String link;
    private final SubInventory subInventory;
}
