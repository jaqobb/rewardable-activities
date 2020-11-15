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

package dev.jaqobb.rewardableactivities;

import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class RewardableActivitiesPlugin extends JavaPlugin {

    private Economy economy;

    @Override
    public void onEnable() {
        this.economy = this.setupEconomy();
        if (this.economy != null) {
            this.getLogger().log(Level.INFO, "Economy has been successfully setup.");
            this.getLogger().log(Level.INFO, "Economy provider: " + this.economy.getName());
        } else {
            this.getLogger().log(Level.INFO, "Could not find Vault or economy plugin, economy rewards will not be supported.");
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
