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

package dev.jaqobb.rewardable_activities.listener.block;

import com.cryptomorin.xseries.XMaterial;
import dev.jaqobb.rewardable_activities.RewardableActivitiesConstants;
import dev.jaqobb.rewardable_activities.RewardableActivitiesPlugin;
import dev.jaqobb.rewardable_activities.data.RewardableActivity;
import dev.jaqobb.rewardable_activities.data.RewardableActivityReward;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreakListener implements Listener {

    private final RewardableActivitiesPlugin plugin;

    public BlockBreakListener(RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (this.plugin.isBlockPlaceOwnershipCheckEnabled() && !this.plugin.hasMetadata(block, RewardableActivitiesConstants.BLOCK_BROKEN_BY_PLAYER_KEY)) {
            this.plugin.setMetadata(block, RewardableActivitiesConstants.BLOCK_BROKEN_BY_PLAYER_KEY, true);
        }
        if (this.plugin.isBlockBreakOwnershipCheckEnabled() && this.plugin.hasMetadata(block, RewardableActivitiesConstants.BLOCK_PLACED_BY_PLAYER_KEY)) {
            this.plugin.unsetMetadata(block, RewardableActivitiesConstants.BLOCK_PLACED_BY_PLAYER_KEY);
            return;
        }
        RewardableActivity activity = this.plugin.getRepository().getBlockBreakActivity(XMaterial.matchXMaterial(block.getType()));
        if (activity == null) {
            return;
        }
        Player player = event.getPlayer();
        RewardableActivityReward reward = activity.getReward(player);
        if (reward != null) {
            reward.reward(this.plugin, player);
        }
    }
}
