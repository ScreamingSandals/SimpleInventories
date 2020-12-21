package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import org.screamingsandals.simpleinventories.utils.CloneMethod;

@Data
public class Clone implements Cloneable {
    private CloneMethod cloneMethod = CloneMethod.MISSING;
    private String cloneLink;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Clone clone() {
        var clone = new Clone();
        clone.cloneMethod = cloneMethod;
        clone.cloneLink = cloneLink;
        return clone;
    }
}
