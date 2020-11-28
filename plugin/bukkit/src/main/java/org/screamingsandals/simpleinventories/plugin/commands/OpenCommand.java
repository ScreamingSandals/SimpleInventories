package org.screamingsandals.simpleinventories.plugin.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.plugin.Inventory;
import org.screamingsandals.simpleinventories.plugin.SimpleInventoriesPlugin;

public class OpenCommand extends BaseCommand {

	protected OpenCommand() {
		super("open", Arrays.asList(USE_PERMISSION, ADMIN_PERMISSION), false);
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			Inventory inv = SimpleInventoriesPlugin.getInventory(args.get(0));
			if (inv == null) {
				sender.sendMessage("§cInventory " + args.get(0) + " doesn't exist!");
				return true;
			}
			inv.openForPlayer((Player) sender);
			return true;
		}
		return false;
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			completion.addAll(SimpleInventoriesPlugin.getInventoryNames());
		}
	}

}
