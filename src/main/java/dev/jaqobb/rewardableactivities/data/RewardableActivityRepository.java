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

import com.cryptomorin.xseries.XMaterial;
import dev.jaqobb.rewardableactivities.RewardableActivitiesPlugin;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public final class RewardableActivityRepository {

    private final RewardableActivitiesPlugin plugin;
    private final Map<XMaterial, RewardableActivity> blockBreakRewardableActivities;
    private final Map<XMaterial, RewardableActivity> blockPlaceRewardableActivities;
    private final Map<EntityType, RewardableActivity> entityKillRewardableActivities;
    private final Map<EntityType, RewardableActivity> entityBreedRewardableActivities;

    public RewardableActivityRepository(final RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
        this.blockBreakRewardableActivities = new EnumMap<>(XMaterial.class);
        this.blockPlaceRewardableActivities = new EnumMap<>(XMaterial.class);
        this.entityKillRewardableActivities = new EnumMap<>(EntityType.class);
        this.entityBreedRewardableActivities = new EnumMap<>(EntityType.class);
    }

    public void loadAllRewardableActivities() {
        this.loadBlockBreakRewardableActivities();
        this.loadBlockPlaceRewardableActivities();
        this.loadEntityKillRewardableActivities();
        this.loadEntityBreedRewardableActivities();
    }

    public void loadBlockBreakRewardableActivities() {
        this.blockBreakRewardableActivities.clear();
        this.blockBreakRewardableActivities.putAll(this.loadRewardableActivities("block.break", materialType -> XMaterial.matchXMaterial(materialType.toUpperCase()).orElse(null)));
    }

    public void loadBlockPlaceRewardableActivities() {
        this.blockPlaceRewardableActivities.clear();
        this.blockPlaceRewardableActivities.putAll(this.loadRewardableActivities("block.place", materialType -> XMaterial.matchXMaterial(materialType.toUpperCase()).orElse(null)));
    }

    @SuppressWarnings("deprecation")
    public void loadEntityKillRewardableActivities() {
        this.entityKillRewardableActivities.clear();
        this.entityKillRewardableActivities.putAll(this.loadRewardableActivities("entity.kill", entityType -> EntityType.fromName(entityType.toLowerCase())));
    }

    @SuppressWarnings("deprecation")
    public void loadEntityBreedRewardableActivities() {
        this.entityBreedRewardableActivities.clear();
        this.entityBreedRewardableActivities.putAll(this.loadRewardableActivities("entity.breed", entityType -> EntityType.fromName(entityType.toLowerCase())));
    }

    private <T> Map<T, RewardableActivity> loadRewardableActivities(
        final String path,
        final Function<String, T> keyFunction
    ) {
        Map<T, RewardableActivity> rewardableActivities = new HashMap<>(16);
        ConfigurationSection mainSection = this.plugin.getConfig().getConfigurationSection(path);
        if (mainSection == null) {
            return rewardableActivities;
        }
        for (String key : mainSection.getKeys(false)) {
            Map<String, RewardableActivityReward> rewards = new LinkedHashMap<>(16);
            ConfigurationSection rewardsSection = mainSection.getConfigurationSection(key);
            for (String group : rewardsSection.getKeys(false)) {
                ConfigurationSection rewardSection = rewardsSection.getConfigurationSection(group);
                Number chance = (Number) rewardSection.get("chance");
                Number minimumEconomy = rewardSection.isSet("minimum-economy") ? (Number) rewardSection.get("minimum-economy") : 0.0D;
                Number maximumEconomy = rewardSection.isSet("maximum-economy") ? (Number) rewardSection.get("maximum-economy") : 0.0D;
                Collection<String> commands = rewardSection.getStringList("commands");
                rewards.put(group, new RewardableActivityReward(group, chance.doubleValue(), minimumEconomy.doubleValue(), maximumEconomy.doubleValue(), commands));
            }
            rewardableActivities.put(keyFunction.apply(key), new RewardableActivity(key, rewards));
        }
        return rewardableActivities;
    }

    public Collection<RewardableActivity> getBlockBreakRewardableActivities() {
        return Collections.unmodifiableCollection(this.blockBreakRewardableActivities.values());
    }

    public RewardableActivity getBlockBreakRewardableActivity(final XMaterial material) {
        return this.blockBreakRewardableActivities.get(material);
    }

    public Collection<RewardableActivity> getBlockPlaceRewardableActivities() {
        return Collections.unmodifiableCollection(this.blockPlaceRewardableActivities.values());
    }

    public RewardableActivity getBlockPlaceRewardableActivity(final XMaterial material) {
        return this.blockPlaceRewardableActivities.get(material);
    }

    public Collection<RewardableActivity> getEntityKillRewardableActivities() {
        return Collections.unmodifiableCollection(this.entityKillRewardableActivities.values());
    }

    public RewardableActivity getEntityKillRewardableActivity(final EntityType entityType) {
        return this.entityKillRewardableActivities.get(entityType);
    }

    public Collection<RewardableActivity> getEntityBreedRewardableActivities() {
        return Collections.unmodifiableCollection(this.entityBreedRewardableActivities.values());
    }

    public RewardableActivity getEntityBreedRewardableActivity(final EntityType entityType) {
        return this.entityBreedRewardableActivities.get(entityType);
    }
}
