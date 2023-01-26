/*
 * MIT License
 *
 * Copyright (c) 2020-2023 Jakub Zagórski (jaqobb)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaqobb.rewardable_activities.command;

import dev.jaqobb.rewardable_activities.RewardableActivitiesConstants;
import dev.jaqobb.rewardable_activities.RewardableActivitiesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class RewardableActivitiesCommand implements CommandExecutor {

    private final RewardableActivitiesPlugin plugin;

    public RewardableActivitiesCommand(RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
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
