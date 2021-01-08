/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Jakub Zagórski (jaqobb)
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

package dev.jaqobb.rewardableactivities;

import dev.jaqobb.rewardableactivities.command.RewardableActivitiesCommand;
import dev.jaqobb.rewardableactivities.command.RewardableActivitiesCommandTabCompleter;
import dev.jaqobb.rewardableactivities.data.RewardableActivityRepository;
import dev.jaqobb.rewardableactivities.listener.block.BlockBreakListener;
import dev.jaqobb.rewardableactivities.listener.block.BlockExplodeListener;
import dev.jaqobb.rewardableactivities.listener.block.BlockPistonExtendListener;
import dev.jaqobb.rewardableactivities.listener.block.BlockPistonRetractListener;
import dev.jaqobb.rewardableactivities.listener.block.BlockPlaceListener;
import dev.jaqobb.rewardableactivities.listener.entity.EntityBreedListener;
import dev.jaqobb.rewardableactivities.listener.entity.EntityDamageByEntityListener;
import dev.jaqobb.rewardableactivities.listener.entity.EntityExplodeListener;
import dev.jaqobb.rewardableactivities.listener.entity.SpawnerSpawnListener;
import dev.jaqobb.rewardableactivities.listener.player.PlayerFishListener;
import dev.jaqobb.rewardableactivities.listener.player.PlayerJoinListener;
import dev.jaqobb.rewardableactivities.updater.Updater;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class RewardableActivitiesPlugin extends JavaPlugin {

    private boolean blockBreakOwnershipCheckEnabled;
    private boolean blockPlaceOwnershipCheckEnabled;
    private boolean entityBreedOwnershipCheckEnabled;
    private boolean entitySpawnerOwnershipCheckEnabled;
    private Metrics metrics;
    private Updater updater;
    private Economy economy;
    private RewardableActivityRepository repository;

    @Override
    public void onLoad() {
        this.saveDefaultConfig();
        this.loadConfig(false);
    }

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "Starting metrics...");
        this.metrics = new Metrics(this, 9499);
        this.getLogger().log(Level.INFO, "Starting updater...");
        this.updater = new Updater(this, 86090);
        this.updater.runTaskTimerAsynchronously(this, 0L, 20L * 60L * 30L);
        this.economy = this.setupEconomy();
        if (this.economy != null) {
            this.getLogger().log(Level.INFO, "Economy has been successfully setup.");
            this.getLogger().log(Level.INFO, "Economy provider: " + this.economy.getName());
        } else {
            this.getLogger().log(Level.INFO, "Could not find Vault or economy plugin, economy rewards will not be supported.");
        }
        this.getLogger().log(Level.INFO, "Registering command...");
        this.getCommand("rewardable-activities").setExecutor(new RewardableActivitiesCommand(this));
        this.getCommand("rewardable-activities").setTabCompleter(new RewardableActivitiesCommandTabCompleter());
        this.getLogger().log(Level.INFO, "Registering listeners...");
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new BlockBreakListener(this), this);
        pluginManager.registerEvents(new BlockExplodeListener(this), this);
        pluginManager.registerEvents(new BlockPistonExtendListener(this), this);
        pluginManager.registerEvents(new BlockPistonRetractListener(this), this);
        pluginManager.registerEvents(new BlockPlaceListener(this), this);
        pluginManager.registerEvents(new EntityBreedListener(this), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(this), this);
        pluginManager.registerEvents(new EntityExplodeListener(this), this);
        pluginManager.registerEvents(new SpawnerSpawnListener(this), this);
        pluginManager.registerEvents(new PlayerFishListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
    }

    public void loadConfig(final boolean reload) {
        this.getLogger().log(Level.INFO, (reload ? "Rel" : "L") + "oading configuration...");
        this.blockBreakOwnershipCheckEnabled = this.getConfig().getBoolean("block.ownership-check.break", this.getConfig().getBoolean("block.ownership-check.place", this.getConfig().getBoolean("block.ownership-check")));
        this.blockPlaceOwnershipCheckEnabled = this.getConfig().getBoolean("block.ownership-check.place", true);
        this.entityBreedOwnershipCheckEnabled = this.getConfig().getBoolean("entity.ownership-check.breed", this.getConfig().getBoolean("entity.ownership-check", true));
        this.entitySpawnerOwnershipCheckEnabled = this.getConfig().getBoolean("entity.ownership-check.spawner", true);
        this.getLogger().log(Level.INFO, (reload ? "Rel" : "L") + "oading rewardable activities...");
        if (this.repository == null) {
            this.repository = new RewardableActivityRepository(this);
        }
        this.repository.loadAllRewardableActivities(reload);
        this.getLogger().log(Level.INFO, (reload ? "Rel" : "L") + "oaded rewardable activities:");
        this.getLogger().log(Level.INFO, " * Block break: " + this.repository.getBlockBreakRewardableActivities().size());
        this.getLogger().log(Level.INFO, " * Block place: " + this.repository.getBlockPlaceRewardableActivities().size());
        this.getLogger().log(Level.INFO, " * Entity kill: " + this.repository.getEntityKillRewardableActivities().size());
        this.getLogger().log(Level.INFO, " * Entity breed: " + this.repository.getEntityBreedRewardableActivities().size());
        this.getLogger().log(Level.INFO, " * Item fish: " + this.repository.getItemFishRewardableActivities().size());
    }

    public boolean isBlockBreakOwnershipCheckEnabled() {
        return this.blockBreakOwnershipCheckEnabled;
    }

    public boolean isBlockPlaceOwnershipCheckEnabled() {
        return this.blockPlaceOwnershipCheckEnabled;
    }

    public boolean isEntityBreedOwnershipCheckEnabled() {
        return this.entityBreedOwnershipCheckEnabled;
    }

    public boolean isEntitySpawnerOwnershipCheckEnabled() {
        return this.entitySpawnerOwnershipCheckEnabled;
    }

    public Metrics getMetrics() {
        return this.metrics;
    }

    public Updater getUpdater() {
        return this.updater;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public RewardableActivityRepository getRepository() {
        return this.repository;
    }

    public boolean hasMetadata(
        final Metadatable metadatable,
        final String key
    ) {
        return metadatable.hasMetadata(key);
    }

    public void setMetadata(
        final Metadatable metadatable,
        final String key,
        final Object value
    ) {
        if (!metadatable.hasMetadata(key)) {
            metadatable.setMetadata(key, new FixedMetadataValue(this, value));
        }
    }

    public void unsetMetadata(
        final Metadatable metadatable,
        final String key
    ) {
        if (metadatable.hasMetadata(key)) {
            metadatable.removeMetadata(key, this);
        }
    }

    public void updatePistonBlocks(
        final BlockFace direction,
        final List<Block> blocks
    ) {
        List<Block> blocksPlacedByPlayer = new ArrayList<>(blocks.size());
        List<Block> blocksSoonToBePlacedByPlayer = new ArrayList<>(blocks.size());
        for (Block block : blocks) {
            if (!blocksPlacedByPlayer.contains(block) && this.hasMetadata(block, RewardableActivitiesConstants.BLOCK_PLACED_BY_PLAYER_KEY)) {
                blocksPlacedByPlayer.add(block);
            }
        }
        for (Block block : blocks) {
            if (blocksPlacedByPlayer.contains(block)) {
                if (!blocksSoonToBePlacedByPlayer.contains(block)) {
                    this.unsetMetadata(block, RewardableActivitiesConstants.BLOCK_PLACED_BY_PLAYER_KEY);
                }
                Block blockSoonToBePlacedByPlayer = block.getRelative(direction);
                blocksSoonToBePlacedByPlayer.add(blockSoonToBePlacedByPlayer);
                this.setMetadata(blockSoonToBePlacedByPlayer, RewardableActivitiesConstants.BLOCK_PLACED_BY_PLAYER_KEY, true);
            }
        }
    }

    private Economy setupEconomy() {
        if (!this.getServer().getPluginManager().isPluginEnabled("Vault")) {
            return null;
        }
        RegisteredServiceProvider<Economy> economyServiceProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyServiceProvider == null) {
            return null;
        }
        return this.economy = economyServiceProvider.getProvider();
    }
}
