package org.screamingsandals.simpleinventories.material;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Platform {
    JAVA_FLATTENING(false),
    JAVA_LEGACY(true),
    BEDROCK(true);

    private final boolean usingLegacyNames;
}
