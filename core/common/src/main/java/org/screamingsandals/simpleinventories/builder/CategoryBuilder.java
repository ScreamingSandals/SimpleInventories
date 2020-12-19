package org.screamingsandals.simpleinventories.builder;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.screamingsandals.simpleinventories.inventory.SubInventory;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBuilder extends AbstractSubInventoryBuilder {
    private SubInventory subInventory;

}
