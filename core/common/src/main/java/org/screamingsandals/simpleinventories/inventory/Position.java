package org.screamingsandals.simpleinventories.inventory;

import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.simpleinventories.utils.BreakType;

import java.util.function.Function;
import java.util.function.Supplier;

@Data
public class Position {
    @Setter(onMethod_ = @Deprecated)
    @Nullable
    private Supplier<Integer> absolute;
    @Nullable
    private Supplier<Integer> row;
    @Nullable
    private Function<Integer, Integer> column;
    @Nullable
    private Supplier<Integer> skip;
    @Nullable
    private Supplier<BreakType> linebreak;
    @Nullable
    private Supplier<BreakType> pagebreak;
}
