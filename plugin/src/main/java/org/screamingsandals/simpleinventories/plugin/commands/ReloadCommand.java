package org.screamingsandals.simpleinventories.plugin.commands;

import org.bukkit.command.CommandSender;
import org.screamingsandals.simpleinventories.plugin.SimpleInventoriesPlugin;

import java.util.List;

public class ReloadCommand extends BaseCommand {

    public ReloadCommand() {
        super("reload", ADMIN_PERMISSION, true);
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        SimpleInventoriesPlugin.getInstance().reload();
        sender.sendMessage("Plugin reloaded!");
        return true;
    }

    @Override
    public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
        // Nothing to add.
    }

}
