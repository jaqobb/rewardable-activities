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

package dev.jaqobb.rewardableactivities.listener.entity;

import dev.jaqobb.rewardableactivities.RewardableActivitiesPlugin;
import dev.jaqobb.rewardableactivities.data.RewardableActivity;
import dev.jaqobb.rewardableactivities.data.RewardableActivityReward;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

public final class EntityBreedListener implements Listener {

    private final RewardableActivitiesPlugin plugin;

    public EntityBreedListener(final RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityBreed(final EntityBreedEvent event) {
        Entity breeder = event.getBreeder();
        if (breeder == null) {
            return;
        }
        if (!(breeder instanceof Player)) {
            return;
        }
        Player breederPlayer = (Player) breeder;
        RewardableActivity rewardableActivity = this.plugin.getRepository().getEntityBreedRewardableActivity(event.getEntity().getType());
        if (rewardableActivity == null) {
            return;
        }
        RewardableActivityReward rewardableActivityReward = rewardableActivity.getReward(breederPlayer);
        if (rewardableActivityReward != null) {
            rewardableActivityReward.reward(this.plugin.getEconomy(), breederPlayer);
        }
    }
}
