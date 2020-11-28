package org.screamingsandals.simpleinventories.plugin.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.simpleinventories.plugin.Inventory;
import org.screamingsandals.simpleinventories.plugin.SimpleInventoriesPlugin;

public class SendCommand extends BaseCommand {

	protected SendCommand() {
		super("send", ADMIN_PERMISSION, true);
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) {
		if (args.size() == 2) {
			Player player = Bukkit.getPlayer(args.get(1));
			if (player == null) {
				sender.sendMessage("§cPlayer " + args.get(0) + " doesn't exist! or is offline");
				return true;
			}
			
			Inventory inv = SimpleInventoriesPlugin.getInventory(args.get(1));
			if (inv == null) {
				sender.sendMessage("§cInventory " + args.get(0) + " doesn't exist!");
				return true;
			}
			inv.openForPlayer(player);
			return true;
		}
		return false;
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			Bukkit.getOnlinePlayers().forEach(player -> completion.add(player.getName()));
		} else if (args.size() == 2) {
			completion.addAll(SimpleInventoriesPlugin.getInventoryNames());
		}
	}

}
