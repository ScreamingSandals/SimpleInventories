package org.screamingsandals.simpleinventories.groovy.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.screamingsandals.simpleinventories.utils.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class GroovyBukkitStackBuilder implements IGroovyStackBuilder {
    private final ItemStack stack;

    @Override
    public void amount(int amount) {
        stack.setAmount(amount);
    }

    @Override
    public void lore(List<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    @Override
    public void name(String name) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
    }

    @Override
    public void type(String type) {
        ItemStack shortStack = StackParser.parseShortStack(type);
        type(shortStack.getType());
        durability(shortStack.getDurability());
        amount(shortStack.getAmount());
        ItemMeta shortMeta = shortStack.getItemMeta();
        if (shortMeta.hasDisplayName()) {
            name(shortMeta.getDisplayName());
        }
        if (shortMeta.hasLore()) {
            lore(shortMeta.getLore());
        }
    }

    @Override
    public void type(Material type) {
        stack.setType(type);
    }

    @Override
    public void durability(short durability) {
        stack.setDurability(durability);
    }

    @Override
    public void customModelData(int data) {
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(data);
        stack.setItemMeta(meta);
    }

    @Override
    public void repair(int repair) {
        ItemMeta meta = stack.getItemMeta();
        if (meta instanceof Repairable) {
            ((Repairable) meta).setRepairCost(repair);
            stack.setItemMeta(meta);
        }
    }

    @Override
    public void flags(List<Object> flags) {
        ItemMeta meta = stack.getItemMeta();
        for (Object str : flags) {
            try {
                meta.addItemFlags(ItemFlag.valueOf(str.toString()));
            } catch (Throwable t) {
            }
        }
        stack.setItemMeta(meta);
    }

    @Override
    public void unbreakable(boolean unbreakable) {
        ItemMeta meta = stack.getItemMeta();
        meta.setUnbreakable(unbreakable);
        stack.setItemMeta(meta);
    }

    @Override
    public void enchant(String enchant, int level) {
        enchant(EnchantmentSearchEngine.searchEnchantment(enchant), level);
    }

    @Override
    public void enchant(Enchantment enchant, int level) {
        if (enchant != null) {
            ItemMeta meta = stack.getItemMeta();
            meta.addEnchant(enchant, level, true);
            stack.setItemMeta(meta);
        }
    }

    @Override
    public void potion(String potion) {
        if (MaterialSearchEngine.getVersionNumber() == 108) {
            stack.setDurability(PotionTypeSearchEngine1_8_8.find(potion).toDamageValue());
        } else {
            ItemMeta meta = stack.getItemMeta();
            if (meta instanceof PotionMeta) {
                ((PotionMeta) meta).setBasePotionData(PotionTypeSearchEngine.find(potion));
                stack.setItemMeta(meta);
            }
        }
    }

    @Override
    public void potion(PotionType potion) {
        ItemMeta meta = stack.getItemMeta();
        if (meta instanceof PotionMeta) {
            ((PotionMeta) meta).setBasePotionData(new PotionData(potion));
            stack.setItemMeta(meta);
        }
    }
}
