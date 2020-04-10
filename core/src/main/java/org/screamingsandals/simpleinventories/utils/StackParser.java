package org.screamingsandals.simpleinventories.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

public class StackParser {
	
	public static final List<String> STRING_BEGIN_END = Arrays.asList("\"", "'");
	public static final List<String> ESCAPE_SYMBOLS = Arrays.asList("\\");
	public static final List<String> ARGUMENT_SEPARATORS = Arrays.asList(";");
	
	/**
	 * This method will check the format of the stacks and than it'll parse the stacks.
	 * 
	 * @param stacks The list of objects that should be parsed to stacks
	 * @return list of ItemStacks
	 */
	public static List<ItemStack> parseAll(Collection<Object> stacks) {
		List<ItemStack> newStacks = new ArrayList<>();
		stacks.forEach(stack -> {
			newStacks.add(parse(stack));
		});
		return newStacks;
	}
	
	/**
	 * This method will check the format of the stack and than it'll parse the stack.
	 * 
	 * @param stack The object that should be parsed to stack
	 * @return Parsed ItemStack
	 */
	public static ItemStack parse(Object stack) {
		if (stack instanceof ItemStack) {
			return (ItemStack) stack;
		} else if (stack instanceof Map) {
			return parseLongStack((Map<String, Object>) stack);
		} else {
			return parseShortStack(stack.toString());
		}
	}

	
	/**
	 * This method will check the format of the stack and than it'll parse the stack or return null.
	 * 
	 * @param stack The object that should be parsed to stack
	 * @return Parsed ItemStack or null on fail
	 */
	public static ItemStack parseNullable(Object stack) {
		if (stack instanceof ItemStack) {
			return (ItemStack) stack;
		} else if (stack instanceof Map) {
			return parseLongStack((Map<String, Object>) stack);
		} else if (stack instanceof String) {
			return parseShortStack((String) stack);
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack parseShortStack(String shortStack) {
		shortStack = shortStack.trim();
		if (shortStack.startsWith("(cast to ItemStack)")) {
			shortStack = shortStack.substring(19).trim();
		}
		
		char[] characters = shortStack.toCharArray();
		List<String> arguments = new ArrayList<String>();
		int lastIndexOfEscape = -2;
		boolean workOnString = false;
		String workOnStringWith = "";
		String build = "";
		for (int i = 0; i < characters.length; i++) {
			char character = characters[i];
			String c = String.valueOf(character);
			if (ESCAPE_SYMBOLS.contains(c) && lastIndexOfEscape != (i - 1)) {
				lastIndexOfEscape = i;
			} else if (workOnString) {
				if (lastIndexOfEscape != (i - 1) && c.equals(workOnStringWith)) {
					workOnString = false;
					arguments.add(build.trim());
					build = "";
				} else {
					build += c;
				}
			} else if (STRING_BEGIN_END.contains(c) && lastIndexOfEscape != (i - 1)) {
				workOnString = true;
				workOnStringWith = c;
			} else if (ARGUMENT_SEPARATORS.contains(c) && lastIndexOfEscape != (i - 1)) {
				arguments.add(build);
				build = "";
			} else {
				build += c;
			}
		}
		if (!build.equals("")) {
			arguments.add(build);
		}

		Material mat = Material.AIR;
		short damage = 0;
		int amount = 1;
		String displayName = null;
		List<String> lore = new ArrayList<String>();

		for (int i = 0; i < arguments.size(); i++) {
			String argument = arguments.get(i);
			if (i == 0) {
				MaterialSearchEngine.Result res = MaterialSearchEngine.find(argument);
				mat = res.getMaterial();
				damage = res.getDamage();
			} else if (i == 1) {
				if (!argument.equals("")) {
					amount = Integer.parseInt(argument);
				}
			} else if (i == 2) {
				if (!argument.equals("")) {
					displayName = argument;
				}
			} else {
				lore.add(argument);
			}
		}

		ItemStack stack = new ItemStack(mat, amount, damage);
		if (mat != Material.AIR) {
			ItemMeta meta = stack.getItemMeta();
			if (displayName != null && !displayName.equals("")) {
				meta.setDisplayName(displayName);
			}
			if (!lore.isEmpty()) {
				meta.setLore(lore);
			}
			stack.setItemMeta(meta);

		}
		return stack;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static ItemStack parseLongStack(Map<String, Object> obj) {
		ItemStack stack = parseShortStack((String) obj.get("type"));
		
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
