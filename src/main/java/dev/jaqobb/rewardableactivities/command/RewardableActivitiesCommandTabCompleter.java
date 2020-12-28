package dev.jaqobb.rewardableactivities.command;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public final class RewardableActivitiesCommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(
        final CommandSender sender,
        final Command command,
        final String label,
        final String[] arguments
    ) {
        if (!sender.hasPermission("rewardableactivities.command.rewardableactivities")) {
            return null;
        }
        List<String> completions = new LinkedList<>();
        if (arguments.length == 1) {
            String argument = arguments[0].toLowerCase();
            if ("reload".startsWith(argument)) {
                completions.add("reload");
            }
        }
        return completions;
    }
}
