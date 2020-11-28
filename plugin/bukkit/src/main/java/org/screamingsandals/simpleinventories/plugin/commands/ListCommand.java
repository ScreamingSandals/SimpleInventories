package org.screamingsandals.simpleinventories.plugin.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.plugin.Inventory;
import org.screamingsandals.simpleinventories.plugin.SimpleInventoriesPlugin;

import java.util.Arrays;
import java.util.List;

public class ListCommand extends BaseCommand {

	protected ListCommand() {
		super("list", Arrays.asList(USE_PERMISSION, ADMIN_PERMISSION), true);
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) {
		sender.sendMessage("§f[SI] §aAvailable inventories:");
		SimpleInventoriesPlugin.getInventoryNames().forEach(inventory -> {
			sender.sendMessage("§7" + inventory);
		});
		return true;
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
	}

}
