package org.screamingsandals.simpleinventories.material;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.simpleinventories.material.builder.ItemFactory;
import org.screamingsandals.simpleinventories.material.meta.EnchantmentHolder;
import org.screamingsandals.simpleinventories.material.meta.PotionHolder;

import java.util.ArrayList;
import java.util.List;

@Data
public class Item {
    //@Nullable // in initial state it's null
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
    private final List<EnchantmentHolder> enchantments = new ArrayList<>();
    @Nullable
    private List<String> itemFlags;
    @Nullable
    private PotionHolder potion;

    @Deprecated
    @Nullable
    private Object platformMeta;

    public <R> R as(Class<R> type) {
        return ItemFactory.convertItem(this, type);
    }
}
