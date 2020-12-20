package org.screamingsandals.simpleinventories.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CloneMethod {
    MISSING(false, false),
    OVERRIDE(true, false),
    INCREMENT_MISSING(false, true),
    INCREMENT_OVERRIDE(true, true);

    private final boolean override;
    private final boolean increment;
}
