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

package dev.jaqobb.rewardable_activities.data;

import dev.jaqobb.rewardable_activities.RewardableActivitiesPlugin;
import dev.jaqobb.rewardable_activities.util.RandomUtils;
import java.util.Collection;
import java.util.Collections;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class RewardableActivityReward {

    private final String group;
    private final double chance;
    private final double minimumEconomy;
    private final double maximumEconomy;
    private final Collection<String> commands;

    public RewardableActivityReward(String group, double chance, double minimumEconomy, double maximumEconomy, Collection<String> commands) {
        this.group = group;
        this.chance = chance;
        this.minimumEconomy = minimumEconomy;
        this.maximumEconomy = maximumEconomy;
        this.commands = commands;
    }

    public String getGroup() {
        return this.group;
    }

    public double getChance() {
        return this.chance;
    }

    public boolean testChance() {
        return RandomUtils.chance(this.chance);
    }

    public double getMinimumEconomy() {
        return this.minimumEconomy;
    }

    public double getMaximumEconomy() {
        return this.maximumEconomy;
    }

    public double getRandomEconomy() {
        return RandomUtils.getRandomDouble(this.minimumEconomy, this.maximumEconomy);
    }

    public void depositEconomy(Economy economy, Player player, double amount) {
        economy.depositPlayer(player, amount);
    }

    public Collection<String> getCommands() {
        return Collections.unmodifiableCollection(this.commands);
    }

    public void executeCommands(RewardableActivitiesPlugin plugin, Player player) {
        this.commands.forEach(command -> {
            command = command.replace("{player}", player.getName()).replace("{group}", this.group);
            if (plugin.isPlaceholderApiPresent()) {
                command = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, command);
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    public void reward(RewardableActivitiesPlugin plugin, Player player) {
        if (plugin.getEconomy() != null && this.minimumEconomy >= 0.0D && this.maximumEconomy > 0.0D && this.minimumEconomy <= this.maximumEconomy) {
            double economy = this.getRandomEconomy();
            if (economy > 0.0D) {
                this.depositEconomy(plugin.getEconomy(), player, economy);
            }
        }
        this.executeCommands(plugin, player);
    }
}
