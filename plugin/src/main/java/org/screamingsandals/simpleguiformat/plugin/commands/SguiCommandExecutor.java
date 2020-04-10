package org.screamingsandals.simpleguiformat.plugin.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SguiCommandExecutor implements CommandExecutor, TabCompleter {
	
    private HashMap<String, BaseCommand> commands = new HashMap<>();
    
    {
    	registerCommand(new HelpCommand());
    	registerCommand(new OpenCommand());
    	registerCommand(new ReloadCommand());
    	registerCommand(new SendCommand());
    }
    
    private void registerCommand(BaseCommand command) {
    	commands.put(command.getName(), command);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completionList = new ArrayList<>();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                for (BaseCommand c : commands.values()) {
                    if (c.hasPermission(player)) {
                        completionList.add(c.getName());
                    }
                }
            } else if (args.length > 1) {
                ArrayList<String> arguments = new ArrayList<>(Arrays.asList(args));
                arguments.remove(0);
                BaseCommand bCommand = commands.get(args[0]);
                if (bCommand != null) {
                    if (bCommand.hasPermission(player)) {
                        bCommand.completeTab(completionList, sender, arguments);
                    }
                }
            }
        }
        List<String> finalCompletionList = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length - 1], completionList, finalCompletionList);
        return finalCompletionList;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            args = new String[]{"help"};
        }

        String command = args[0];
        ArrayList<String> arguments = new ArrayList<>(Arrays.asList(args));
        arguments.remove(0);

        BaseCommand bCommand = commands.get(command.toLowerCase());

        if (bCommand == null) {
            sender.sendMessage("§cUnknown command! Use §7/sgui help§c for help");
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {
            if (!bCommand.isConsoleCommand()) {
                sender.sendMessage("§cConsole can't use this command!");
                return true;
            }
        }

        if (!bCommand.hasPermission(sender)) {
            sender.sendMessage("§cYou don't have permissions to use this command!");
            return true;
        }

        boolean result = bCommand.execute(sender, arguments);

        if (!result) {
            sender.sendMessage("§cUnknown usage of command! Use §7/sgui help§c for more information!");
        }

        return true;
    }

}
