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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public final class EntityDamageByEntityListener implements Listener {

    private final RewardableActivitiesPlugin plugin;

    public EntityDamageByEntityListener(RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity victim   = (LivingEntity) event.getEntity();
        Player       attacker = this.getPlayer(event.getDamager());
        if (attacker == null) {
            return;
        }
        if (victim.getHealth() - event.getFinalDamage() > 0.0D) {
            return;
        }
        if (this.plugin.isEntityBreedOwnershipCheckEnabled() && this.plugin.hasMetadata(victim, RewardableActivitiesConstants.ENTITY_BRED_BY_PLAYER_KEY)) {
            return;
        }
        if (this.plugin.isEntitySpawnerOwnershipCheckEnabled() && this.plugin.hasMetadata(victim, RewardableActivitiesConstants.ENTITY_SPAWNED_BY_SPAWNER_KEY)) {
            return;
        }
        RewardableActivity activity = this.plugin.getRepository().getEntityKillRewardableActivity(victim.getType());
        if (activity == null) {
            return;
        }
        RewardableActivityReward reward = activity.getReward(attacker);
        if (reward != null) {
            reward.reward(this.plugin, attacker);
        }
    }

    private Player getPlayer(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        }
        if (entity instanceof Projectile) {
            Projectile       projectile       = (Projectile) entity;
            ProjectileSource projectileSource = projectile.getShooter();
            if (projectileSource instanceof Player) {
                return (Player) projectileSource;
            }
        }
        return null;
    }
}
