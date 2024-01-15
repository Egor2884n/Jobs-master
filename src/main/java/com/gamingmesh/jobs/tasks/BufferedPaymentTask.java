/**
 * Jobs Plugin for Bukkit
 * Copyright (C) 2011 Zak Ford <zak.j.ford@gmail.com>
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.gamingmesh.jobs.tasks;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.CurrencyType;
import com.gamingmesh.jobs.economy.BufferedEconomy;
import com.gamingmesh.jobs.economy.BufferedPayment;
import com.gamingmesh.jobs.economy.Economy;

import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class BufferedPaymentTask implements Runnable {

    private BufferedEconomy bufferedEconomy;
    private Economy economy;
    private BufferedPayment payment;
    private Jobs plugin;

    public BufferedPaymentTask(BufferedEconomy bufferedEconomy, Economy economy, BufferedPayment payment, Jobs plugin) {
        this.bufferedEconomy = bufferedEconomy;
        this.economy = economy;
        this.payment = payment;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        double money = payment.get(CurrencyType.MONEY);
        if (money > 0) {
            if (Jobs.getGCManager().isEconomyAsync()) {
                CMIScheduler.get().runTaskAsynchronously(() -> economy.depositPlayer(payment.getOfflinePlayer(), money));
            } else {
                economy.depositPlayer(payment.getOfflinePlayer(), money);
            }
        } else if (!economy.withdrawPlayer(payment.getOfflinePlayer(), -money)) {
            bufferedEconomy.pay(payment);
        }

        double points = payment.get(CurrencyType.POINTS);
        if (points != 0D) {
            Jobs.getPlayerManager().getJobsPlayer(payment.getOfflinePlayer().getUniqueId()).getPointsData().addPoints(points);
        }

        double skill_exp = payment.get(CurrencyType.SKILLS_EXP);
        if (skill_exp != 0D) {
            try {
                String skill = "null";
                switch (payment.getJobName()) {
                    case "§e§lИсследователь": skill = "endurance"; break;
                    case "§e§lЗачарователь": skill = "enchanting"; break;
                    case "§e§lАлхимик": skill = "alchemy"; break;
                    case "§e§lОхотник": {
                        Material material = Bukkit.getPlayer(payment.getOfflinePlayer().getName()).getInventory().getItemInMainHand().getType();
                        if (material == Material.BOW || material == Material.CROSSBOW) {
                            skill = "archery";
                        } else {
                            skill = "fighting";
                        } break;
                    }
                    case "§e§lСельхоз": skill = "excavation"; break;
                    case "§e§lРыбак": skill = "fishing"; break;
                    case "§e§lШахтёр": skill = "mining"; break;
                    case "§e§lЛесоруб": skill = "foraging"; break;
                    case "§e§lФермер": skill = "farming"; break;
                }

                String finalSkill = skill;
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sk xp add " + payment.getOfflinePlayer().getName() + " " + finalSkill + " " + skill_exp));
            } catch (Exception ignore) {}
        }
    }
}