package org.screamingsandals.simpleinventories.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.simpleinventories.inventory.Options;
import org.screamingsandals.simpleinventories.listeners.InventoryListener;
import org.screamingsandals.simpleinventories.plugin.commands.SguiCommandExecutor;
import org.screamingsandals.simpleinventories.plugin.config.Configurator;

public class SimpleInventoriesPlugin extends JavaPlugin {
	private static SimpleInventoriesPlugin instance;
	private Map<String, Inventory> inventories;
	
	public static SimpleInventoriesPlugin getInstance() {
		return instance;
	}
	
	public static Set<String> getInventoryNames() {
		return instance.inventories.keySet();
	}
	
	public static Inventory getInventory(String name) {
		return instance.inventories.get(name.toLowerCase());
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		InventoryListener.init(this);
		
		SguiCommandExecutor executor = new SguiCommandExecutor();
		PluginCommand command = getCommand("simpleinventories");
		command.setExecutor(executor);
		command.setTabCompleter(executor);
		
		this.inventories = new HashMap<>();
		
		Configurator config = new Configurator(this);
		config.createFiles();
		
		ConfigurationSection inventories = config.config.getConfigurationSection("inventories");
		
		for (String inventoryN : inventories.getKeys(false)) {
			ConfigurationSection inventory = inventories.getConfigurationSection(inventoryN);
			
			String file = inventory.getString("file");
			String section = inventory.getString("section");
			Options options = Options.deserialize(inventory.getConfigurationSection("options"), this);
			
			Inventory inv = new Inventory(options, new File(getDataFolder(), file), section);
			
			this.inventories.put(inventoryN.toLowerCase(), inv);
        }
		
        try {
        	float javaVer = Float.parseFloat(System.getProperty("java.class.version"));
        	if (javaVer < 55) {
        		getLogger().warning("Future versions of plugins from ScreamingSandals will require at least Java 11. "
        			+ "Your server is not prepared for it. Update your Java or contact your hosting."
        			+ "Java 8 for commercial usage is already out of casual support, for personal usage it's supported until December 2020!"
        			+ "Java 9 and Java 10 were short-term support versions, these versions are already not supported.");
        		getLogger().warning("Future versions of Java will require Minecraft version at least 1.12");
        		
        	}
        } catch (Throwable t) { // What if it fails? Why it should fail I don't know :D
        }
        
        this.inventories.values().forEach(inv -> {
        	try {
				inv.load();
			} catch (Exception e) {
				getLogger().severe("Your configuration is bad!");
				e.printStackTrace();
			}
        });
	}
	
	@Override
	public void onDisable() {
		PluginCommand command = getCommand("sgui");
		command.setExecutor(null);
		command.setTabCompleter(null);
		
		inventories.values().forEach(Inventory::destroy);
		inventories.clear();
	}
}
