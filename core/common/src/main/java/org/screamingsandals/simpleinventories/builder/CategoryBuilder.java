package org.screamingsandals.simpleinventories.builder;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.screamingsandals.simpleinventories.inventory.Inventory;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBuilder extends AbstractSubInventoryBuilder {
    protected SubInventory subInventory;

    @Override
    protected Inventory getFormat() {
        return subInventory.getFormat();
    }

    @Override
    protected void putObjectToQueue(@NonNull Object object) {
        subInventory.getWaitingQueue().add(object);
    }
}
