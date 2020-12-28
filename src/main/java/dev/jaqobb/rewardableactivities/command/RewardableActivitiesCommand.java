package dev.jaqobb.rewardableactivities.command;

import dev.jaqobb.rewardableactivities.RewardableActivitiesConstants;
import dev.jaqobb.rewardableactivities.RewardableActivitiesPlugin;
import net.md_5.bungee.api.ChatColor;
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
        if (!sender.hasPermission("rewardableactivities.command.rewardableactivities")) {
            sender.sendMessage(RewardableActivitiesConstants.PREFIX + ChatColor.RED + "You do not have the required permissions to do that.");
            return true;
        }
        if (arguments.length == 0) {
            sender.sendMessage(RewardableActivitiesConstants.PREFIX + ChatColor.WHITE + "Available commands:");
            sender.sendMessage(RewardableActivitiesConstants.PREFIX + ChatColor.GRAY + "/rewardable-activities reload" + ChatColor.WHITE + " - Reloads plugin.");
            return true;
        }
        if (arguments[0].equalsIgnoreCase("reload")) {
            if (arguments.length != 1) {
                sender.sendMessage(RewardableActivitiesConstants.PREFIX + ChatColor.WHITE + "Correct usage: " + ChatColor.GRAY + "/" + label + " reload" + ChatColor.WHITE + ".");
                return true;
            }
            this.plugin.reloadConfig();
            this.plugin.loadConfig(true);
            sender.sendMessage(RewardableActivitiesConstants.PREFIX + ChatColor.WHITE + "Plugin has been reloaded.");
            return true;
        }
        sender.sendMessage(RewardableActivitiesConstants.PREFIX + ChatColor.WHITE + "Available commands:");
        sender.sendMessage(RewardableActivitiesConstants.PREFIX + ChatColor.GRAY + "/rewardable-activities reload" + ChatColor.WHITE + " - Reloads plugin.");
        return true;
    }
}
