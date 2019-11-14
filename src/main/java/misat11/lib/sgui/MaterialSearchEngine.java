package misat11.lib.sgui;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class MaterialSearchEngine {
	
	public static class Result {
		private Material material;
		private short damage;
		
		public Result(Material material, short damage) {
			this.material = material;
			this.damage = damage;
		}
		
		public Material getMaterial() {
			return material;
		}
		
		public short getDamage() {
			return damage;
		}
		
		public boolean hasDamage() {
			return damage > 0;
		}
	}
	
	/**
	 * From Flattening to Legacy Material name translator
	 */
	public static enum FFTLTranslator {
		// TODO add translates
		// TODO add meta translator for 1.12 colored items such as bed
		RED_BED("BED") // Example translate
		;
		
		private String translate;
		private short damage;
		
		private FFTLTranslator(String translate) {
			this(translate, (short) 0);
		}
		
		private FFTLTranslator(String translate, short damage) {
			this.translate = translate;
			this.damage = damage;
		}
		
	}
	
	private static int versionNumber;
	private static boolean isLegacy;
	
	static {
        String[] bukkitVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        versionNumber = 0;
        for (int i = 0; i < 2; i++) {
            versionNumber += Integer.parseInt(bukkitVersion[i]) * (i == 0 ? 100 : 1);
        }

        isLegacy = versionNumber < 113;
	}
	
	public static Result find(String materialArgument) {
		String[] splitByColon = materialArgument.split(":");
		String material = splitByColon[0];
		short damage = 0;
		Material mat = Material.matchMaterial(material);
		if (mat == null) {
			if (isLegacy) {
				// try translate it from flattening to legacy
				try {
					FFTLTranslator translate = FFTLTranslator.valueOf(material.toUpperCase());
					mat = Material.matchMaterial(translate.translate);
					damage = translate.damage;
				} catch (Throwable t) {
				}
			} else {
				// try legacy
				try {
					mat = Material.matchMaterial(material, true);
				} catch (Throwable t) {
				}
			}
		}
		if (mat == null) {
			// maybe it's id?
			try {
				int legacyId = Integer.parseInt(material);
				
				for (Material mater : Material.values()) {
					try {
						if (mater.getId() == legacyId) {
							mat = mater;
						}
					} catch (Throwable t) {
						// This material is not legacy
					}
				}
			} catch (Throwable t) {
				// this is not id
			}
		}
		if (mat == null) {
			// this is nothing, let it be air
			mat = Material.AIR;
		}
		if (splitByColon.length > 1) {
			String damageString = splitByColon[1];
			try {
				damage = (short) Integer.parseInt(damageString);
			} catch (Throwable t) {
				// Invalid damage
			}
		}
		return new Result(mat, damage);
	}
}
