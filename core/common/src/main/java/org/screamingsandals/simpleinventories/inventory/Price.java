package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;

@Data
public class Price implements Cloneable {
    private int amount;
    private String currency;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Price clone() {
        var price = new Price();
        price.amount = amount;
        price.currency = currency;
        return price;
    }
}
