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

package dev.jaqobb.rewardable_activities.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;

public final class RewardableActivity {

    private final String id;
    private final Map<String, List<RewardableActivityReward>> rewards;

    public RewardableActivity(String id, Map<String, List<RewardableActivityReward>> rewards) {
        this.id = id;
        this.rewards = rewards;
    }

    public String getId() {
        return this.id;
    }

    public Map<String, List<RewardableActivityReward>> getRewards() {
        return Collections.unmodifiableMap(this.rewards);
    }

    public List<RewardableActivityReward> getRewards(Player player) {
        String groupToUse = "default";
        for (String group : this.rewards.keySet()) {
            if (player.hasPermission("rewardableactivities.group." + group)) {
                groupToUse = group;
            }
        }
        return this.rewards.get(groupToUse);
    }

    public RewardableActivityReward getReward(Player player) {
        for (RewardableActivityReward reward : this.getRewards(player)) {
            if (reward.testChance()) {
                return reward;
            }
        }
        return null;
    }
}
