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

import com.cryptomorin.xseries.XMaterial;
import dev.jaqobb.rewardable_activities.RewardableActivitiesPlugin;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public final class RewardableActivityRepository {

    private final RewardableActivitiesPlugin plugin;
    private final Map<XMaterial, RewardableActivity> blockBreakActivities;
    private final Map<XMaterial, RewardableActivity> blockPlaceActivities;
    private final Map<EntityType, RewardableActivity> entityKillActivities;
    private final Map<EntityType, RewardableActivity> entityBreedActivities;
    private final Map<XMaterial, RewardableActivity> itemFishActivities;

    public RewardableActivityRepository(RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
        this.blockBreakActivities = new EnumMap<>(XMaterial.class);
        this.blockPlaceActivities = new EnumMap<>(XMaterial.class);
        this.entityKillActivities = new EnumMap<>(EntityType.class);
        this.entityBreedActivities = new EnumMap<>(EntityType.class);
        this.itemFishActivities = new EnumMap<>(XMaterial.class);
    }

    public void loadAllActivities(boolean reload) {
        this.loadBlockBreakActivities(reload);
        this.loadBlockPlaceActivities(reload);
        this.loadEntityKillActivities(reload);
        this.loadEntityBreedActivities(reload);
        this.loadItemFishActivities(reload);
    }

    public void loadBlockBreakActivities(boolean reload) {
        if (reload) {
            this.blockBreakActivities.clear();
        }
        this.blockBreakActivities.putAll(this.loadActivities("block.break", type -> {
            XMaterial material = XMaterial.matchXMaterial(type.toUpperCase()).orElse(null);
            if (material == null) {
                throw new EnumConstantNotPresentException(XMaterial.class, type);
            }
            return material;
        }));
    }

    public void loadBlockPlaceActivities(boolean reload) {
        if (reload) {
            this.blockPlaceActivities.clear();
        }
        this.blockPlaceActivities.putAll(this.loadActivities("block.place", type -> {
            XMaterial material = XMaterial.matchXMaterial(type.toUpperCase()).orElse(null);
            if (material == null) {
                throw new EnumConstantNotPresentException(XMaterial.class, type);
            }
            return material;
        }));
    }

    public void loadEntityKillActivities(boolean reload) {
        if (reload) {
            this.entityKillActivities.clear();
        }
        this.entityKillActivities.putAll(this.loadActivities("entity.kill", type -> EntityType.valueOf(type.toUpperCase())));
    }

    public void loadEntityBreedActivities(boolean reload) {
        if (reload) {
            this.entityBreedActivities.clear();
        }
        this.entityBreedActivities.putAll(this.loadActivities("entity.breed", type -> EntityType.valueOf(type.toUpperCase())));
    }

    public void loadItemFishActivities(boolean reload) {
        if (reload) {
            this.itemFishActivities.clear();
        }
        this.itemFishActivities.putAll(this.loadActivities("item.fish", type -> {
            XMaterial material = XMaterial.matchXMaterial(type.toUpperCase()).orElse(null);
            if (material == null) {
                throw new EnumConstantNotPresentException(XMaterial.class, type);
            }
            return material;
        }));
    }

    @SuppressWarnings("unchecked")
    private <T> Map<T, RewardableActivity> loadActivities(String path, Function<String, T> keyFunction) {
        Map<T, RewardableActivity> activities = new HashMap<>(16);
        ConfigurationSection mainSection = this.plugin.getConfig().getConfigurationSection(path);
        if (mainSection == null) {
            return activities;
        }
        for (String key : mainSection.getKeys(false)) {
            Map<String, List<RewardableActivityReward>> rewardMap = new LinkedHashMap<>(16);
            ConfigurationSection rewardSection = mainSection.getConfigurationSection(key);
            for (String group : rewardSection.getKeys(false)) {
                List<RewardableActivityReward> rewards = new LinkedList<>();
                if (rewardSection.isList(group)) {
                    for (Map<?, ?> groupSection : rewardSection.getMapList(group)) {
                        Number chance = (Number) groupSection.get("chance");
                        Number minimumEconomy = groupSection.containsKey("minimum-economy") ? (Number) groupSection.get("minimum-economy") : 0.0D;
                        Number maximumEconomy = groupSection.containsKey("maximum-economy") ? (Number) groupSection.get("maximum-economy") : 0.0D;
                        Collection<String> commands = (List<String>) groupSection.get("commands");
                        rewards.add(new RewardableActivityReward(group, chance.doubleValue(), minimumEconomy.doubleValue(), maximumEconomy.doubleValue(), commands));
                    }
                    rewardMap.put(group, rewards);
                } else {
                    ConfigurationSection groupSection = rewardSection.getConfigurationSection(group);
                    Number chance = (Number) groupSection.get("chance");
                    Number minimumEconomy = groupSection.isSet("minimum-economy") ? (Number) groupSection.get("minimum-economy") : 0.0D;
                    Number maximumEconomy = groupSection.isSet("maximum-economy") ? (Number) groupSection.get("maximum-economy") : 0.0D;
                    Collection<String> commands = groupSection.getStringList("commands");
                    rewards.add(new RewardableActivityReward(group, chance.doubleValue(), minimumEconomy.doubleValue(), maximumEconomy.doubleValue(), commands));
                    rewardMap.put(group, rewards);
                }
            }
            activities.put(keyFunction.apply(key), new RewardableActivity(key, rewardMap));
        }
        return activities;
    }

    public Collection<RewardableActivity> getBlockBreakActivities() {
        return Collections.unmodifiableCollection(this.blockBreakActivities.values());
    }

    public RewardableActivity getBlockBreakActivity(XMaterial material) {
        return this.blockBreakActivities.get(material);
    }

    public Collection<RewardableActivity> getBlockPlaceActivities() {
        return Collections.unmodifiableCollection(this.blockPlaceActivities.values());
    }

    public RewardableActivity getBlockPlaceActivity(XMaterial material) {
        return this.blockPlaceActivities.get(material);
    }

    public Collection<RewardableActivity> getEntityKillActivities() {
        return Collections.unmodifiableCollection(this.entityKillActivities.values());
    }

    public RewardableActivity getEntityKillActivity(EntityType type) {
        return this.entityKillActivities.get(type);
    }

    public Collection<RewardableActivity> getEntityBreedActivities() {
        return Collections.unmodifiableCollection(this.entityBreedActivities.values());
    }

    public RewardableActivity getEntityBreedActivity(EntityType type) {
        return this.entityBreedActivities.get(type);
    }

    public Collection<RewardableActivity> getItemFishActivities() {
        return Collections.unmodifiableCollection(this.itemFishActivities.values());
    }

    public RewardableActivity getItemFishActivity(XMaterial material) {
        return this.itemFishActivities.get(material);
    }
}
