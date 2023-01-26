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

package dev.jaqobb.rewardable_activities;

import net.md_5.bungee.api.ChatColor;

public final class RewardableActivitiesConstants {

    public static final String PREFIX = ChatColor.GRAY + "Rewardable Activities" + ChatColor.GOLD + ChatColor.BOLD + " > ";

    public static final String PLACEHOLDER_API_PLUGIN_NAME = "PlaceholderAPI";

    public static final String BLOCK_BROKEN_BY_PLAYER_KEY = "rewardableactivities:broken_by_player";
    public static final String BLOCK_PLACED_BY_PLAYER_KEY = "rewardableactivities:placed_by_player";
    public static final String BLOCK_PLACED_BY_PLAYER_2_KEY = "rewardableactivities:placed_by_player_2";

    public static final String ENTITY_BRED_BY_PLAYER_KEY = "rewardableactivities:bred_by_player";
    public static final String ENTITY_SPAWNED_BY_SPAWNER_KEY = "rewardableactivities:spawned_by_spawner";

    private RewardableActivitiesConstants() {
        throw new UnsupportedOperationException("Cannot create instance of this class");
    }
}
