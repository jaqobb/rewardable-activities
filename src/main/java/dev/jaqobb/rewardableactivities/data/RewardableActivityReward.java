/*
 * MIT License
 *
 * Copyright (c) 2020 Jakub Zagórski (jaqobb)
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

package dev.jaqobb.rewardableactivities.data;

import dev.jaqobb.rewardableactivities.util.RandomUtils;
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

    public RewardableActivityReward(
        final String group,
        final double chance,
        final double minimumEconomy,
        final double maximumEconomy,
        final Collection<String> commands
    ) {
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

    public void depositEconomy(
        final Economy economy,
        final Player player,
        final double economyToDeposit
    ) {
        economy.depositPlayer(player, economyToDeposit);
    }

    public Collection<String> getCommands() {
        return Collections.unmodifiableCollection(this.commands);
    }

    public void executeCommands(final Player player) {
        this.commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
    }
}
