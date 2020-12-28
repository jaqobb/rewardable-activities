package dev.jaqobb.rewardableactivities.command;

import dev.jaqobb.rewardableactivities.RewardableActivitiesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class RewardableActivitiesCommand implements CommandExecutor {

    private final RewardableActivitiesPlugin plugin;

    public RewardableActivitiesCommand(final RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
        final CommandSender sender,
        final Command command,
        final String label,
        final String[] arguments
    ) {
        return true;
    }
}
