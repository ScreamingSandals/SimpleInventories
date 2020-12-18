package org.screamingsandals.simpleinventories.wrapper;

import lombok.Data;
import org.screamingsandals.simpleinventories.SimpleInventoriesCore;

import java.util.UUID;

@Data
public class PlayerWrapper {
    private String name;
    private UUID uuid;

    public <T> T as(Class<T> type) {
        return SimpleInventoriesCore.convertPlayerWrapper(this, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerWrapper)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return ((PlayerWrapper) obj).uuid.equals(this.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
