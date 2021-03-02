package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Insert implements Queueable {
    private final String link;
    private final SubInventoryLike<?> temporaryInventory;
}
