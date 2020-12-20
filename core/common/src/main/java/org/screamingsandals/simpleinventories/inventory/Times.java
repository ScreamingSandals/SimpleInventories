package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import org.screamingsandals.simpleinventories.utils.TimesFlags;

import java.util.ArrayList;
import java.util.List;

@Data
public class Times {
    private int repeat = 1;
    private final List<TimesFlags> flags = new ArrayList<>();
}
