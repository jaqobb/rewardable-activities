/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Jakub Zagórski (jaqobb)
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

package dev.jaqobb.rewardable_activities.listener.entity;

import dev.jaqobb.rewardable_activities.RewardableActivitiesConstants;
import dev.jaqobb.rewardable_activities.RewardableActivitiesPlugin;
import dev.jaqobb.rewardable_activities.data.RewardableActivity;
import dev.jaqobb.rewardable_activities.data.RewardableActivityReward;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

public final class EntityBreedListener implements Listener {

    private final RewardableActivitiesPlugin plugin;

    public EntityBreedListener(RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityBreed(EntityBreedEvent event) {
        Entity child   = event.getEntity();
        Entity breeder = event.getBreeder();
        if (breeder == null) {
            return;
        }
        if (!(breeder instanceof Player)) {
            return;
        }
        Player breederPlayer = (Player) breeder;
        if (this.plugin.isEntityBreedOwnershipCheckEnabled()) {
            this.plugin.setMetadata(child, RewardableActivitiesConstants.ENTITY_BRED_BY_PLAYER_KEY, true);
        }
        RewardableActivity activity = this.plugin.getRepository().getEntityBreedRewardableActivity(child.getType());
        if (activity == null) {
            return;
        }
        RewardableActivityReward reward = activity.getReward(breederPlayer);
        if (reward != null) {
            reward.reward(this.plugin, breederPlayer);
        }
    }
}
