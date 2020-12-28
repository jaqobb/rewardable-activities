package dev.jaqobb.rewardableactivities.command;

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
        return null;
    }
}
