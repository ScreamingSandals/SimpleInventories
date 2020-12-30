package org.screamingsandals.simpleinventories.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BreakType {
    BEFORE(true, false),
    AFTER(false, true),
    BOTH(true, true);

    private final boolean breakingBefore;
    private final boolean breakingAfter;
}
