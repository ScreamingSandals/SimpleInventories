package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import org.screamingsandals.lib.utils.TimesFlags;

import java.util.ArrayList;
import java.util.List;

@Data
public class Times implements Cloneable {
    private int repeat = 1;
    private final List<TimesFlags> flags = new ArrayList<>();

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Times clone() {
        var times = new Times();
        times.repeat = repeat;
        times.flags.addAll(flags);
        return times;
    }
}
