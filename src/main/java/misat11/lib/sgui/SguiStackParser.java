package misat11.lib.sgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

public class SguiStackParser {
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static ItemStack parseSguiStack(Map<String, Object> obj) {
		ItemStack stack = ShortStackParser.parseShortStack((String) obj.get("type"));
		
		if (stack.getType() == Material.AIR) {
			return stack;
		}
		
		if (obj.containsKey("amount")) {
			if (obj.get("amount") instanceof Number) {
				stack.setAmount(((Number) obj.get("amount")).intValue());
			} else {
				
			}
		}
		
		if (obj.containsKey("damage")) {
			if (obj.get("damage") instanceof Number) {
				stack.setDurability(((Number) obj.get("damage")).shortValue());
			}
		}
		
		if (obj.containsKey("durability")) {
			if (obj.get("durability") instanceof Number) {
				stack.setDurability(((Number) obj.get("durability")).shortValue());
			}
		}
		
		if (obj.containsKey("meta")) {
			if (obj.get("meta") instanceof ItemMeta) {
				try {
					stack.setItemMeta((ItemMeta) obj.get("meta"));
				} catch (Throwable t) {
				}
			}
		}
		
		ItemMeta meta = stack.getItemMeta();
		
		if (obj.containsKey("display-name")) {
			String str = obj.get("display-name").toString();
			
			meta.setDisplayName(str);
		}
		
		if (obj.containsKey("loc-name")) {
			String str = obj.get("loc-name").toString();
			try {
				meta.setLocalizedName(str);
			} catch (Throwable t) {
			}
		}
		
		if (obj.containsKey("custom-model-data") && obj.get("custom-model-data") instanceof Number) {
			try {
				meta.setCustomModelData(((Number) obj.get("custom-model-data")).intValue());
			} catch (Throwable t) {
			}
		}
		
		if (obj.containsKey("repair-cost") && meta instanceof Repairable) {
			String str = obj.get("repair-cost").toString();
			try {
				((Repairable) meta).setRepairCost(Integer.parseInt(str));
			} catch (Throwable t) {
			}
		}

		if (obj.containsKey("ItemFlags") && obj.get("ItemFlags") instanceof List) {
			for (String str : (List<String>) obj.get("ItemFlags")) {
				try {
					meta.addItemFlags(ItemFlag.valueOf(str));
				} catch (Throwable t) {
				}
			}
		}
		
		if (obj.containsKey("Unbreakable")) {
			try {
				meta.setUnbreakable(Boolean.parseBoolean(obj.get("Unbreakable").toString()));
			} catch (Throwable t) {
			}
		}
		
		if (obj.containsKey("lore")) {
			List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
			
			if (obj.get("lore") instanceof List) {
				for (String str : (List<String>) obj.get("lore")) {
					lore.add(str);
				}
			} else {
				lore.add(obj.get("lore").toString());
			}
			
			meta.setLore(lore);
		}
		
		if (obj.containsKey("enchants")) {
			if (obj.get("enchants") instanceof List) {
				for (String ob : (List<String>) obj.get("enchants")) {
					if (ob.equals("SWEEPING")) {
						ob = "SWEEPING_EDGE";
					}
					Enchantment ench = Enchantment.getByName(ob);
					if (ench != null && meta.hasEnchant(ench)) {
						meta.addEnchant(ench, 1, true);
					}
				}
			} else if (obj.get("enchants") instanceof Map) {
				for (Map.Entry<String, Object> entry : ((Map<String, Object>) obj.get("enchants")).entrySet()) {
					String ob = entry.getKey();
					if (ob.equals("SWEEPING")) {
						ob = "SWEEPING_EDGE";
					}
					Enchantment ench = Enchantment.getByName(ob);
					int i = 1;
					try {
						i = ((Number) entry.getValue()).intValue();
					} catch (Throwable t) {
					}
					if (ench != null && !meta.hasEnchant(ench)) {
						meta.addEnchant(ench, i, true);
					}
				}
			} else {
				String ob =  obj.get("enchants").toString();
				if (ob.equals("SWEEPING")) {
					ob = "SWEEPING_EDGE";
				}
				Enchantment ench = Enchantment.getByName(ob);
				if (ench != null && meta.hasEnchant(ench)) {
					meta.addEnchant(ench, 1, true);
				}
			}
		}
		
		stack.setItemMeta(meta);
		
		return stack;
	}

}
