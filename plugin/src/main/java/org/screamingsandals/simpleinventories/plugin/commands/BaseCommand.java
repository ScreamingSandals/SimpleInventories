package org.screamingsandals.simpleinventories.plugin.commands;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public abstract class BaseCommand {

    public static final String ADMIN_PERMISSION = "simpleguiformat.admin";
    public static final String USE_PERMISSION = "simpleguiformat.use";

    private String name;
    private List<String> permission;
    private boolean allowConsole;

    protected BaseCommand(String name, String permission, boolean allowConsole) {
    	this(name, Arrays.asList(permission), allowConsole);
    }

    protected BaseCommand(String name, List<String> permission, boolean allowConsole) {
        this.name = name.toLowerCase();
        this.permission = permission;
        this.allowConsole = allowConsole;
    }

    public String getName() {
        return this.name;
    }

    public boolean isConsoleCommand() {
        return this.allowConsole;
    }

    public List<String> getPermissions() {
        return this.permission;
    }

    public abstract boolean execute(CommandSender sender, List<String> args);

    public abstract void completeTab(List<String> completion, CommandSender sender, List<String> args);

    public boolean hasPermission(CommandSender sender) {
        if (permission == null || permission.isEmpty()) {
            return true; // There's no permissions required
        }

        for (String perm : permission) {
        	if (sender.hasPermission(perm)) {
        		return true;
        	}
        }
        
        return false;
    }

}
