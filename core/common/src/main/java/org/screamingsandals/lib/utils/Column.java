package org.screamingsandals.lib.utils;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum Column {
    LEFT(row_size -> 0),
    MIDDLE(row_size -> row_size / 2),
    RIGHT(row_size -> row_size - 1),
    FIRST(row_size -> 0),
    CENTER(row_size -> row_size / 2),
    LAST(row_size -> row_size - 1);

    private final Function<Integer, Integer> convert;

    public int convert(int row_size) {
        return convert.apply(row_size);
    }
}
