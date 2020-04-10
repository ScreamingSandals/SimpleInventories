package org.screamingsandals.simpleguiformat.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.screamingsandals.simpleguiformat.plugin.SimpleGuiPlugin;

import java.util.List;

public class ReloadCommand extends BaseCommand {

    public ReloadCommand() {
        super("reload", ADMIN_PERMISSION, true);
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        Bukkit.getServer().getPluginManager().disablePlugin(SimpleGuiPlugin.getInstance());
        Bukkit.getServer().getPluginManager().enablePlugin(SimpleGuiPlugin.getInstance());
        sender.sendMessage("Plugin reloaded!");
        return true;
    }

    @Override
    public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
        // Nothing to add.
    }

}
