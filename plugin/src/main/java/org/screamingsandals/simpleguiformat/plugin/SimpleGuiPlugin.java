package org.screamingsandals.simpleguiformat.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.simpleguiformat.listeners.InventoryListener;

public class SimpleGuiPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		InventoryListener.init(this);
		
		// TODO
		
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
	}
	
	@Override
	public void onDisable() {
		// TODO
	}
}
