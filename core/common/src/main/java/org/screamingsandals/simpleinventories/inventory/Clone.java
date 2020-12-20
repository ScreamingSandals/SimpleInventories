package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import org.screamingsandals.simpleinventories.utils.CloneMethod;

@Data
public class Clone {
    private CloneMethod cloneMethod = CloneMethod.MISSING;
    private String cloneLink;
}
