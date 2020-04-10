package org.screamingsandals.simpleguiformat.plugin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleguiformat.SimpleGuiFormat;
import org.screamingsandals.simpleguiformat.inventory.Options;

public class Inventory {
	private final Options options;
	private final File file;
	private final String section;
	private SimpleGuiFormat format;
	
	public Inventory(Options options, File file, String section) {
		this.options = options;
		this.file = file;
		this.section = section;
	}
	
	public void load() throws Exception {
		if (format == null) {
			format = new SimpleGuiFormat(options);
			format.load(file, section);
			format.generateData();
		}
	}
	
	public void openForPlayer(Player player) {
		format.openForPlayer(player);
	}
	
	public void destroy() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (format.getCurrentGuiHolder(player) != null) {
				player.closeInventory();
			}
		});
	}
}
