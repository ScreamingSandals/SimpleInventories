package org.screamingsandals.simpleinventories.material;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.simpleinventories.material.meta.EnchantmentHolder;
import org.screamingsandals.simpleinventories.material.meta.PotionHolder;

import java.util.List;

@Data
public class Item {
    @NotNull
    private MaterialHolder material;
    @Nullable
    private String displayName;
    @Nullable
    private String localizedName;
    private int amount = 1;
    private int customModelData;
    private int repair;
    private boolean unbreakable;
    @Nullable
    private List<String> lore;
    @Nullable
    private List<EnchantmentHolder> enchantments;
    @Nullable
    private List<String> itemFlags;
    @Nullable
    private PotionHolder potion;

    @Deprecated
    @Nullable
    private Object platformMeta;
}
