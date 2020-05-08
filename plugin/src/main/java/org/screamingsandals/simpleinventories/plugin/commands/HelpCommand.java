package org.screamingsandals.simpleinventories.plugin.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class HelpCommand extends BaseCommand {

    public HelpCommand() {
        super("help", Arrays.asList(USE_PERMISSION, ADMIN_PERMISSION), true);
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (sender instanceof Player) {
            sendHelp((Player) sender);
        } else if (sender instanceof ConsoleCommandSender) {
            sendConsoleHelp((ConsoleCommandSender) sender);
        }
        return true;
    }

    @Override
    public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
        // Nothing to add.
    }

    public void sendConsoleHelp(ConsoleCommandSender console) {
    	console.sendMessage("SimpleInventories - Help for console");
        console.sendMessage("/si list - §cShows you all available inventories");
    	console.sendMessage("/si send <player> <inventory> - §7Opens inventory for player");
    	console.sendMessage("/si reload - §7Reloads plugin");
    }

    public void sendHelp(Player player) {
    	player.sendMessage("SimpleGuiFormat - Help");
        player.sendMessage("/si list - §cShows you all available inventories");
    	player.sendMessage("/si open <inventory> - ů7Opens you an inventory");
    	
    	if (player.hasPermission(ADMIN_PERMISSION)) {
	    	player.sendMessage("/si send <player> <inventory> - §7Opens inventory for player");
	    	player.sendMessage("/si reload - §7Reloads plugin");
    	}
    }

}
