package org.screamingsandals.simpleinventories.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kr.entree.spigradle.annotations.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.player.PlayerUtils;
import org.screamingsandals.simpleinventories.bukkit.SimpleInventoriesBukkit;
import org.screamingsandals.simpleinventories.inventory.InventorySet;
import org.screamingsandals.simpleinventories.render.InventoryRenderer;

@PluginMain
public class SimpleInventoriesPluginBukkit extends JavaPlugin {
	private static SimpleInventoriesPluginBukkit instance;
	private Map<String, InventorySet> inventories;
	
	public static SimpleInventoriesPluginBukkit getInstance() {
		return instance;
	}
	
	public static Set<String> getInventoryNames() {
		return instance.inventories.keySet();
	}
	
	public static InventorySet getInventory(String name) {
		return instance.inventories.get(name.toLowerCase());
	}
	
	@Override
	public void onEnable() {
		instance = this;

		SimpleInventoriesBukkit.init(this);

		this.inventories = new HashMap<>();

		load();

		// TODO: Commands

		Bukkit.getConsoleSender().sendMessage("§6=========§f=============  by ScreamingSandals <Misat11, Ceph>");
		Bukkit.getConsoleSender()
				.sendMessage("§6+ Simple §fInventories +  §6Version: " + getDescription().getVersion());
		Bukkit.getConsoleSender()
				.sendMessage("§6=========§f=============  " + (getDescription().getVersion().contains("SNAPSHOT") ? "§cSNAPSHOT VERSION" : "§aSTABLE VERSION"));
        
        this.inventories.values().forEach(inv -> {
        	try {
				inv.getMainSubInventory().process();
			} catch (Exception e) {
				getLogger().severe("Your configuration is bad!");
				e.printStackTrace();
			}
        });
	}
	
	@Override
	public void onDisable() {
		Bukkit.getOnlinePlayers().forEach(player ->
			SimpleInventoriesBukkit.getInventoryRenderer(PlayerUtils.wrapPlayer(player)).ifPresent(InventoryRenderer::close)
		);
		inventories.clear();
	}

	public void reload() {
		onDisable();

		this.inventories = new HashMap<>();

		load();
	}

	private void load() {
		// TODO: LOT OF SHITS HERE
	}
}
