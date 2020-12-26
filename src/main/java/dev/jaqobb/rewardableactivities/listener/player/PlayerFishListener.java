package dev.jaqobb.rewardableactivities.listener.player;

import com.cryptomorin.xseries.XMaterial;
import dev.jaqobb.rewardableactivities.RewardableActivitiesPlugin;
import dev.jaqobb.rewardableactivities.data.RewardableActivity;
import dev.jaqobb.rewardableactivities.data.RewardableActivityReward;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public final class PlayerFishListener implements Listener {

    private final RewardableActivitiesPlugin plugin;

    public PlayerFishListener(final RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFish(final PlayerFishEvent event) {
        Player player = event.getPlayer();
        Entity caught = event.getCaught();
        if (caught == null) {
            return;
        }
        Item caughtItem = (Item) caught;
        RewardableActivity rewardableActivity = this.plugin.getRepository().getItemFishRewardableActivity(XMaterial.matchXMaterial(caughtItem.getItemStack().getType()));
        if (rewardableActivity == null) {
            return;
        }
        RewardableActivityReward rewardableActivityReward = rewardableActivity.getReward(player);
        if (rewardableActivityReward != null) {
            rewardableActivityReward.reward(this.plugin.getEconomy(), player);
        }
    }
}
